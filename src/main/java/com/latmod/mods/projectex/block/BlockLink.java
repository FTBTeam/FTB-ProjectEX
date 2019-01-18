package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.tile.TileLink;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockLink extends Block
{
	public BlockLink()
	{
		super(Material.ROCK, MapColor.BLACK);
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
		return new TileLink();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			TileEntity tileEntity = world.getTileEntity(pos);

			if (tileEntity instanceof TileLink)
			{
				if (player.getUniqueID().equals(((TileLink) tileEntity).owner))
				{
					player.openGui(ProjectEX.INSTANCE, 0, world, pos.getX(), pos.getY(), pos.getZ());
				}
				else
				{
					player.sendStatusMessage(new TextComponentString(((TileLink) tileEntity).name), true);
				}
			}
		}

		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		TileEntity tileEntity = world.getTileEntity(pos);

		if (tileEntity instanceof TileLink)
		{
			((TileLink) tileEntity).owner = placer.getUniqueID();
			((TileLink) tileEntity).name = placer.getName();
			tileEntity.markDirty();
		}
	}
}