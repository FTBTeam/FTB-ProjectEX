package com.latmod.mods.projectex.item;

import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.gameObjs.items.KleinStar;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemMagnumStar extends Item implements IItemEmc
{
	public static final long[] STAR_EMC = new long[12];

	static
	{
		long emc = 204800000L;

		for (int i = 0; i < STAR_EMC.length; i++)
		{
			STAR_EMC[i] = emc;
			emc *= 4L;
		}
	}

	public final KleinStar.EnumKleinTier tier;

	public ItemMagnumStar(KleinStar.EnumKleinTier t)
	{
		tier = t;
		setMaxStackSize(1);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		double emc = getStoredEmc(stack);
		return emc == 0D ? 1D : 1D - emc / getMaximumEmc(stack);
	}

	@Override
	public double addEmc(ItemStack stack, double toAdd)
	{
		double add = Math.min(getMaximumEmc(stack) - getStoredEmc(stack), toAdd);
		ItemPE.addEmcToStack(stack, add);
		return add;
	}

	@Override
	public double extractEmc(ItemStack stack, double toRemove)
	{
		double sub = Math.min(getStoredEmc(stack), toRemove);
		ItemPE.removeEmc(stack, sub);
		return sub;
	}

	@Override
	public double getStoredEmc(ItemStack stack)
	{
		return ItemPE.getEmc(stack);
	}

	@Override
	public double getMaximumEmc(ItemStack stack)
	{
		return STAR_EMC[tier.ordinal()];
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
}