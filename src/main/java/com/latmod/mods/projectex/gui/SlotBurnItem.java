package com.latmod.mods.projectex.gui;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.item.IItemEmc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;

/**
 * @author LatvianModder
 */
public class SlotBurnItem extends SlotItemHandler
{
	private final ContainerTableBase table;

	public SlotBurnItem(ContainerTableBase t, int index, int x, int y)
	{
		super(t, index, x, y);
		table = t;
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return !(stack.getItem() instanceof IItemEmc) && table.isItemValid(stack) && ProjectEAPI.getEMCProxy().hasValue(stack);
	}

	@Override
	public ItemStack getStack()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public void putStack(ItemStack stack)
	{
		table.playerData.setEmc(table.playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());

		if (!table.playerData.hasKnowledge(stack) && table.playerData.addKnowledge(ItemHandlerHelper.copyStackWithSize(stack, 1)))
		{
			table.updateValidItemList();

			if (table.player instanceof EntityPlayerMP)
			{
				table.playerData.sync((EntityPlayerMP) table.player);
			}
		}

		table.updateCurrentItemList();
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 64;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount)
	{
		return ItemStack.EMPTY;
	}
}