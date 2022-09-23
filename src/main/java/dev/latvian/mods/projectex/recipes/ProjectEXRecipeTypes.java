package dev.latvian.mods.projectex.recipes;

import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ProjectEXRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, ProjectEX.MOD_ID);

    public static final RegistryObject<RecipeType<AlchemyTableRecipe>> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", AlchemyTableRecipeType::new);

    public static class AlchemyTableRecipeType implements RecipeType<AlchemyTableRecipe> {
    }
}
