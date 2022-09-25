package dev.latvian.mods.projectex.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FilterSlot extends SlotItemHandler {
    public FilterSlot(LinkOutputHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return false;
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return getItemHandler() instanceof LinkOutputHandler h ?
                h.getItemForDisplay(getSlotIndex()) :
                ItemHandlerHelper.copyStackWithSize(super.getItem(), 1);
    }
}
