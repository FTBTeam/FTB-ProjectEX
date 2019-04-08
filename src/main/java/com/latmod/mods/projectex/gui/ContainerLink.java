package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEXUtils;
import com.latmod.mods.projectex.integration.PersonalEMC;
import com.latmod.mods.projectex.tile.TileLink;
import com.latmod.mods.projectex.tile.TileLinkMK2;
import com.latmod.mods.projectex.tile.TileLinkMK3;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.config.ProjectEConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			ItemStack oldStack = stack.copy();

			long value = ProjectEAPI.getEMCProxy().getValue(stack);

			if (value > 0L)
			{
				PersonalEMC.get(player).addKnowledge(ProjectEXUtils.fixOutput(stack));
				addItemToOutput(ProjectEXUtils.fixOutput(stack));
				link.addEMC += (double) stack.getCount() * (double) value * ProjectEConfig.difficulty.covalenceLoss;
				link.markDirty();
			}
			else
			{
				return ItemStack.EMPTY;
			}

			slot.putStack(ItemStack.EMPTY);
			return oldStack;
		}

		return ItemStack.EMPTY;
	}

	private void addItemToOutput(ItemStack stack)
	{
		for (int i = 0; i < link.outputSlots.length; i++)
		{
			if (link.outputSlots[i].getItem() == stack.getItem() && link.outputSlots[i].getMetadata() == stack.getMetadata())
			{
				return;
			}
		}

		for (int i = 0; i < link.outputSlots.length; i++)
		{
			if (link.outputSlots[i].isEmpty())
			{
				link.outputSlots[i] = stack;
				return;
			}
		}
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
					player.inventory.placeItemBackInInventory(player.world, stack);
				}
			}

			return true;
		}

		return link.setOutputStack(player, index, player.inventory.getItemStack(), true);
	}
}