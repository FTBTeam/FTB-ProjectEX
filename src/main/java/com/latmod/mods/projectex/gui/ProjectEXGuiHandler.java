package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
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
public class ProjectEXGuiHandler implements IGuiHandler
{
	public static final int LINK = 1;
	public static final int STONE_TABLE = 2;
	public static final int TABLET_MK2 = 3;

	public static void open(EntityPlayer player, int id, int x, int y, int z)
	{
		player.openGui(ProjectEX.INSTANCE, id, player.world, x, y, z);
	}

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == LINK)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileLink)
			{
				return new ContainerLink(player, (TileLink) tileEntity);
			}
		}
		else if (id == STONE_TABLE)
		{
			return new ContainerStoneTable(player);
		}
		else if (id == TABLET_MK2)
		{
			return new ContainerTabletMK2(player);
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
		if (id == LINK)
		{
			TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

			if (tileEntity instanceof TileLink)
			{
				return new GuiLink(new ContainerLink(player, (TileLink) tileEntity));
			}
		}
		else if (id == STONE_TABLE)
		{
			return new GuiStoneTable(new ContainerStoneTable(player));
		}
		else if (id == TABLET_MK2)
		{
			return new GuiTabletMK2(new ContainerTabletMK2(player));
		}

		return null;
	}
}