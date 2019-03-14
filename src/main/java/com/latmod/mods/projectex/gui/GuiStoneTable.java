package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.net.MessageCreateItemButton;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiStoneTable extends GuiContainer implements ContainerTableBase.KnowledgeUpdate
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/stone_table.png");

	private static String staticSearch = "";
	private static int staticPage = 0;

	public ContainerStoneTable table;
	private GuiTextField searchField;

	public final List<ItemStack> validItems;
	public final List<ButtonCreateItem> itemButtons;

	public GuiStoneTable(ContainerStoneTable c)
	{
		super(c);
		table = c;
		table.knowledgeUpdate = this;
		ySize = 217;
		validItems = new ArrayList<>();
		itemButtons = new ArrayList<>();
		updateValidItemList();
	}

	private String trim(String s)
	{
		return TextFormatting.getTextWithoutFormattingCodes(s.trim()).toLowerCase();
	}

	public void updateValidItemList()
	{
		validItems.clear();
		String s = trim(staticSearch);

		for (ItemStack stack : table.playerData.getKnowledge())
		{
			if (table.isItemValid(stack) && (s.isEmpty() || trim(stack.getDisplayName()).contains(s)))
			{
				validItems.add(stack);
			}
		}

		Collections.reverse(validItems);
		updateCurrentItemList();
	}

	public void updateCurrentItemList()
	{
		for (int i = 0; i < itemButtons.size(); i++)
		{
			int index = i + staticPage * itemButtons.size();

			if (index >= 0 && index < validItems.size())
			{
				itemButtons.get(i).type = validItems.get(index);
			}
			else
			{
				itemButtons.get(i).type = ItemStack.EMPTY;
			}
		}
	}

	@Override
	public void initGui()
	{
		itemButtons.clear();
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
		addButton(new ButtonBurnItem(3, guiLeft + 80, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 24));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 111, guiTop + 33));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 49, guiTop + 33));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 120, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 40, guiTop + 64));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 111, guiTop + 95));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 49, guiTop + 95));
		addButton(new ButtonCreateItem(table, 3, guiLeft + 80, guiTop + 104));
		updateValidItemList();
	}

	@Override
	protected <T extends GuiButton> T addButton(T button)
	{
		if (button instanceof ButtonCreateItem)
		{
			itemButtons.add((ButtonCreateItem) button);
		}

		return super.addButton(button);
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

		for (ButtonCreateItem button : itemButtons)
		{
			if (button.isMouseOver() && !button.type.isEmpty())
			{
				renderToolTip(button.type, mouseX, mouseY);
			}
		}

		if (!staticSearch.equals(searchField.getText()))
		{
			staticSearch = searchField.getText();
			staticPage = 0;
			updateValidItemList();
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

		if (button instanceof ButtonBurnItem)
		{
			clickGuiSlot(ItemStack.EMPTY, isShiftKeyDown() ? ContainerTableBase.UNLEARN : ContainerTableBase.BURN);
		}
		else if (button instanceof ButtonCreateItem)
		{
			clickGuiSlot(((ButtonCreateItem) button).type, isShiftKeyDown() ? ContainerTableBase.TAKE_STACK : ContainerTableBase.TAKE_ONE);
		}
		else if (button.id == 1)
		{
			changePage(false);
		}
		else if (button.id == 2)
		{
			changePage(true);
		}
	}

	private void clickGuiSlot(ItemStack stack, int mode)
	{
		if (table.clickGuiSlot(stack, mode))
		{
			ProjectEXNetHandler.NET.sendToServer(new MessageCreateItemButton(stack, mode));
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
			if (staticPage < MathHelper.ceil(validItems.size() / (float) itemButtons.size()) - 1)
			{
				staticPage++;
				updateCurrentItemList();
			}
		}
		else if (staticPage > 0)
		{
			staticPage--;
			updateCurrentItemList();
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

	@Override
	public void updateKnowledge()
	{
		staticPage = 0;
		staticSearch = "";
		updateValidItemList();
	}
}