package dev.latvian.mods.projectex.offline;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.client.ClientUtils;
import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.network.PacketEMCSync;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jline.utils.Log;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PersonalEMC extends SavedData {
    private static final String DATA_KEY = ProjectEX.MOD_ID + ":personal_emc";

    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1L);

    private static ServerLevel overworld;

    private static final PersonalEMC CLIENT_INSTANCE = new PersonalEMC();

    // Required because of a Forge bug https://github.com/MinecraftForge/MinecraftForge/issues/5696
    private final HashSet<UUID> LOGGED_IN_PLAYERS = new HashSet<>();
    private final Map<UUID, OfflineKnowledgeProvider> OFFLINE_MAP = new HashMap<>();
    private final Map<UUID, BigInteger> EMC_MAP = new Object2ObjectOpenHashMap<>();

    private static ServerLevel getOverworld() {
        if (overworld == null) {
            overworld = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
            if (overworld == null) {
                throw new IllegalStateException("Overworld not initialized!");
            }
        }
        return overworld;
    }

    public static PersonalEMC getInstance(Level level) {
        return level.isClientSide() ? CLIENT_INSTANCE : getOverworld().getDataStorage().computeIfAbsent(PersonalEMC::load, PersonalEMC::new, DATA_KEY);
    }

    private PersonalEMC() {
    }

    private static PersonalEMC load(CompoundTag compoundTag) {
        PersonalEMC res = new PersonalEMC();

        CompoundTag providers = compoundTag.getCompound("providers");
        for (String idStr : providers.getAllKeys()) {
            try {
                UUID id = UUID.fromString(idStr);
                res.OFFLINE_MAP.put(id, OfflineKnowledgeProvider.fromNBT(id, providers.getCompound(idStr)));
            } catch (IllegalArgumentException e) {
                Log.warn("Invalid UUID: " + idStr);
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag providerTag = new CompoundTag();
        OFFLINE_MAP.forEach((id, provider) -> providerTag.put(id.toString(), provider.serializeNBT()));
        compoundTag.put("providers", providerTag);
        return compoundTag;
    }

    private void clear() {
        LOGGED_IN_PLAYERS.clear();
        OFFLINE_MAP.clear();
        EMC_MAP.clear();
    }

    //-------------------------------
    // Convenience methods below here

    public static IKnowledgeProvider getKnowledge(Player player) {
        return player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).orElseThrow(RuntimeException::new);
    }

    public static IKnowledgeProvider getKnowledge(Level world, UUID id) {
        if (id == null || id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L) {
            return null;
        }

        if (world.isClientSide()) {
            Player player = ClientUtils.getClientPlayer();
            return player == null ? null : getKnowledge(player);
        }

        Player player = world.getServer().getPlayerList().getPlayer(id);
        if (player != null) {
            return getKnowledge(player);
        }

        // the player's offline; use the offline cache instead
        PersonalEMC manager = getInstance(world);
        OfflineKnowledgeProvider provider = manager.OFFLINE_MAP.get(id);
        if (provider == null) {
            // must never have seen this player before; the offline map is world-saved data
            provider = new OfflineKnowledgeProvider(id);
            manager.OFFLINE_MAP.put(provider.playerId, provider);
            manager.setDirty();
        }
        return provider;
    }

    public static void add(IKnowledgeProvider knowledgeProvider, BigInteger add) {
        knowledgeProvider.setEmc(knowledgeProvider.getEmc().add(add));
    }

    public static void remove(IKnowledgeProvider knowledgeProvider, BigInteger remove) {
        knowledgeProvider.setEmc(knowledgeProvider.getEmc().subtract(remove));
    }

    @Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
    public static class Listener {
        @SubscribeEvent
        public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getPlayer() instanceof ServerPlayer sp) {
                PersonalEMC manager = getInstance(sp.getLevel());
                if (manager.LOGGED_IN_PLAYERS.add(sp.getUUID())) {
                    OfflineKnowledgeProvider knowledgeProvider = manager.OFFLINE_MAP.get(sp.getUUID());
                    if (knowledgeProvider != null) {
                        // copy the offline knowledge/EMC for the player back to their live data
                        IKnowledgeProvider provider = getKnowledge(sp);
                        OfflineKnowledgeProvider.copy(knowledgeProvider, provider);
                        manager.OFFLINE_MAP.remove(knowledgeProvider.playerId);
                        provider.sync(sp);
                        manager.setDirty();
                    }
                }
            }
        }

        @SubscribeEvent
        public static void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            if (event.getPlayer() instanceof ServerPlayer sp) {
                PersonalEMC manager = getInstance(sp.getLevel());
                if (manager.LOGGED_IN_PLAYERS.remove(sp.getUUID())) {
                    // copy the player's current knowledge/EMC into the offline map
                    IKnowledgeProvider provider = getKnowledge(sp);
                    OfflineKnowledgeProvider knowledgeProvider = new OfflineKnowledgeProvider(sp.getUUID());
                    OfflineKnowledgeProvider.copy(provider, knowledgeProvider);
                    manager.OFFLINE_MAP.put(knowledgeProvider.playerId, knowledgeProvider);
                    manager.EMC_MAP.remove(knowledgeProvider.playerId);
                    manager.setDirty();
                }
            }
        }

        @SubscribeEvent
        public static void playerTick(TickEvent.PlayerTickEvent event) {
            if (event.player instanceof ServerPlayer sp) {
                PersonalEMC manager = getInstance(sp.getLevel());
                BigInteger prev = manager.EMC_MAP.get(sp.getUUID());
                BigInteger emc = getKnowledge(sp).getEmc();

                if (prev == null || !prev.equals(emc)) {
                    manager.EMC_MAP.put(sp.getUUID(), emc);
                    NetworkHandler.sendToPlayer(sp, new PacketEMCSync(emc));
                }
            }
        }

        @SubscribeEvent
        public static void worldUnloaded(WorldEvent.Unload event) {
            if (event.getWorld() instanceof ServerLevel serverLevel && serverLevel.dimension().equals(Level.OVERWORLD)) {
                getInstance(serverLevel).clear();
            }
        }
    }
}