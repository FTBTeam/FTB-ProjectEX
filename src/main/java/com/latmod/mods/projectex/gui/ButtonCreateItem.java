package com.latmod.mods.projectex.gui;

import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ButtonCreateItem extends GuiButton
{
	public final GuiStoneTable gui;
	public ItemStack type;

	public ButtonCreateItem(GuiStoneTable g, int id, int x, int y)
	{
		super(id, x, y, 16, 16, "");
		gui = g;
		type = ItemStack.EMPTY;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
		{
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.enableDepth();
			RenderHelper.enableGUIStandardItemLighting();
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			RenderItem renderItem = mc.getRenderItem();
			renderItem.renderItemAndEffectIntoGUI(mc.player, type, x, y);

			String count = "";

			int d = (int) Math.min(gui.table.playerData.getEmc() / (double) ProjectEAPI.getEMCProxy().getValue(type), type.getMaxStackSize());

			if (d > 0)
			{
				count = Integer.toString(d);
			}

			renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, type, x, y, count);
			RenderHelper.disableStandardItemLighting();

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