package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.gui.ProjectEXGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockStoneTable extends Block
{
	public static final AxisAlignedBB AABB = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.25D, 1D);

	public BlockStoneTable()
	{
		super(Material.ROCK);
		setHardness(2F);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
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