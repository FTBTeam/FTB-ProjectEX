package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.tile.TileLinkMK2;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public class BlockLinkMK2 extends BlockLink
{
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileLinkMK2();
	}
}