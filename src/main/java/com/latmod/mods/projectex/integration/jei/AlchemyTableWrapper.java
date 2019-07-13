package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.EMCFormat;
import com.latmod.mods.projectex.tile.AlchemyTableRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

/**
 * @author LatvianModder
 */
public class AlchemyTableWrapper implements IRecipeWrapper
{
	public final AlchemyTableRecipe recipe;

	public AlchemyTableWrapper(AlchemyTableRecipe r)
	{
		recipe = r;
	}

	@Override
	public void getIngredients(IIngredients ingredients)
	{
		ingredients.setInput(VanillaTypes.ITEM, recipe.input);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
	}

	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
	{
		long emc = recipe.getTotalCost();
		String s = EMCFormat.INSTANCE.format(emc) + " EMC";
		mc.fontRenderer.drawString(s, (recipeWidth - mc.fontRenderer.getStringWidth(s)) / 2, 5, 0xFF222222);
	}
}