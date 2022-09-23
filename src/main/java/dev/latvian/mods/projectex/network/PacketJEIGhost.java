package dev.latvian.mods.projectex.network;

import dev.latvian.mods.projectex.inventory.FilterSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Received on: SERVER
 * Sent by client when ghost items are dragged from JEI into a filter slot
 */
public class PacketJEIGhost {
    private final int slotIndex;
    private final ItemStack stack;

    public PacketJEIGhost(int slotIndex, ItemStack stack) {
        this.slotIndex = slotIndex;
        this.stack = stack;
    }

    public PacketJEIGhost(FriendlyByteBuf buf) {
        this.slotIndex = buf.readVarInt();
        this.stack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(slotIndex);
        buf.writeItem(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && slotIndex >= 0 && slotIndex < player.containerMenu.slots.size()
                    && player.containerMenu.getSlot(slotIndex) instanceof FilterSlot f) {
                f.set(stack);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
