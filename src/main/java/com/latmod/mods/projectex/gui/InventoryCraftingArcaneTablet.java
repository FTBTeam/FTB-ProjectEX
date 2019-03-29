package com.latmod.mods.projectex.gui;

import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author LatvianModder
 */
public class InventoryCraftingArcaneTablet extends InventoryCrafting
{
	private final ContainerArcaneTablet tablet;
	private final IItemHandlerModifiable items;

	public InventoryCraftingArcaneTablet(ContainerArcaneTablet container, int width, int height, IItemHandlerModifiable i)
	{
		super(container, width, height);
		tablet = container;
		items = i;
	}

	@Override
	public boolean isEmpty()
	{
		for (int i = 0; i < items.getSlots(); i++)
		{
			if (!items.getStackInSlot(i).isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index < 0 || index >= getSizeInventory() ? ItemStack.EMPTY : items.getStackInSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		items.setStackInSlot(index, stack);
		tablet.onCraftMatrixChanged(this);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (index < 0 || index >= getSizeInventory())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack0 = items.getStackInSlot(index);
		items.setStackInSlot(index, ItemStack.EMPTY);
		return stack0;
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (index < 0 || index >= getSizeInventory() || count <= 0 || items.getStackInSlot(index).isEmpty())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack = items.getStackInSlot(index).splitStack(count);

		if (!stack.isEmpty())
		{
			tablet.onCraftMatrixChanged(this);
		}

		return stack;
	}

	@Override
	public void clear()
	{
		for (int i = 0; i < items.getSlots(); i++)
		{
			items.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public void fillStackedContents(RecipeItemHelper helper)
	{
		for (int i = 0; i < items.getSlots(); i++)
		{
			helper.accountStack(items.getStackInSlot(i));
		}
	}

	@Override
	public void markDirty()
	{
		//tablet.markDirty();
		tablet.onCraftMatrixChanged(this);
		//TinkerTools.proxy.sendPacketToServerOnly(new InventoryCraftingSyncPacket());
	}
}
