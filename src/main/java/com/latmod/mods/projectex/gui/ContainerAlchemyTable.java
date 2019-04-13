package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.tile.TileAlchemyTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author LatvianModder
 */
public class ContainerAlchemyTable extends Container
{
	public final EntityPlayer player;
	public final TileAlchemyTable table;

	public int progress = -1;
	public int emc = -1;

	public ContainerAlchemyTable(EntityPlayer ep, TileAlchemyTable p)
	{
		player = ep;
		table = p;

		addSlotToContainer(new SlotItemHandler(table.items, 0, 44, 35));
		addSlotToContainer(new SlotItemHandler(table.items, 1, 116, 35));

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

			if (index < 2)
			{
				if (!mergeItemStack(stack, 2, inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(stack, 0, 2, false))
			{
				return ItemStack.EMPTY;
			}

			if (stack.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			return oldStack;
		}

		return ItemStack.EMPTY;
	}

	public void sendData(IContainerListener listener)
	{
		listener.sendWindowProperty(this, 0, Math.min(255, table.totalProgress <= 0 ? 0 : (table.progress * 255 / table.totalProgress)));
		listener.sendWindowProperty(this, 1, Math.min(255, table.totalCost <= 0D ? 0 : (int) (table.storedEMC * 255L / table.totalCost)));
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		sendData(listener);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		if (!table.getWorld().isRemote)
		{
			for (IContainerListener listener : listeners)
			{
				sendData(listener);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		if (id == 0)
		{
			progress = data & 255;
		}
		else if (id == 1)
		{
			emc = data & 255;
		}
		else
		{
			super.updateProgressBar(id, data);
		}
	}
}