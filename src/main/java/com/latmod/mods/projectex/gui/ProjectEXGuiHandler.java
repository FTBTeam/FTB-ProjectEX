package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileLink;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum ProjectEXGuiHandler implements IGuiHandler
{
	INSTANCE;

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof TileLink)
		{
			return new ContainerLink(player, (TileLink) tileEntity);
		}

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		return getClientGuiElement0(id, player, world, x, y, z);
	}

	@Nullable
	private Object getClientGuiElement0(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity instanceof TileLink)
		{
			return new GuiLink(new ContainerLink(player, (TileLink) tileEntity));
		}

		return null;
	}
}