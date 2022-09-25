package dev.latvian.mods.projectex.network;

import dev.latvian.mods.projectex.client.ClientUtils;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.math.BigInteger;
import java.util.function.Supplier;

/**
 * Received on: CLIENT
 * Sent by server when player's personal EMC has changed (EMC is displayed on-screen)
 */
public class PacketEMCSync {
    private final BigInteger emc;

    public PacketEMCSync(BigInteger emc) {
        this.emc = emc;
    }

    public PacketEMCSync(FriendlyByteBuf buf) {
        this.emc = new BigInteger(buf.readUtf());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(emc.toString());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) {
                Player player = ClientUtils.getClientPlayer();
                if (player != null) {
                    player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).ifPresent(provider -> provider.setEmc(emc));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
