package dev.latvian.mods.projectex.inventory;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

/**
 * An item handler which has access to the block entity which holds it. Automatically marks the BE as dirty if the
 * contents change.
 *
 * @param <T> block entity type
 */
public class BaseItemStackHandler<T extends BlockEntity> extends ItemStackHandler {
    protected final T owningBlockEntity;

    public BaseItemStackHandler(T owningBlockEntity, int size) {
        super(size);
        this.owningBlockEntity = owningBlockEntity;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        owningBlockEntity.setChanged();
    }
}
