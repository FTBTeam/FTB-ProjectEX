package dev.latvian.mods.projectex.integration.jei;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.recipes.AlchemyTableRecipe;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeTypes {
    public static final RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE = register("alchemy_table", AlchemyTableRecipe.class);

    private static <T extends Recipe<?>> RecipeType<T> register(String name, Class<T> recipeClass) {
        return RecipeType.create(ProjectEX.MOD_ID, name, recipeClass);
    }
}
