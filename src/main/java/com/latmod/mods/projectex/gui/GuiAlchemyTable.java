package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class GuiAlchemyTable extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/alchemy_table.png");

	public final ContainerAlchemyTable container;

	public GuiAlchemyTable(ContainerAlchemyTable c)
	{
		super(c);
		container = c;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (container.emc > 0)
		{
			drawTexturedModalRect(guiLeft + 77, guiTop + 34, 177, 17, (int) (container.emc / 255F * 24F), 18);
		}

		if (container.progress > 0)
		{
			drawTexturedModalRect(guiLeft + 78, guiTop + 35, 177, 0, (int) (container.progress / 255F * 22F), 16);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		//String s = this.tileFurnace.getDisplayName().getUnformattedText();
		//this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		//this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
}