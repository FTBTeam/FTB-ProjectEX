package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileLink;
import com.latmod.mods.projectex.tile.TileLinkMK2;
import com.latmod.mods.projectex.tile.TileLinkMK3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author LatvianModder
 */
public class ContainerLink extends Container
{
	public final EntityPlayer player;
	public final TileLink link;

	public ContainerLink(EntityPlayer ep, TileLink p)
	{
		player = ep;
		link = p;

		if (link instanceof TileLinkMK3)
		{
			addSlotToContainer(new SlotItemHandler(link, 0, 8, 17));

			for (int k = 0; k < 3; ++k)
			{
				for (int i1 = 0; i1 < 9; ++i1)
				{
					addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 162 + k * 18));
				}
			}

			for (int l = 0; l < 9; ++l)
			{
				addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 220));
			}
		}
		else if (link instanceof TileLinkMK2)
		{
			addSlotToContainer(new SlotItemHandler(link, 0, 35, 35));

			for (int k = 0; k < 3; ++k)
			{
				for (int i1 = 0; i1 < 9; ++i1)
				{
					addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
				}
			}

			for (int l = 0; l < 9; ++l)
			{
				addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
			}
		}
		else
		{
			for (int i = 0; i < 18; i++)
			{
				addSlotToContainer(new SlotItemHandler(link, i, 8 + (i % 6) * 18, 17 + (i / 6) * 18));
			}

			for (int k = 0; k < 3; ++k)
			{
				for (int i1 = 0; i1 < 9; ++i1)
				{
					addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
				}
			}

			for (int l = 0; l < 9; ++l)
			{
				addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 142));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (index < link.inputSlots.length)
			{
				if (!mergeItemStack(stack1, link.inputSlots.length, inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(stack1, 0, link.inputSlots.length, false))
			{
				return ItemStack.EMPTY;
			}

			if (stack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return stack;
	}

	@Override
	public boolean enchantItem(EntityPlayer player, int id)
	{
		if (!player.getUniqueID().equals(link.owner))
		{
			return false;
		}

		int index = id % link.outputSlots.length;

		if (player.inventory.getItemStack().isEmpty())
		{
			int type = id / link.outputSlots.length;

			if (type == 1)
			{
				link.outputSlots[index] = ItemStack.EMPTY;
				link.markDirty();
				return true;
			}

			if (!player.world.isRemote)
			{
				ItemStack stack = link.extractItem(link.inputSlots.length + index, type == 2 ? 1 : 64, false);

				if (!stack.isEmpty())
				{
					ItemHandlerHelper.giveItemToPlayer(player, stack);
				}
			}

			return true;
		}

		return link.setOutputStack(player, index, player.inventory.getItemStack());
	}
}