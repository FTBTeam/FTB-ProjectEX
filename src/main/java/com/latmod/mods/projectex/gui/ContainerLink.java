package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileLink;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.event.PlayerAttemptCondenserSetEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
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

			if (index < 18)
			{
				if (!mergeItemStack(stack1, 18, inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(stack1, 0, 18, false))
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

		if (player.inventory.getItemStack().isEmpty())
		{
			if (id == 1)
			{
				link.output = ItemStack.EMPTY;
				link.markDirty();
				return true;
			}

			if (!player.world.isRemote)
			{
				ItemStack stack = link.extractItem(18, id == 2 ? 1 : 64, false);

				if (!stack.isEmpty())
				{
					ItemHandlerHelper.giveItemToPlayer(player, stack);
				}
			}

			return true;
		}
		else if (ProjectEAPI.getEMCProxy().getValue(player.inventory.getItemStack()) > 0L)
		{
			ItemStack stack = ItemHandlerHelper.copyStackWithSize(player.inventory.getItemStack(), 1);

			if (!MinecraftForge.EVENT_BUS.post(new PlayerAttemptCondenserSetEvent(player, stack)))
			{
				link.output = stack;
				link.markDirty();
			}

			return true;
		}

		return false;
	}
}