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
	public static final long[] MAGNUM_STAR_EMC = new long[] {204800000L, 819200000L, 3276800000L, 13107200000L, 52428800000L, 209715200000L};

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
		return MAGNUM_STAR_EMC[tier.ordinal()];
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.RARE;
	}
}