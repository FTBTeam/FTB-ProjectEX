package dev.latvian.mods.projectex.inventory;

import dev.latvian.mods.projectex.block.entity.AbstractLinkInvBlockEntity;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LinkInputHandler extends BaseItemStackHandler<AbstractLinkInvBlockEntity> {
    public LinkInputHandler(AbstractLinkInvBlockEntity blockEntity, int inputSize) {
        super(blockEntity, inputSize);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return ProjectEAPI.getEMCProxy().hasValue(stack);
    }
}
