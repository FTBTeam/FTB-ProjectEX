package com.latmod.mods.projectex.gui;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ContainerTableBase extends Container implements IItemHandler
{
	public final EntityPlayer player;
	public final IKnowledgeProvider playerData;
	public String search = "";
	public int page = 0;
	public final List<ItemStack> validItems;
	public final List<ItemStack> currentItems;

	public ContainerTableBase(EntityPlayer p)
	{
		player = p;
		playerData = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(player.getUniqueID());
		validItems = new ArrayList<>();
		currentItems = new ArrayList<>();
		updateValidItemList();
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getSlots()
	{
		return 1 + getCreateSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return true;
	}

	public int getCreateSlots()
	{
		return 0;
	}

	public final void updateCurrentItemList()
	{
		currentItems.clear();

		String s = TextFormatting.getTextWithoutFormattingCodes(search.trim()).toLowerCase();

		double d = playerData.getEmc();

		for (ItemStack stack : validItems)
		{
			if (d >= ProjectEAPI.getEMCProxy().getValue(stack) && TextFormatting.getTextWithoutFormattingCodes(stack.getDisplayName().trim()).toLowerCase().contains(s))
			{
				currentItems.add(stack);
			}
		}

		updateCurrentSlots();
		detectAndSendChanges();
	}

	public void updateCurrentSlots()
	{
		for (int i = 0; i < getCreateSlots(); i++)
		{
			Slot slot = getSlot(i + 1);

			if (slot instanceof SlotCreateItem)
			{
				int index = page * 8 + i;
				((SlotCreateItem) slot).type = index >= 0 && index < currentItems.size() ? currentItems.get(index) : ItemStack.EMPTY;
			}
		}
	}

	public void updateValidItemList()
	{
		validItems.clear();

		for (ItemStack stack : playerData.getKnowledge())
		{
			if (isItemValid(stack))
			{
				validItems.add(stack);
			}
		}
	}
}