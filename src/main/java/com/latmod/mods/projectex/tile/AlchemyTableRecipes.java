package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXUtils;
import moze_intel.projecte.gameObjs.ObjHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class AlchemyTableRecipes
{
	public static final AlchemyTableRecipes INSTANCE = new AlchemyTableRecipes();

	public final List<AlchemyTableRecipe> LIST = new ArrayList<>();

	public void add(ItemStack input, ItemStack output)
	{
		AlchemyTableRecipe recipe = new AlchemyTableRecipe();
		recipe.input = ProjectEXUtils.fixOutput(input);
		recipe.output = ProjectEXUtils.fixOutput(output);
		LIST.add(recipe);
	}

	public void addSteps(ItemStack... stacks)
	{
		for (int i = 1; i < stacks.length; i++)
		{
			add(stacks[i - 1], stacks[i]);
		}
	}

	public void addDefaultRecipes()
	{
		addSteps(
				new ItemStack(Items.COAL, 1, 1),
				new ItemStack(Items.COAL, 1, 0)
		);

		addSteps(
				new ItemStack(Items.REDSTONE),
				new ItemStack(Items.GUNPOWDER),
				new ItemStack(Items.GLOWSTONE_DUST),
				new ItemStack(Items.BLAZE_POWDER),
				new ItemStack(Items.BLAZE_ROD)
		);

		addSteps(
				new ItemStack(Items.DYE, 1, 4),
				new ItemStack(Items.PRISMARINE_SHARD),
				new ItemStack(Items.PRISMARINE_CRYSTALS)
		);

		addSteps(
				new ItemStack(ObjHandler.covalence, 1, 0),
				new ItemStack(ObjHandler.covalence, 1, 1),
				new ItemStack(ObjHandler.covalence, 1, 2)
		);

		addSteps(
				new ItemStack(Items.BEEF),
				new ItemStack(Items.ROTTEN_FLESH),
				new ItemStack(Items.LEATHER),
				new ItemStack(Items.SPIDER_EYE),
				new ItemStack(Items.BONE)
		);

		addSteps(
				new ItemStack(Items.WHEAT_SEEDS),
				new ItemStack(Items.MELON),
				new ItemStack(Items.APPLE),
				new ItemStack(Items.CARROT),
				new ItemStack(Items.BEETROOT),
				new ItemStack(Items.POTATO),
				new ItemStack(Blocks.PUMPKIN)
		);

		addSteps(
				new ItemStack(Items.COOKIE),
				new ItemStack(Items.BREAD),
				new ItemStack(Blocks.CAKE)
		);

		add(new ItemStack(Items.ENDER_EYE), new ItemStack(Items.CHORUS_FRUIT));
		add(new ItemStack(Blocks.CONCRETE, 1, 15), new ItemStack(Blocks.OBSIDIAN));
		add(new ItemStack(Items.LAVA_BUCKET), new ItemStack(Blocks.OBSIDIAN));
		add(new ItemStack(Items.STRING), new ItemStack(Items.FEATHER));
	}

	public boolean hasOutput(ItemStack input)
	{
		return getOutput(input) != null;
	}

	@Nullable
	public AlchemyTableRecipe getOutput(ItemStack input)
	{
		if (input.isEmpty())
		{
			return null;
		}

		Item item = input.getItem();
		int meta = !input.getHasSubtypes() && input.isItemStackDamageable() ? 0 : input.getMetadata();

		for (AlchemyTableRecipe recipe : LIST)
		{
			if (item == recipe.input.getItem() && meta == recipe.input.getMetadata())
			{
				return recipe;
			}
		}

		return null;
	}
}