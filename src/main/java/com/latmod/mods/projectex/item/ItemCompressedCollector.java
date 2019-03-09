package com.latmod.mods.projectex.item;

import com.latmod.mods.projectex.block.EnumTier;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ItemCompressedCollector extends Item
{
	public ItemCompressedCollector()
	{
		setHasSubtypes(true);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return ProjectEXItems.COLLECTOR.getTranslationKey(stack);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (isInCreativeTab(tab))
		{
			for (EnumTier tier : EnumTier.VALUES)
			{
				items.add(new ItemStack(this, 1, tier.ordinal()));
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(I18n.format("tile.projectex.collector.compressed"));
	}
}