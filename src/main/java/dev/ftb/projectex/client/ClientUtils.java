package dev.ftb.projectex.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
