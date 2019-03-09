package com.latmod.mods.projectex.item;

import com.latmod.mods.projectex.block.EnumMatter;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author LatvianModder
 */
public class ItemMatter extends Item
{
	public ItemMatter()
	{
		setHasSubtypes(true);
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return getTranslationKey() + "." + EnumMatter.byMeta(stack.getMetadata()).getName();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			for (EnumMatter matter : EnumMatter.VALUES)
			{
				items.add(new ItemStack(this, 1, matter.ordinal()));
			}
		}
	}
}