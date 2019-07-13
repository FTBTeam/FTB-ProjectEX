package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.item.ProjectEXItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class AlchemyTableCategory implements IRecipeCategory<AlchemyTableWrapper>
{
	public static final String UID = "projectex.alchemy_table";

	private final IDrawable background;
	private final IDrawable icon;

	public AlchemyTableCategory(IGuiHelper guiHelper)
	{
		background = guiHelper.drawableBuilder(new ResourceLocation(ProjectEX.MOD_ID + ":textures/gui/alchemy_table_jei.png"), 0, 0, 128, 18).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(ProjectEXItems.ALCHEMY_TABLE));
	}

	@Override
	public String getUid()
	{
		return UID;
	}

	@Override
	public String getTitle()
	{
		return I18n.format("tile.projectex.alchemy_table.name");
	}

	@Override
	public String getModName()
	{
		return ProjectEX.MOD_NAME;
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, AlchemyTableWrapper entry, IIngredients ingredients)
	{
		IGuiItemStackGroup stacks = layout.getItemStacks();
		stacks.init(0, true, 0, 0);
		stacks.init(1, false, 110, 0);
		stacks.set(ingredients);
	}
}