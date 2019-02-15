package com.latmod.mods.projectex.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author LatvianModder
 */
public class ItemMatter extends Item
{
	public static final String[] NAMES = {
			"purple",
			"blue",
			"cyan",
			"green",
			"yellow",
			"white",
			"fading"
	};

	public ItemMatter()
	{
		setHasSubtypes(true);
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return getTranslationKey() + "." + NAMES[stack.getMetadata()];
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			for (int i = 0; i < NAMES.length; i++)
			{
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
}