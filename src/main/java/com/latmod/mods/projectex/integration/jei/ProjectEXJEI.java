package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.GuiLink;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

/**
 * @author LatvianModder
 */
@JEIPlugin
public class ProjectEXJEI implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		registry.addGhostIngredientHandler(GuiLink.class, new GuiLinkGhostIngredientHandler());
		registry.addAdvancedGuiHandlers(new GuiTableAdvancedGuiHandler());
		//registry.addRecipeClickArea(GuiArcaneTablet.class, 88, 32, 28, 23, VanillaRecipeCategoryUid.CRAFTING);
		//registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerArcaneTablet.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
		//registry.addRecipeCatalyst(new ItemStack(ProjectEXItems.ARCANE_TABLET), VanillaRecipeCategoryUid.CRAFTING);
	}
}