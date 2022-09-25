package dev.ftb.projectex.menu;

import dev.ftb.projectex.block.entity.CompressedRefinedLinkBlockEntity;
import dev.ftb.projectex.inventory.FilterSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class CompressedRefinedLinkMenu extends AbstractLinkMenu<CompressedRefinedLinkBlockEntity> {
    public CompressedRefinedLinkMenu(int windowId, Inventory playerInv, BlockPos pos) {
        super(ProjectEXMenuTypes.COMPRESSED_REFINED_LINK.get(), windowId, playerInv, pos);

        addSlot(new SlotItemHandler(getBlockEntity().getInputHandler(), 0, 8, 17));

        for (int i = 0; i < 54; i++) {
            addSlot(new FilterSlot(getBlockEntity().getOutputHandler(), i, 8 + (i % 9) * 18, 41 + (i / 9) * 18));
        }

        addPlayerSlots(playerInv, 8, 162);
    }

    public CompressedRefinedLinkMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(windowId, playerInv, buf.readBlockPos());
    }

    @Override
    protected Class<CompressedRefinedLinkBlockEntity> blockEntityClass() {
        return CompressedRefinedLinkBlockEntity.class;
    }
}
