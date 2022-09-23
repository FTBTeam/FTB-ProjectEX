package dev.latvian.mods.projectex.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public abstract class AbstractProjectEXMenu<T extends BlockEntity> extends AbstractContainerMenu {
    private final T blockEntity;
    int playerSlotsStart;

    public AbstractProjectEXMenu(MenuType<?> type, int windowId, Inventory invPlayer, BlockPos tilePos) {
        super(type, windowId);

        if (tilePos != null) {
            BlockEntity be0 = invPlayer.player.level.getBlockEntity(tilePos);
            if (be0 != null && blockEntityClass().isAssignableFrom(be0.getClass())) {
                //noinspection unchecked
                blockEntity = (T) be0;  // should be safe: we have done an isAssignableFrom()
            } else {
                blockEntity = null;
            }
        } else {
            blockEntity = null;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !blockEntity.isRemoved() && player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < 64.0;
    }

    @Nonnull
    protected abstract Class<T> blockEntityClass();

    public T getBlockEntity() {
        return blockEntity;
    }

    protected final void addPlayerSlots(Inventory playerInv, int xOffset, int yOffset) {
        playerSlotsStart = slots.size();

        // Add the player's inventory slots to the container
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new Slot(playerInv, col + row * 9 + 9, xOffset + col * 18, yOffset + row * 18));
            }
        }

        // Add the player's action bar slots to the container
        for (int idx = 0; idx < 9; ++idx) {
            addSlot(new Slot(playerInv, idx, xOffset + idx * 18, yOffset + 58));
        }
    }

}
