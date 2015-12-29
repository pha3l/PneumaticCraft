package pneumaticCraft.common.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;
import pneumaticCraft.PneumaticCraft;
import pneumaticCraft.common.NBTUtil;
import pneumaticCraft.common.network.NetworkHandler;
import pneumaticCraft.common.network.PacketSyncAmadronOffers;
import pneumaticCraft.common.recipes.AmadronOffer;
import pneumaticCraft.common.recipes.AmadronOfferCustom;
import pneumaticCraft.common.recipes.AmadronOfferManager;
import pneumaticCraft.common.util.IOHelper;
import pneumaticCraft.common.util.PneumaticCraftUtils;
import pneumaticCraft.proxy.CommonProxy.EnumGuiId;

public class ItemAmadronTablet extends ItemPressurizable implements IAmadronInterface{

    public ItemAmadronTablet(String textureLocation, int maxAir, int volume){
        super(textureLocation, maxAir, volume);
        setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
        if(!world.isRemote) {
            NetworkHandler.sendTo(new PacketSyncAmadronOffers(AmadronOfferManager.getInstance().getAllOffers()), (EntityPlayerMP)player);
            player.openGui(PneumaticCraft.instance, EnumGuiId.AMADRON.ordinal(), player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack tablet, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float par8, float par9, float par10){

        TileEntity te = world.getTileEntity(pos);
        if(te instanceof IFluidHandler) {
            if(!world.isRemote) {
                setLiquidProvidingLocation(tablet, pos, world.provider.getDimensionId());
                player.addChatComponentMessage(new ChatComponentTranslation("message.amadronTable.setLiquidProvidingLocation", pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimensionId(), world.provider.getDimensionName()));
            }
        } else if(te instanceof IInventory) {
            if(!world.isRemote) {
                setItemProvidingLocation(tablet, pos, world.provider.getDimensionId());
                player.addChatComponentMessage(new ChatComponentTranslation("message.amadronTable.setItemProvidingLocation", pos.getX(), pos.getY(), pos.getZ(), world.provider.getDimensionId(), world.provider.getDimensionName()));
            }
        } else {
            return false;
        }
        return true;

    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean par4){
        super.addInformation(stack, player, infoList, par4);
        BlockPos pos = getItemProvidingLocation(stack);
        if(pos != null) {
            int dim = getItemProvidingDimension(stack);
            infoList.add(I18n.format("gui.tooltip.amadronTablet.itemLocation", pos.getX(), pos.getY(), pos.getZ(), dim));
        } else {
            infoList.add(I18n.format("gui.tooltip.amadronTablet.selectItemLocation"));
        }

        pos = getLiquidProvidingLocation(stack);
        if(pos != null) {
            int dim = getLiquidProvidingDimension(stack);
            infoList.add(I18n.format("gui.tooltip.amadronTablet.fluidLocation", pos.getX(), pos.getY(), pos.getZ(), dim));
        } else {
            infoList.add(I18n.format("gui.tooltip.amadronTablet.selectFluidLocation"));
        }
    }

    public static IInventory getItemProvider(ItemStack tablet){
        BlockPos pos = getItemProvidingLocation(tablet);
        if(pos != null) {
            int dimension = getItemProvidingDimension(tablet);
            TileEntity te = PneumaticCraftUtils.getTileEntity(pos, dimension);
            return IOHelper.getInventoryForTE(te);
        }
        return null;
    }

    public static BlockPos getItemProvidingLocation(ItemStack tablet){
        NBTTagCompound compound = tablet.getTagCompound();
        if(compound != null) {
            int x = compound.getInteger("itemX");
            int y = compound.getInteger("itemY");
            int z = compound.getInteger("itemZ");
            if(x != 0 || y != 0 || z != 0) {
                return new BlockPos(x, y, z);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static int getItemProvidingDimension(ItemStack tablet){
        return tablet.hasTagCompound() ? tablet.getTagCompound().getInteger("itemDim") : 0;
    }

    public static void setItemProvidingLocation(ItemStack tablet, BlockPos pos, int dimensionId){
        NBTUtil.setInteger(tablet, "itemX", pos.getX());
        NBTUtil.setInteger(tablet, "itemY", pos.getY());
        NBTUtil.setInteger(tablet, "itemZ", pos.getZ());
        NBTUtil.setInteger(tablet, "itemDim", dimensionId);
    }

    public static IFluidHandler getLiquidProvider(ItemStack tablet){
        BlockPos pos = getLiquidProvidingLocation(tablet);
        if(pos != null) {
            int dimension = getLiquidProvidingDimension(tablet);
            TileEntity te = PneumaticCraftUtils.getTileEntity(pos, dimension);
            if(te instanceof IFluidHandler) return (IFluidHandler)te;
        }
        return null;
    }

    public static BlockPos getLiquidProvidingLocation(ItemStack tablet){
        NBTTagCompound compound = tablet.getTagCompound();
        if(compound != null) {
            int x = compound.getInteger("liquidX");
            int y = compound.getInteger("liquidY");
            int z = compound.getInteger("liquidZ");
            if(x != 0 || y != 0 || z != 0) {
                return new BlockPos(x, y, z);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static int getLiquidProvidingDimension(ItemStack tablet){
        return tablet.hasTagCompound() ? tablet.getTagCompound().getInteger("liquidDim") : 0;
    }

    public static void setLiquidProvidingLocation(ItemStack tablet, BlockPos pos, int dimensionId){
        NBTUtil.setInteger(tablet, "liquidX", pos.getX());
        NBTUtil.setInteger(tablet, "liquidY", pos.getY());
        NBTUtil.setInteger(tablet, "liquidZ", pos.getZ());
        NBTUtil.setInteger(tablet, "liquidDim", dimensionId);
    }

    public static Map<AmadronOffer, Integer> getShoppingCart(ItemStack tablet){
        Map<AmadronOffer, Integer> offers = new HashMap<AmadronOffer, Integer>();

        if(tablet.hasTagCompound() && tablet.getTagCompound().hasKey("shoppingCart")) {
            NBTTagList list = tablet.getTagCompound().getTagList("shoppingCart", 10);
            for(int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tag = list.getCompoundTagAt(i);
                offers.put(tag.hasKey("inStock") ? AmadronOfferCustom.loadFromNBT(tag) : AmadronOffer.loadFromNBT(tag), tag.getInteger("amount"));
            }
        }
        return offers;
    }

    public static void setShoppingCart(ItemStack tablet, Map<AmadronOffer, Integer> cart){
        NBTTagList list = new NBTTagList();
        for(Map.Entry<AmadronOffer, Integer> entry : cart.entrySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            entry.getKey().writeToNBT(tag);
            tag.setInteger("amount", entry.getValue());
            list.appendTag(tag);
        }
        NBTUtil.setCompoundTag(tablet, "shoppingCart", list);
    }
}
