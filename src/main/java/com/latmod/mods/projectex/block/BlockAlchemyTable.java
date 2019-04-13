package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.gui.ProjectEXGuiHandler;
import com.latmod.mods.projectex.tile.TileAlchemyTable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class BlockAlchemyTable extends Block
{
	public BlockAlchemyTable()
	{
		super(Material.ROCK);
		setHardness(2F);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileAlchemyTable();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileAlchemyTable)
			{
				ProjectEXGuiHandler.open(player, ProjectEXGuiHandler.ALCHEMY_TABLE, pos.getX(), pos.getY(), pos.getZ());
			}
		}

		return true;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
	{
		double x = pos.getX();
		double y = pos.getY() + 1.15D;
		double z = pos.getZ();

		world.spawnParticle(EnumParticleTypes.FLAME, x + 2.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
		world.spawnParticle(EnumParticleTypes.FLAME, x + 13.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
		world.spawnParticle(EnumParticleTypes.FLAME, x + 13.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
		world.spawnParticle(EnumParticleTypes.FLAME, x + 2.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
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
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity tileentity = world.getTileEntity(pos);

		if (tileentity instanceof TileAlchemyTable)
		{
			ItemStackHandler items = ((TileAlchemyTable) tileentity).items;

			for (int i = 0; i < items.getSlots(); i++)
			{
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
			}
		}

		super.breakBlock(world, pos, state);
	}
}