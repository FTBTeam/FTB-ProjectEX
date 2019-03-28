package com.latmod.mods.projectex.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class ButtonArrow extends GuiButton
{
	private final GuiTableBase gui;
	private final int textureX, textureY;

	public ButtonArrow(GuiTableBase g, int id, int x, int y, int tx, int ty)
	{
		super(id, x, y, 18, 18, "");
		gui = g;
		textureX = tx;
		textureY = ty;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		FontRenderer fontrenderer = mc.fontRenderer;
		mc.getTextureManager().bindTexture(gui.getTexture());
		GlStateManager.color(1F, 1F, 1F, 1F);
		hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		if (hovered)
		{
			drawTexturedModalRect(x, y, textureX, textureY, width, height);
		}

		mouseDragged(mc, mouseX, mouseY);
	}

}