package dev.ftb.projectex;

import dev.ftb.projectex.network.NetworkHandler;
import dev.ftb.projectex.network.PacketEMCSync;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
public class EMCSync {
    private static final Map<UUID, BigInteger> EMC_MAP = new Object2ObjectOpenHashMap<>();

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer serverPlayer && event.phase == TickEvent.Phase.START) {
            BigInteger prev = EMC_MAP.get(event.player.getUUID());
            serverPlayer.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                BigInteger emc = provider.getEmc();
                if (prev == null || !prev.equals(emc)) {
                    EMC_MAP.put(event.player.getUUID(), emc);
                    NetworkHandler.sendToPlayer(serverPlayer, new PacketEMCSync(emc));
                }
            });

        }
    }
}
