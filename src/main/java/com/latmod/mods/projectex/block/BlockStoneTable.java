package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.gui.ProjectEXGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockStoneTable extends Block
{
	private static final AxisAlignedBB[] AABBS = new AxisAlignedBB[] {
			new AxisAlignedBB(0D, 0D, 0D, 1D, 0.25D, 1D),
			new AxisAlignedBB(0D, 0.75D, 0D, 1D, 1D, 1D),
			new AxisAlignedBB(0D, 0D, 0D, 1D, 1D, 0.25D),
			new AxisAlignedBB(0D, 0D, 0.75D, 1D, 1D, 1D),
			new AxisAlignedBB(0D, 0D, 0D, 0.25D, 1D, 1D),
			new AxisAlignedBB(0.75D, 0D, 0D, 1D, 1D, 1D)
	};

	public BlockStoneTable()
	{
		super(Material.ROCK);
		setHardness(2F);
		setDefaultState(blockState.getBaseState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockDirectional.FACING);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.byIndex(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockDirectional.FACING).getIndex();
	}

	@Override
	@Deprecated
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(BlockDirectional.FACING, facing.getOpposite());
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABBS[state.getValue(BlockDirectional.FACING).getIndex()];
	}

	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot)
	{
		return state.withProperty(BlockDirectional.FACING, rot.rotate(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirror)
	{
		return state.withRotation(mirror.toRotation(state.getValue(BlockDirectional.FACING)));
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			ProjectEXGuiHandler.open(player, ProjectEXGuiHandler.STONE_TABLE, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}
}