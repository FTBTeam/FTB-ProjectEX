package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.net.MessageSyncEMC;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author LatvianModder
 */
public class SlotCreateItem extends SlotItemHandler
{
	public final ContainerTableBase table;
	public ItemStack type = ItemStack.EMPTY;

	public SlotCreateItem(ContainerTableBase t, int index, int x, int y)
	{
		super(t, index, x, y);
		table = t;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack getStack()
	{
		return type;
	}

	@Override
	public void putStack(ItemStack stack)
	{
	}

	@Override
	public boolean canTakeStack(EntityPlayer player)
	{
		return !type.isEmpty();
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		if (amount <= 0 || type.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		double value = ProjectEAPI.getEMCProxy().getValue(type);

		if (value <= 0D)
		{
			return ItemStack.EMPTY;
		}

		double max = table.playerData.getEmc() / value;

		if (amount > 64 || amount > max)
		{
			amount = (int) Math.min(64D, Math.min(amount, max));
		}

		if (amount <= 0)
		{
			return ItemStack.EMPTY;
		}

		table.playerData.setEmc(table.playerData.getEmc() - value * amount);

		if (table.player instanceof EntityPlayerMP)
		{
			ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(table.playerData.getEmc()), (EntityPlayerMP) table.player);
		}

		table.updateCurrentItemList();
		return ItemHandlerHelper.copyStackWithSize(type, amount);
	}
}