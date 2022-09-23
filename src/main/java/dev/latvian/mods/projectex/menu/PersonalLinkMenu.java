package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.block.entity.PersonalLinkBlockEntity;
import dev.latvian.mods.projectex.inventory.FilterSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

public class PersonalLinkMenu extends AbstractLinkMenu<PersonalLinkBlockEntity> {
    public PersonalLinkMenu(int windowId, Inventory playerInv, BlockPos pos) {
        super(ProjectEXMenuTypes.PERSONAL_LINK.get(), windowId, playerInv, pos);

        for (int i = 0; i < 18; i++) {
            addSlot(new SlotItemHandler(getBlockEntity().getInputHandler(), i, 8 + (i % 6) * 18, 17 + (i / 6) * 18));
        }
        addSlot(new FilterSlot(getBlockEntity().getOutputHandler(), 0, 152, 35));

        addPlayerSlots(playerInv, 8, 84);
    }

    public PersonalLinkMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(windowId, playerInv, buf.readBlockPos());
    }

    @Override
    protected Class<PersonalLinkBlockEntity> blockEntityClass() {
        return PersonalLinkBlockEntity.class;
    }

}
