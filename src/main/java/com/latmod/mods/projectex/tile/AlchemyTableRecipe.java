package com.latmod.mods.projectex.tile;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class AlchemyTableRecipe
{
	public ItemStack input = ItemStack.EMPTY;
	public ItemStack output = ItemStack.EMPTY;
	public long emcOverride = 0L;
	public int progressOverride = 0;

	public long getTotalCost()
	{
		if (emcOverride > 0L)
		{
			return emcOverride;
		}

		return Math.max(64L, (ProjectEAPI.getEMCProxy().getValue(input) + ProjectEAPI.getEMCProxy().getValue(output)) * 3L);
	}

	public int getTotalProgress()
	{
		return progressOverride > 0 ? progressOverride : 200;
	}
}