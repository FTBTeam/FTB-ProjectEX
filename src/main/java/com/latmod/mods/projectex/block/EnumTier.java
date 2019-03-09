package com.latmod.mods.projectex.block;

import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.item.ProjectEXItems;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public enum EnumTier implements IStringSerializable
{
	BASIC("basic", () -> new ItemStack(Blocks.DIAMOND_BLOCK), ProjectEXConfig.tiers.basic),
	DARK("dark", () -> new ItemStack(ObjHandler.matter, 1, 0), ProjectEXConfig.tiers.dark),
	RED("red", () -> new ItemStack(ObjHandler.matter, 1, 1), ProjectEXConfig.tiers.red),
	MAGENTA("magenta", EnumMatter.MAGENTA, ProjectEXConfig.tiers.magenta),
	PINK("pink", EnumMatter.PINK, ProjectEXConfig.tiers.pink),
	PURPLE("purple", EnumMatter.PURPLE, ProjectEXConfig.tiers.purple),
	VIOLET("violet", EnumMatter.VIOLET, ProjectEXConfig.tiers.violet),
	BLUE("blue", EnumMatter.BLUE, ProjectEXConfig.tiers.blue),
	CYAN("cyan", EnumMatter.CYAN, ProjectEXConfig.tiers.cyan),
	GREEN("green", EnumMatter.GREEN, ProjectEXConfig.tiers.green),
	LIME("lime", EnumMatter.LIME, ProjectEXConfig.tiers.lime),
	YELLOW("yellow", EnumMatter.YELLOW, ProjectEXConfig.tiers.yellow),
	ORANGE("orange", EnumMatter.ORANGE, ProjectEXConfig.tiers.orange),
	WHITE("white", EnumMatter.WHITE, ProjectEXConfig.tiers.white),
	FADING("fading", EnumMatter.FADING, ProjectEXConfig.tiers.fading),
	FINAL("final", () -> new ItemStack(ProjectEXItems.FINAL_STAR_SHARD), ProjectEXConfig.tiers.final_tier);

	public static final EnumTier[] VALUES = values();

	public static EnumTier byMeta(int meta)
	{
		return meta < 0 || meta >= VALUES.length ? BASIC : VALUES[meta];
	}

	private final String name;
	public final Supplier<ItemStack> matter;
	public ProjectEXConfig.BlockTier properties;

	EnumTier(String n, Supplier<ItemStack> ma, ProjectEXConfig.BlockTier b)
	{
		name = n;
		matter = ma;
		properties = b;
	}

	@Override
	public String getName()
	{
		return name;
	}
}