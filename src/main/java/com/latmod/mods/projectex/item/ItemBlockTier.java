package com.latmod.mods.projectex.item;

import com.latmod.mods.projectex.block.EnumTier;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ItemBlockTier extends ItemBlock
{
	public ItemBlockTier(Block block)
	{
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}

	@Override
	public String getTranslationKey(ItemStack stack)
	{
		return getTranslationKey() + "." + EnumTier.byMeta(stack.getMetadata()).getName();
	}
}