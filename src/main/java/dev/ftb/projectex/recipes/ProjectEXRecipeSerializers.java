package dev.ftb.projectex.recipes;

import dev.ftb.projectex.ProjectEX;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProjectEXRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectEX.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AlchemyTableRecipe>> ALCHEMY_TABLE
            = REGISTRY.register("alchemy_table", () -> new AlchemyTableRecipe.Serializer<>(AlchemyTableRecipe::new));
}
