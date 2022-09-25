package dev.ftb.projectex.integration.jei;

import com.google.common.collect.ImmutableList;
import dev.ftb.projectex.client.gui.AbstractProjectEXScreen;
import dev.ftb.projectex.inventory.FilterSlot;
import dev.ftb.projectex.network.NetworkHandler;
import dev.ftb.projectex.network.PacketJEIGhost;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EMCLinkJEI implements IGhostIngredientHandler<AbstractProjectEXScreen> {
    @Override
    public <I> List<Target<I>> getTargets(AbstractProjectEXScreen gui, I ingredient, boolean doStart) {
        ImmutableList.Builder<Target<I>> builder = ImmutableList.builder();
        if (ingredient instanceof ItemStack stack && ProjectEAPI.getEMCProxy().hasValue(stack)) {
            NonNullList<Slot> slots = gui.getMenu().slots;
            // indexed for loop needed to get raw container slot index
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if (slot instanceof FilterSlot filterSlot) {
                    //noinspection unchecked
                    builder.add((Target<I>) new ItemStackTarget(filterSlot, i, gui));
                }
            }
        }
        return builder.build();
    }

    @Override
    public void onComplete() {
    }

    private record ItemStackTarget(FilterSlot filterSlot, int rawSlot, AbstractProjectEXScreen gui) implements Target<ItemStack> {
        @Override
        public Rect2i getArea() {
            return new Rect2i(gui.getGuiLeft() + filterSlot.x, gui.getGuiTop() + filterSlot.y, 16, 16);
        }

        @Override
        public void accept(ItemStack ingredient) {
            filterSlot.set(ingredient);
            NetworkHandler.sendToServer(new PacketJEIGhost(rawSlot, ingredient));
        }
    }
}
