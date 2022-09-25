package dev.ftb.projectex.datagen.recipes;

import com.google.gson.JsonObject;
import dev.ftb.projectex.ProjectEX;
import dev.ftb.projectex.recipes.AlchemyTableRecipe;
import net.minecraft.resources.ResourceLocation;

public class AlchemyTableRecipeBuilder extends ProjectEXRecipeBuilder<AlchemyTableRecipeBuilder> {
    private static final ResourceLocation ID = new ResourceLocation(ProjectEX.MOD_ID, "alchemy_table");
    private final AlchemyTableRecipe recipe;

    public AlchemyTableRecipeBuilder(AlchemyTableRecipe recipe) {
        super(ID);
        this.recipe = recipe;
    }

    @Override
    protected ProjectEXRecipeBuilder<AlchemyTableRecipeBuilder>.RecipeResult getResult(ResourceLocation id) {
        return new RecipeResult(id) {
            @Override
            public void serializeRecipeData(JsonObject json) {
                recipe.toJson(json);
            }
        };
    }
}
