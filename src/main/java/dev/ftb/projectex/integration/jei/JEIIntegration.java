package dev.ftb.projectex.integration.jei;

import com.google.common.collect.ImmutableList;
import dev.ftb.projectex.ProjectEX;
import dev.ftb.projectex.block.ProjectEXBlocks;
import dev.ftb.projectex.client.gui.AbstractProjectEXScreen;
import dev.ftb.projectex.client.gui.AlchemyTableScreen;
import dev.ftb.projectex.recipes.ProjectEXRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(ProjectEX.MOD_ID, "default");

    static IJeiHelpers jeiHelpers;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        jeiHelpers = registry.getJeiHelpers();
        registry.addRecipeCategories(new AlchemyTableCategory());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        addRecipeType(registration, ProjectEXRecipeTypes.ALCHEMY_TABLE.get(), RecipeTypes.ALCHEMY_TABLE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ProjectEXBlocks.ALCHEMY_TABLE.get()), RecipeTypes.ALCHEMY_TABLE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlchemyTableScreen.class, 78, 35, 23, 14, RecipeTypes.ALCHEMY_TABLE);

        registration.addGhostIngredientHandler(AbstractProjectEXScreen.class, new EMCLinkJEI());
    }

    private <C extends Container, T extends Recipe<C>> void addRecipeType(IRecipeRegistration registration, RecipeType<T> type, mezz.jei.api.recipe.RecipeType<T> recipeType) {
        List<T> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(type);
        registration.addRecipes(recipeType, ImmutableList.copyOf(recipes));
    }
}
