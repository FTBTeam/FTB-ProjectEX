package com.latmod.mods.projectex.item;

import moze_intel.projecte.api.item.IItemEmc;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemFinalStar extends Item implements IItemEmc
{
	public ItemFinalStar()
	{
		setMaxStackSize(1);
	}

	@Override
	public double addEmc(ItemStack stack, double toAdd)
	{
		return 0D;
	}

	@Override
	public double extractEmc(ItemStack stack, double toRemove)
	{
		return toRemove;
	}

	@Override
	public double getStoredEmc(ItemStack stack)
	{
		return 1000000000000D;
	}

	@Override
	public double getMaximumEmc(ItemStack stack)
	{
		return 1000000000000D;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.EPIC;
	}
}