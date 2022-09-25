/*
 * This file is part of pnc-repressurized.
 *
 *     pnc-repressurized is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     pnc-repressurized is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with pnc-repressurized.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.ftb.projectex.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public abstract class AbstractProjectEXCategory<T> implements IRecipeCategory<T> {
    private final RecipeType<T> type;
    private final Component localizedName;
    private final IDrawable background;
    private final IDrawable icon;

    protected AbstractProjectEXCategory(RecipeType<T> type, Component localizedName, IDrawable background, IDrawable icon) {
        this.type = type;
        this.localizedName = localizedName;
        this.background = background;
        this.icon = icon;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return type;
    }

    @Override
    public ResourceLocation getUid() {
        return type.getUid();
    }

    @Override
    public Class<? extends T> getRecipeClass() {
        return type.getRecipeClass();
    }

    static IGuiHelper guiHelper() {
        return JEIIntegration.jeiHelpers.getGuiHelper();
    }

    List<Component> positionalTooltip(double mouseX, double mouseY, BiPredicate<Double,Double> predicate, String translationKey, Object... args) {
        return predicate.test(mouseX, mouseY) ?
                Collections.singletonList(new TranslatableComponent(translationKey, args)) :
                Collections.emptyList();
    }
}
