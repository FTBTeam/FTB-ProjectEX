package dev.ftb.projectex.block.entity;

import dev.ftb.projectex.menu.RefinedLinkMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RefinedLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
    public RefinedLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.REFINED_LINK.get(), blockPos, blockState, 1, 9);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectex.refined_link");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new RefinedLinkMenu(windowId, playerInv, getBlockPos());
    }

    @Override
    protected boolean learnItems() {
        return true;
    }
}
