package dev.ftb.projectex.block.entity;

import dev.ftb.projectex.menu.CompressedRefinedLinkMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CompressedRefinedLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
    public CompressedRefinedLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.COMPRESSED_REFINED_LINK.get(), blockPos, blockState, 1, 54);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectex.compressed_refined_link");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new CompressedRefinedLinkMenu(windowId, playerInv, getBlockPos());
    }

    @Override
    protected boolean learnItems() {
        return true;
    }
}
