package dev.ftb.projectex.menu;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class OutputSlot extends SlotItemHandler {
    public OutputSlot(ItemStackHandler inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
