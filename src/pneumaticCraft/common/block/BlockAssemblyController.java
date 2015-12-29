package pneumaticCraft.common.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import pneumaticCraft.common.tileentity.TileEntityAssemblyController;
import pneumaticCraft.lib.BBConstants;
import pneumaticCraft.proxy.CommonProxy.EnumGuiId;

public class BlockAssemblyController extends BlockPneumaticCraftModeled{

    public BlockAssemblyController(Material par2Material){
        super(par2Material);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, BlockPos pos){
        setBlockBounds(BBConstants.ASSEMBLY_CONTROLLER_MIN_POS, 0F, BBConstants.ASSEMBLY_CONTROLLER_MIN_POS, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS_TOP, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS);
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){
        setBlockBounds(BBConstants.ASSEMBLY_CONTROLLER_MIN_POS, BBConstants.ASSEMBLY_CONTROLLER_MIN_POS, BBConstants.ASSEMBLY_CONTROLLER_MIN_POS, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS_TOP, BBConstants.ASSEMBLY_CONTROLLER_MAX_POS);
        super.addCollisionBoxesToList(world, pos, state, axisalignedbb, arraylist, par7Entity);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected Class<? extends TileEntity> getTileEntityClass(){
        return TileEntityAssemblyController.class;
    }

    @Override
    public EnumGuiId getGuiID(){
        return EnumGuiId.ASSEMBLY_CONTROLLER;
    }
}
