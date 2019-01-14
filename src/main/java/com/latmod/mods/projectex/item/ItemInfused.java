package com.latmod.mods.projectex.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemInfused extends Item
{
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}