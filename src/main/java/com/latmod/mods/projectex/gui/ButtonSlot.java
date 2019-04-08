package com.latmod.mods.projectex.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
 */
public class ButtonSlot extends GuiButton
{
	public final ContainerTableBase table;

	public ButtonSlot(ContainerTableBase t, int id, int x, int y)
	{
		super(id, x, y, 16, 16, "");
		table = t;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
		{
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

			if (hovered)
			{
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.colorMask(true, true, true, false);
				drawGradientRect(x, y, x + width, y + height, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}
	}

	@Override
	public void playPressSound(SoundHandler soundHandler)
	{
	}
}