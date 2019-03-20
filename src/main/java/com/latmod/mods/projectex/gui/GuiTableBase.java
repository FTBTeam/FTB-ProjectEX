package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.net.MessageCreateItemButton;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class GuiTableBase extends GuiContainer implements ContainerTableBase.KnowledgeUpdate
{
	public static String staticSearch = "";
	public static int staticPage = 0;

	public ContainerTableBase table;
	public GuiTextField searchField;

	public final List<ItemStack> validItems;
	public final List<ButtonCreateItem> itemButtons;

	public GuiTableBase(ContainerTableBase c)
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
		boolean mod = s.startsWith("@");

		if (mod)
		{
			s = s.substring(1);
		}

		for (ItemStack stack : table.playerData.getKnowledge())
		{
			if (table.isItemValid(stack) && (s.isEmpty() || mod ? stack.getItem().getRegistryName().getNamespace().startsWith(s) : trim(stack.getDisplayName()).contains(s)))
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

	public abstract Rectangle getTextBox();

	public abstract void addButtons();

	public abstract ResourceLocation getTexture();

	@Override
	public void initGui()
	{
		itemButtons.clear();
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		Rectangle tb = getTextBox();
		searchField = new GuiTextField(0, fontRenderer, tb.x, tb.y, tb.width, tb.height);
		searchField.setTextColor(-1);
		searchField.setDisabledTextColour(-1);
		searchField.setEnableBackgroundDrawing(false);
		searchField.setMaxStringLength(35);
		searchField.setText(staticSearch);
		addButtons();
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
		mc.getTextureManager().bindTexture(getTexture());
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);

		if (button instanceof ButtonBurnItem)
		{
			clickGuiSlot(ItemStack.EMPTY, isShiftKeyDown() ? ContainerTableBase.BURN_ALT : ContainerTableBase.BURN);
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
			searchField.setFocused(true);
		}
		else if (searchField.mouseClicked(mouseX, mouseY, mouseButton) && !table.player.inventory.getItemStack().isEmpty())
		{
			searchField.setText(trim(table.player.inventory.getItemStack().getDisplayName()));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (searchField.textboxKeyTyped(typedChar, keyCode))
		{
			return;
		}

		if (!searchField.isFocused() && (typedChar == '\t' || typedChar == ' '))
		{
			searchField.setFocused(true);
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
	public void updateKnowledge()
	{
		staticPage = 0;
		staticSearch = "";
		updateValidItemList();
	}
}