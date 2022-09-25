package dev.ftb.projectex.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.projectex.client.gui.EMCFormat;
import dev.ftb.projectex.item.ProjectEXItems;
import dev.ftb.projectex.recipes.AlchemyTableRecipe;
import dev.ftb.projectex.util.ProjectEXUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class AlchemyTableCategory extends AbstractProjectEXCategory<AlchemyTableRecipe> {
    protected AlchemyTableCategory() {
        super(RecipeTypes.ALCHEMY_TABLE,
                new TranslatableComponent("block.projectex.alchemy_table"),
                guiHelper().drawableBuilder(ProjectEXUtils.rl("textures/gui/alchemy_table_jei.png"), 0, 0, 128, 18)
                        .setTextureSize(128, 64).build(),
                guiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ProjectEXItems.ALCHEMY_TABLE.get()))
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AlchemyTableRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 1)
                .addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(AlchemyTableRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        recipeSlotsView.getSlotViews(RecipeIngredientRole.INPUT).get(0).getDisplayedIngredient(VanillaTypes.ITEM_STACK).ifPresent(stack -> {
            long emc = recipe.getTotalCost(stack);
            String s = EMCFormat.INSTANCE.format(emc) + " EMC";
            font.draw(poseStack, s, (128 - font.width(s)) / 2f, 5f, 0xFF404040);
        });
    }
}
