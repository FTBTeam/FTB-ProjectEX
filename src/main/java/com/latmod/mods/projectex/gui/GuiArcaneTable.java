package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author LatvianModder
 */
public class GuiArcaneTable extends GuiTableBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/arcane_tablet.png");

	public GuiArcaneTable(ContainerArcaneTablet c)
	{
		super(c);
		ySize = 217;
	}

	@Override
	public Rectangle getTextBox()
	{
		return new Rectangle(guiLeft + 8, guiTop + 7, 160, 11);
	}

	@Override
	public void addButtons()
	{
		addButton(new GuiButton(1, guiLeft + 7, guiTop + 62, 12, 20, "<"));
		addButton(new GuiButton(2, guiLeft + 157, guiTop + 62, 12, 20, ">"));
		addButton(new ButtonBurnItem(3, guiLeft + 80, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 24));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 111, guiTop + 33));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 49, guiTop + 33));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 120, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 40, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 111, guiTop + 95));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 49, guiTop + 95));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 104));
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = EMCFormat.INSTANCE.format(ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(mc.player.getUniqueID()).getEmc());
		fontRenderer.drawStringWithShadow(s, (xSize - fontRenderer.getStringWidth(s)) / 2F, 124F, 0xFFB5B5B5);
	}
}