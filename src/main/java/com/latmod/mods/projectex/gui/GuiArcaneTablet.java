package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.integration.PersonalEMC;
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
		addButton(new ButtonArrow(this, 1, guiLeft + 7, guiTop + 20, 196, 0));
		addButton(new ButtonArrow(this, 2, guiLeft + 151, guiTop + 20, 215, 0));
		addButton(new ButtonBurnItem(table, 3, guiLeft + 80, guiTop + 68));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 28));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 110, guiTop + 38));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 50, guiTop + 38));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 120, guiTop + 68));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 40, guiTop + 68));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 110, guiTop + 98));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 50, guiTop + 98));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 108));
		addButton(new ButtonLearnItem(table, 4, guiLeft + 8, guiTop + 115));
		addButton(new ButtonUnlearnItem(table, 5, guiLeft + 152, guiTop + 115));
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = "WIP! " + EMCFormat.INSTANCE.format(PersonalEMC.get(mc.player).getEmc());
		fontRenderer.drawStringWithShadow(s, (xSize - fontRenderer.getStringWidth(s)) / 2F, -9F, 0xFFB5B5B5);
	}
}