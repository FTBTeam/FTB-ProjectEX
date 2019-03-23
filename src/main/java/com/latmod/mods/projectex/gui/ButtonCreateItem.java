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
	public final ContainerTableBase table;
	public ItemStack type;

	public ButtonCreateItem(ContainerTableBase t, int id, int x, int y)
	{
		super(id, x, y, 16, 16, "");
		table = t;
		type = ItemStack.EMPTY;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
		{
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;

			if (!type.isEmpty())
			{
				GlStateManager.color(1F, 1F, 1F, 1F);
				GlStateManager.enableDepth();
				RenderHelper.enableGUIStandardItemLighting();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
				RenderItem renderItem = mc.getRenderItem();
				renderItem.renderItemAndEffectIntoGUI(mc.player, type, x, y);
				renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, type, x, y, null);
				RenderHelper.disableStandardItemLighting();

				double emc = ProjectEAPI.getEMCProxy().getValue(type);
				String s;

				double d = table.playerData.getEmc() / emc;

				if (d >= 1D)
				{
					s = EMCFormat.INSTANCE_IGNORE_SHIFT.format(d);
				}
				else if (d >= 0.1D)
				{
					s = Double.toString(((int) (d * 10D)) / 10D);
				}
				else
				{
					s = "";
				}

				GlStateManager.pushMatrix();
				GlStateManager.translate(x + 17, y + 12, 0);
				GlStateManager.scale(0.5F, 0.5F, 1F);
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				GlStateManager.disableBlend();
				mc.fontRenderer.drawStringWithShadow(s, -mc.fontRenderer.getStringWidth(s), 0, 16777215);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.enableBlend();
				GlStateManager.popMatrix();
			}

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