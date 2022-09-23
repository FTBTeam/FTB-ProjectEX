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

package dev.latvian.mods.projectex.recipes;

import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.network.PacketClearRecipeCache;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * LRU recipe cache for quick lookup of recipes based on inventory contents.
 * Currently used for vanilla crafting and smelting recipes, and Alchemy Table recipes.
 */
public class RecipeCache<T extends RecipeType<R>, R extends Recipe<C>, C extends Container> {
    public static final RecipeCache<RecipeType<CraftingRecipe>, CraftingRecipe, CraftingContainer> CRAFTING
            = new RecipeCache<>(RecipeType.CRAFTING, true);
    public static final RecipeCache<RecipeType<SmeltingRecipe>, SmeltingRecipe, Container> SMELTING
            = new RecipeCache<>(RecipeType.SMELTING, false);
    public static final RecipeCache<RecipeType<AlchemyTableRecipe>, AlchemyTableRecipe, Container> ALCHEMY
            = new RecipeCache<>(ProjectEXRecipeTypes.ALCHEMY_TABLE.get(), false);

    private static final int MAX_CACHE_SIZE = 1024;
    private static CacheReloadListener cacheReloadListener;

    private final T type;
    private final boolean nbtSignificant;
    private final Int2ObjectLinkedOpenHashMap<Optional<R>> recipeCache = new Int2ObjectLinkedOpenHashMap<>(MAX_CACHE_SIZE, 0.25f);

    private RecipeCache(T type, boolean nbtSignificant) {
        this.type = type;
        this.nbtSignificant = nbtSignificant;
    }

    public static void clearAll() {
        CRAFTING.clear();
        SMELTING.clear();
        ALCHEMY.clear();
    }

    public static PreparableReloadListener getCacheReloadListener() {
        if (cacheReloadListener == null) {
            cacheReloadListener = new CacheReloadListener();
        }
        return cacheReloadListener;
    }

    public Optional<R> getCachedRecipe(Level world, C inv) {
        int key = makeKey(inv);
        if (recipeCache.containsKey(key)) {
            return recipeCache.getAndMoveToFirst(key);
        } else {
            Optional<R> newRecipe = world.getRecipeManager().getRecipeFor(type, inv, world);
            if (recipeCache.size() == MAX_CACHE_SIZE) {
                recipeCache.removeLast();
            }
            recipeCache.put(key, newRecipe);
            return newRecipe;
        }
    }

    private void clear() {
        recipeCache.clear();
    }

    private int makeKey(C inv) {
        List<Integer> c = new ArrayList<>();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty()) {
                c.add(i);
                c.add(stack.getItem().hashCode());
                if (nbtSignificant) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null) c.add(tag.hashCode());
                }
            }
        }
        return c.hashCode();
    }

    private static class CacheReloadListener implements PreparableReloadListener {
        @Override
        public CompletableFuture<Void> reload(PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return CompletableFuture.runAsync(() -> {
                clearAll();
                if (ServerLifecycleHooks.getCurrentServer() != null) {
                    NetworkHandler.sendToAll(new PacketClearRecipeCache());
                }
            }, gameExecutor).thenCompose(stage::wait);
        }
    }
}
