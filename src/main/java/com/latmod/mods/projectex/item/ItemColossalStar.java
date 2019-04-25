package com.latmod.mods.projectex.item;

import moze_intel.projecte.gameObjs.items.KleinStar;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemColossalStar extends ItemMagnumStar
{
	public ItemColossalStar(KleinStar.EnumKleinTier t)
	{
		super(t);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public long getMaximumEmc(ItemStack stack)
	{
		return STAR_EMC[tier.ordinal() + 6];
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}