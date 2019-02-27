package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

/**
 * @author LatvianModder
 */
public class GuiLink extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/link.png");
	public static final DecimalFormat EMC_FORMATTER = new DecimalFormat("#,###");

	private class ButtonFilter extends GuiButton
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

				if (!container.link.output.isEmpty())
				{
					zLevel = 100F;
					itemRender.zLevel = 100F;
					GlStateManager.enableDepth();
					RenderHelper.enableGUIStandardItemLighting();
					itemRender.renderItemAndEffectIntoGUI(mc.player, container.link.output, x, y);
					itemRender.renderItemOverlayIntoGUI(fontRenderer, container.link.output, x, y, "");
					itemRender.zLevel = 0F;
					zLevel = 0F;
				}

				mouseDragged(mc, mouseX, mouseY);
			}
		}
	}

	public final ContainerLink container;
	public double lastEMC;
	public long lastUpdate;
	public double emcs;

	public GuiLink(ContainerLink c)
	{
		super(c);
		container = c;
		lastEMC = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(container.link.owner).getEmc();
		lastUpdate = System.currentTimeMillis();
		emcs = 0D;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.add(new ButtonFilter(0, guiLeft + 152, guiTop + 35));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button instanceof ButtonFilter)
		{
			if (container.enchantItem(container.player, isCtrlKeyDown() ? 2 : isShiftKeyDown() ? 1 : 0))
			{
				mc.playerController.sendEnchantPacket(container.windowId, isCtrlKeyDown() ? 2 : isShiftKeyDown() ? 1 : 0);
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
				if (!container.link.output.isEmpty())
				{
					renderToolTip(container.link.output, x, y);
				}
			}
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		long now = System.currentTimeMillis();
		double emc = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(container.link.owner).getEmc();

		if ((now - lastUpdate) >= 2000L)
		{
			emcs = (emc - lastEMC) / 2D;
			lastEMC = emc;
			lastUpdate = now;
		}

		fontRenderer.drawString(container.link.name, 8, 6, 4210752);

		String s = EMC_FORMATTER.format(emc);

		if (emcs != 0D)
		{
			s += (emcs > 0D ? (TextFormatting.DARK_GREEN + "+") : (TextFormatting.RED + "-")) + EMC_FORMATTER.format(Math.abs(emcs)) + "/s";
		}

		fontRenderer.drawString(s, 8, 73, 4210752);
	}
}