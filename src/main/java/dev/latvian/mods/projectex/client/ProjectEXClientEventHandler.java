package dev.latvian.mods.projectex.client;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.client.gui.EMCFormat;
import dev.latvian.mods.projectex.config.ConfigHelper;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, value = Dist.CLIENT)
public class ProjectEXClientEventHandler {
    private static long emcAmount;  // tracks current player EMC amount
    private static long lastEMC;
    private static int timer;
    private static final long[] emcsa = new long[5];

    public static long emcRate = 0L;  // rate of change of player EMC over last 5 seconds

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            // calculate change rate in player's personal EMC
            // TODO handle long overflow?
            emcAmount = Minecraft.getInstance().player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY)
                    .map(p -> p.getEmc().longValue()).orElse(0L);
            if (timer == 1) {
                System.arraycopy(emcsa, 1, emcsa, 0, emcsa.length - 1);
                emcsa[emcsa.length - 1] = emcAmount - lastEMC;
                lastEMC = emcAmount;

                emcRate = 0L;
                for (long d : emcsa) {
                    emcRate += d;
                }
                emcRate /= emcsa.length;

                timer = -1; //Should be -1 as this leaves the if it would increment. Toys0125
            }

            timer = (timer + 1) % 20;
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        emcAmount = 0L;
        timer = 0;
        emcRate = 0L;
        Arrays.fill(emcsa, 0L);
    }

    @SubscribeEvent
    public static void addInfoText(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().player != null &&
                (!ConfigHelper.client().general.onlyShowEMCWhenHoldingModItem.get() || holdingValidItem(Minecraft.getInstance().player)))
        {
            EMCOverlayPosition oPos = ConfigHelper.client().general.screenPosition.get();
            if (oPos != EMCOverlayPosition.DISABLED && emcAmount > 0D) {
                String s = EMCFormat.INSTANCE.format(emcAmount);
                if (emcRate != 0L) {
                    s += (emcRate > 0L ? (ChatFormatting.GREEN + "+") : (ChatFormatting.RED + "-")) + EMCFormat.INSTANCE.format(Math.abs(emcRate)) + "/s";
                }

                (oPos == EMCOverlayPosition.TOP_LEFT ? event.getLeft() : event.getRight()).add("EMC: " + s);
            }
        }
    }

    private static boolean holdingValidItem(Player player) {
        return holdingValidItem(player.getMainHandItem()) || holdingValidItem(player.getOffhandItem());
    }
    private static boolean holdingValidItem(ItemStack stack) {
        String namespace = stack.getItem().getRegistryName().getNamespace();
        return namespace.equals(ProjectEX.MOD_ID) || namespace.equals(ProjectEAPI.PROJECTE_MODID);
    }
}
