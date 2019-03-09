package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.item.ProjectEXItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public enum EnumMatter implements IStringSerializable, Supplier<ItemStack>
{
	MAGENTA("magenta"),
	PINK("pink"),
	PURPLE("purple"),
	VIOLET("violet"),
	BLUE("blue"),
	CYAN("cyan"),
	GREEN("green"),
	LIME("lime"),
	YELLOW("yellow"),
	ORANGE("orange"),
	WHITE("white"),
	FADING("fading");

	public static final EnumMatter[] VALUES = values();

	public static EnumMatter byMeta(int meta)
	{
		return meta < 0 || meta >= VALUES.length ? MAGENTA : VALUES[meta];
	}

	private final String name;

	EnumMatter(String n)
	{
		name = n;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ItemStack get()
	{
		return new ItemStack(ProjectEXItems.MATTER, 1, ordinal());
	}
}
