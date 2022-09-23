package dev.latvian.mods.projectex.network;

import dev.latvian.mods.projectex.recipes.RecipeCache;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Received on: CLIENT
 * Sent on a server reload to tell clients to clear any recipe cache they have
 */
public class PacketClearRecipeCache {
    public PacketClearRecipeCache() {
    }

    public PacketClearRecipeCache(@SuppressWarnings("unused") FriendlyByteBuf buffer) {
    }

    @SuppressWarnings("EmptyMethod")
    public void toBytes(@SuppressWarnings("unused") FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(RecipeCache::clearAll);
        ctx.get().setPacketHandled(true);
    }
}
