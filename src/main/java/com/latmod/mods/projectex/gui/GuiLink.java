package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.client.ProjectEXClientEventHandler;
import com.latmod.mods.projectex.tile.TileLinkMK2;
import com.latmod.mods.projectex.tile.TileLinkMK3;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiLink extends GuiContainer
{
	private static final ResourceLocation TEXTURE_MK1 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/personal_link.png");
	private static final ResourceLocation TEXTURE_MK2 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/refined_link.png");
	private static final ResourceLocation TEXTURE_MK3 = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/compressed_refined_link.png");

	public class ButtonFilter extends GuiButton
	{
		public ButtonFilter(int id, int x, int y)
		{
			super(id, x, y, 16, 16, "");
		}

		@Override
		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if (visible)
			{
				GlStateManager.color(1F, 1F, 1F, 1F);
				hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				if (hovered)
				{
					//int left, int top, int right, int bottom
					drawRect(x, y, x + width, y + height, -2130706433);
				}

				if (!container.link.outputSlots[id].isEmpty())
				{
					zLevel = 100F;
					itemRender.zLevel = 100F;
					GlStateManager.enableDepth();
					RenderHelper.enableGUIStandardItemLighting();
					itemRender.renderItemAndEffectIntoGUI(mc.player, container.link.outputSlots[id], x, y);
					itemRender.renderItemOverlayIntoGUI(fontRenderer, container.link.outputSlots[id], x, y, "");
					itemRender.zLevel = 0F;
					zLevel = 0F;
				}

				mouseDragged(mc, mouseX, mouseY);
			}
		}
	}

	public final ContainerLink container;

	public GuiLink(ContainerLink c)
	{
		super(c);
		container = c;

		if (container.link instanceof TileLinkMK3)
		{
			ySize = 244;
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();

		if (container.link instanceof TileLinkMK3)
		{
			for (int i = 0; i < 54; i++)
			{
				buttonList.add(new ButtonFilter(i, guiLeft + 8 + (i % 9) * 18, guiTop + 41 + (i / 9) * 18));
			}
		}
		else if (container.link instanceof TileLinkMK2)
		{
			for (int i = 0; i < 9; i++)
			{
				buttonList.add(new ButtonFilter(i, guiLeft + 89 + (i % 3) * 18, guiTop + 17 + (i / 3) * 18));
			}
		}
		else
		{
			buttonList.add(new ButtonFilter(0, guiLeft + 152, guiTop + 35));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button instanceof ButtonFilter)
		{
			int id = button.id + (isCtrlKeyDown() ? 2 : isShiftKeyDown() ? 1 : 0) * container.link.outputSlots.length;

			if (container.enchantItem(container.player, id))
			{
				mc.playerController.sendEnchantPacket(container.windowId, id);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void renderHoveredToolTip(int x, int y)
	{
		super.renderHoveredToolTip(x, y);

		for (GuiButton button : buttonList)
		{
			if (button.isMouseOver() && button instanceof ButtonFilter)
			{
				if (!container.link.outputSlots[button.id].isEmpty())
				{
					renderToolTip(container.link.outputSlots[button.id], x, y);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);

		if (container.link instanceof TileLinkMK3)
		{
			mc.getTextureManager().bindTexture(TEXTURE_MK3);
		}
		else if (container.link instanceof TileLinkMK2)
		{
			mc.getTextureManager().bindTexture(TEXTURE_MK2);
		}
		else
		{
			mc.getTextureManager().bindTexture(TEXTURE_MK1);
		}

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		double emc = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(container.link.owner).getEmc();

		fontRenderer.drawString(container.link.name, 8, 6, 4210752);

		String s = EMCFormat.INSTANCE.format(emc);

		if (ProjectEXClientEventHandler.emcs != 0D)
		{
			s += (ProjectEXClientEventHandler.emcs > 0D ? (TextFormatting.DARK_GREEN + "+") : (TextFormatting.RED + "-")) + EMCFormat.INSTANCE.format(Math.abs(ProjectEXClientEventHandler.emcs)) + "/s";
		}

		if (container.link instanceof TileLinkMK3)
		{
			fontRenderer.drawString(s, 8, 151, 4210752);
		}
		else
		{
			fontRenderer.drawString(s, 8, 73, 4210752);
		}
	}

	public List<GuiButton> getButtons()
	{
		return buttonList;
	}
}