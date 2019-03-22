package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.integration.PersonalEMC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

/**
 * @author LatvianModder
 */
public class GuiArcaneTablet extends GuiTableBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/arcane_tablet.png");

	public GuiArcaneTablet(ContainerArcaneTablet c)
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
		String s = EMCFormat.INSTANCE.format(PersonalEMC.get(mc.player).getEmc());
		fontRenderer.drawStringWithShadow(s, (xSize - fontRenderer.getStringWidth(s)) / 2F, 124F, 0xFFB5B5B5);
		fontRenderer.drawStringWithShadow("Work in Progress! Will have crafting table and favorite item slots later!", -guiLeft + 4F, -guiTop + 4F, -1);
	}
}