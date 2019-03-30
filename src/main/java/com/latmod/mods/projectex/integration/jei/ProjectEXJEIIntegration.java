package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.GuiArcaneTablet;
import com.latmod.mods.projectex.gui.GuiLink;
import com.latmod.mods.projectex.item.ProjectEXItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
@JEIPlugin
public class ProjectEXJEIIntegration implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		registry.addGhostIngredientHandler(GuiLink.class, new GuiLinkGhostIngredientHandler());
		registry.addAdvancedGuiHandlers(new GuiTableAdvancedGuiHandler(), new GuiLinkAdvancedGuiHandler());
		registry.addRecipeClickArea(GuiArcaneTablet.class, -60, 75, 33, 17, VanillaRecipeCategoryUid.CRAFTING);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(new ArcaneTabletRecipeTransferHandler(), VanillaRecipeCategoryUid.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(ProjectEXItems.ARCANE_TABLET), VanillaRecipeCategoryUid.CRAFTING);
	}
}