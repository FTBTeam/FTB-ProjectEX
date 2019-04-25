package com.latmod.mods.projectex.item;

import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.gameObjs.items.KleinStar;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

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
		return getStoredEmc(stack) > 0D;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		long emc = getStoredEmc(stack);
		return emc == 0D ? 1D : MathHelper.clamp(1D - emc / (double) getMaximumEmc(stack), 0D, 1D);
	}

	@Override
	public long addEmc(ItemStack stack, long toAdd)
	{
		long add = Math.min(getMaximumEmc(stack) - getStoredEmc(stack), toAdd);
		ItemPE.addEmcToStack(stack, add);
		return add;
	}

	@Override
	public long extractEmc(ItemStack stack, long toRemove)
	{
		long sub = Math.min(getStoredEmc(stack), toRemove);
		ItemPE.removeEmc(stack, sub);
		return sub;
	}

	@Override
	public long getStoredEmc(ItemStack stack)
	{
		return ItemPE.getEmc(stack);
	}

	@Override
	public long getMaximumEmc(ItemStack stack)
	{
		return STAR_EMC[tier.ordinal()];
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.UNCOMMON;
	}
}