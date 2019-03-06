package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.net.MessageSendSearch;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.utils.Constants;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiStoneTable extends GuiContainer
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/stone_table.png");

	private static String staticSearch = "";
	private static int staticPage = 0;

	private GuiTextField searchField;
	private ContainerStoneTable table;

	public GuiStoneTable(ContainerStoneTable c)
	{
		super(c);
		table = c;
		ySize = 217;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		searchField = new GuiTextField(0, fontRenderer, guiLeft + 8, guiTop + 7, 160, 11);
		searchField.setTextColor(-1);
		searchField.setDisabledTextColour(-1);
		searchField.setEnableBackgroundDrawing(false);
		searchField.setMaxStringLength(35);
		searchField.setText(staticSearch);

		addButton(new GuiButton(1, guiLeft + 7, guiTop + 62, 12, 20, "<"));
		addButton(new GuiButton(2, guiLeft + 157, guiTop + 62, 12, 20, ">"));
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
		searchField.drawTextBox();
		staticSearch = searchField.getText();

		if (staticPage != table.page || !staticSearch.equals(table.search))
		{
			if (!staticSearch.equals(table.search))
			{
				staticPage = 0;
			}

			table.search = staticSearch;
			table.page = staticPage;
			table.updateCurrentItemList();
			ProjectEXNetHandler.NET.sendToServer(new MessageSendSearch(table));
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
		String s = Constants.EMC_FORMATTER.format(ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(mc.player.getUniqueID()).getEmc());
		fontRenderer.drawStringWithShadow(s, (xSize - fontRenderer.getStringWidth(s)) / 2F, 124F, 0xFFB5B5B5);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button.id == 1)
		{
			changePage(false);
		}
		else if (button.id == 2)
		{
			changePage(true);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 1 && mouseX >= searchField.x && mouseX < searchField.x + searchField.width && mouseY >= searchField.y && mouseY < searchField.y + searchField.height)
		{
			searchField.setText("");
		}
		else
		{
			searchField.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (searchField.textboxKeyTyped(typedChar, keyCode))
		{
			return;
		}

		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();

		if (i != 0)
		{
			changePage(i < 0);
		}
	}

	public void changePage(boolean next)
	{
		if (next)
		{
			if (staticPage < MathHelper.ceil(table.currentItems.size() / 8F))
			{
				staticPage++;
			}
		}
		else if (staticPage > 0)
		{
			staticPage--;
		}
	}

	@Override
	public List<String> getItemToolTip(ItemStack stack)
	{
		List<String> list = super.getItemToolTip(stack);

		if (!ProjectEXConfig.general.isStoneTableWhitelisted(stack) && ProjectEAPI.getEMCProxy().hasValue(stack))
		{
			list.add(TextFormatting.RED + I18n.format("gui.stone_table.cant_use"));
		}

		return list;
	}
}