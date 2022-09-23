package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.projectex.block.entity.AbstractLinkInvBlockEntity;
import dev.latvian.mods.projectex.client.ProjectEXClientEventHandler;
import dev.latvian.mods.projectex.menu.AbstractLinkMenu;
import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractLinkScreen<C extends AbstractLinkMenu<T>, T extends AbstractLinkInvBlockEntity> extends AbstractProjectEXScreen<C,T> {
    public AbstractLinkScreen(C menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        // TODO handle long overflow?
        long emc = Minecraft.getInstance().player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY)
                .map(p -> p.getEmc().longValue()).orElse(0L);

        font.draw(poseStack, menu.getBlockEntity().getOwnerName(), 8f, 6f, 0x404040);

        String s = EMCFormat.INSTANCE.format(emc);
        if (ProjectEXClientEventHandler.emcRate != 0D) {
            s += (ProjectEXClientEventHandler.emcRate > 0D ? (ChatFormatting.DARK_GREEN + "+") : (ChatFormatting.RED + "-")) + EMCFormat.INSTANCE.format(Math.abs(ProjectEXClientEventHandler.emcRate)) + "/s";
        }
        font.draw(poseStack, s, 8, getEMCLabelYPos(), 0x404040);
    }
}
