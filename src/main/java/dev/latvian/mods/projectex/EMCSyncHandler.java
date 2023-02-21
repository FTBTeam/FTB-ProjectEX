package dev.latvian.mods.projectex;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Track players who need their EMC value sync'd, and do it in a controlled fashion once a second instead of every time
 * an EMC link block does anything. Avoids excessive sync'ing and performance issues with many such blocks in the world.
 */
public enum EMCSyncHandler {
    INSTANCE;

    private final Set<UUID> toSync = new HashSet<>();  // UUID of players who need EMC sync'd
    private int counter = 0;

    public void needsSync(ServerPlayer player) {
        toSync.add(player.getUUID());
    }

    public void needsSync(UUID playerId) {
        toSync.add(playerId);
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            counter++;
            if (counter == 20) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                toSync.stream()
                        .map(id -> server.getPlayerList().getPlayer(id))
                        .filter(player -> player != null && player.isAlive())
                        .forEach(player -> player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY)
                                .ifPresent(provider -> provider.syncEmc(player)));
                toSync.clear();
                counter = 0;
            }
        }
    }
}
