package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.entity.AbstractEMCBlockEntity;
import dev.latvian.mods.projectex.menu.AbstractProjectEXMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractProjectEXScreen<C extends AbstractProjectEXMenu<T>, T extends AbstractEMCBlockEntity> extends AbstractContainerScreen<C> {
    public static final ResourceLocation TEXTURE_MK1 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/personal_link.png");
    public static final ResourceLocation TEXTURE_MK2 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/refined_link.png");
    public static final ResourceLocation TEXTURE_MK3 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/compressed_refined_link.png");

    public AbstractProjectEXScreen(C menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    final void bindGuiTexture() {
        ResourceLocation guiTexture = getGuiTexture();
        if (guiTexture != null) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.setShaderTexture(0, guiTexture);
            RenderSystem.enableTexture();
        }
    }

    protected abstract ResourceLocation getGuiTexture();

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        bindGuiTexture();

        int xStart = (width - imageWidth) / 2;
        int yStart = (height - imageHeight) / 2;
        blit(poseStack, xStart, yStart, 0, 0, imageWidth, imageHeight);
    }

    protected int getEMCLabelYPos() {
        return 73;
    }
}
