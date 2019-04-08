package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.client.EnumSearchType;
import com.latmod.mods.projectex.client.ProjectEXClientConfig;
import com.latmod.mods.projectex.integration.PersonalEMC;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.io.IOException;

/**
 * @author LatvianModder
 */
public class GuiArcaneTablet extends GuiTableBase
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/arcane_tablet.png");

	public GuiArcaneTablet(ContainerArcaneTablet c)
	{
		super(c);
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

		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 20));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 105, guiTop + 26));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 55, guiTop + 26));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 123, guiTop + 44));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 37, guiTop + 44));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 128, guiTop + 68));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 32, guiTop + 68));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 123, guiTop + 92));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 37, guiTop + 92));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 105, guiTop + 110));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 55, guiTop + 110));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 116));

		addButton(new ButtonLearnItem(table, 4, guiLeft + 8, guiTop + 115));
		addButton(new ButtonUnlearnItem(table, 5, guiLeft + 152, guiTop + 115));

		addButton(new ButtonSmall(6, guiLeft - 71, guiTop + 16, I18n.format("gui.arcane_tablet.rotate")));
		addButton(new ButtonSmall(7, guiLeft - 71, guiTop + 26, I18n.format("gui.arcane_tablet.balance")));
		addButton(new ButtonSmall(8, guiLeft - 71, guiTop + 36, I18n.format("projectex.general.search_type") + ": " + TextFormatting.GRAY + I18n.format(ProjectEXClientConfig.general.search_type.translationKey)));
		addButton(new ButtonSmall(9, guiLeft - 71, guiTop + 61, I18n.format("gui.arcane_tablet.clear")));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == 6)
		{
			handleButton(isShiftKeyDown() ? 42 : 41);
		}
		else if (button.id == 7)
		{
			handleButton(isShiftKeyDown() ? 44 : 43);
		}
		else if (button.id == 8)
		{
			ProjectEXClientConfig.general.search_type = EnumSearchType.VALUES[(ProjectEXClientConfig.general.search_type.ordinal() + 1) % EnumSearchType.VALUES.length];
			button.displayString = I18n.format("projectex.general.search_type") + ": " + TextFormatting.GRAY + I18n.format(ProjectEXClientConfig.general.search_type.translationKey);
		}
		else if (button.id == 9)
		{
			handleButton(40);
		}
		else
		{
			super.actionPerformed(button);
		}
	}

	private void handleButton(int id)
	{
		if (table.enchantItem(mc.player, id))
		{
			mc.playerController.sendEnchantPacket(table.windowId, id);
		}
	}

	@Override
	public ResourceLocation getTexture()
	{
		return TEXTURE;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawTexturedModalRect(guiLeft - 75, guiTop + 10, 180, 19, 76, 89);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String s = EMCFormat.INSTANCE.format(PersonalEMC.get(mc.player).getEmc());
		fontRenderer.drawStringWithShadow(s, (xSize - fontRenderer.getStringWidth(s)) / 2F, -9F, 0xFFB5B5B5);
	}

	@Override
	protected boolean hasClickedOutside(int mouseX, int mouseY, int x, int y)
	{
		if (mouseX >= x - 75 && mouseY >= y + 10 && mouseX < x + 1 && mouseY < y + 99)
		{
			return false;
		}

		return super.hasClickedOutside(mouseX, mouseY, x, y);
	}
}