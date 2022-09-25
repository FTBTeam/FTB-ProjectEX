package dev.ftb.projectex.block.entity;

import dev.ftb.projectex.menu.PersonalLinkMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PersonalLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
    public PersonalLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.PERSONAL_LINK.get(), blockPos, blockState, 18, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectex.personal_link");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player player) {
        return new PersonalLinkMenu(windowId, playerInv, getBlockPos());
    }
}
