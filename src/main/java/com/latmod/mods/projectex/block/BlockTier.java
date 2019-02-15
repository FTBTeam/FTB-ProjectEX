package com.latmod.mods.projectex.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * @author LatvianModder
 */
public class BlockTier extends Block
{
	public static final PropertyEnum<EnumTier> TIER = PropertyEnum.create("tier", EnumTier.class);

	public BlockTier(Material m, MapColor c)
	{
		super(m, c);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TIER);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(TIER, EnumTier.byMeta(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TIER).metadata;
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(TIER).metadata;
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for (EnumTier tier : EnumTier.VALUES)
		{
			items.add(new ItemStack(this, 1, tier.metadata));
		}
	}
}