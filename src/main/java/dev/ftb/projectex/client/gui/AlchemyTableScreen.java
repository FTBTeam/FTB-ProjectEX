package dev.ftb.projectex.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.projectex.ProjectEX;
import dev.ftb.projectex.block.entity.AlchemyTableBlockEntity;
import dev.ftb.projectex.menu.AlchemyTableMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AlchemyTableScreen extends AbstractProjectEXScreen<AlchemyTableMenu, AlchemyTableBlockEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/alchemy_table.png");

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected ResourceLocation getGuiTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);

        if (menu.getBlockEntity().costDisplay > 0) {
            blit(poseStack,leftPos + 77, topPos + 34, 177, 17, Math.max(1, (int) (menu.getBlockEntity().costDisplay / 255F * 24F)), 18);
        }
        if (menu.getBlockEntity().progressDisplay > 0) {
            blit(poseStack,leftPos + 78, topPos + 35, 177, 0, Math.max(1, (int) (menu.getBlockEntity().progressDisplay / 255F * 22F)), 16);
        }
    }
}
