package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonCreateItem;
import com.latmod.mods.projectex.gui.ContainerArcaneTablet;
import com.latmod.mods.projectex.gui.GuiArcaneTablet;
import com.latmod.mods.projectex.net.MessageArcaneTableRecipeTransfer;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LatvianModder
 */
public enum ArcaneTabletJEI implements IAdvancedGuiHandler<GuiArcaneTablet>, IRecipeTransferHandler<ContainerArcaneTablet>
{
	INSTANCE;

	private static final IRecipeTransferError ERROR_CANNOT_TRANSFER = new IRecipeTransferError()
	{
		@Override
		public Type getType()
		{
			return Type.INTERNAL;
		}

		@Override
		public void showError(Minecraft mc, int mouseX, int mouseY, IRecipeLayout layout, int recipeX, int recipeY)
		{
		}
	};

	@Override
	public Class<GuiArcaneTablet> getGuiContainerClass()
	{
		return GuiArcaneTablet.class;
	}

	@Override
	public Class<ContainerArcaneTablet> getContainerClass()
	{
		return ContainerArcaneTablet.class;
	}

	@Nullable
	@Override
	public IRecipeTransferError transferRecipe(ContainerArcaneTablet tablet, IRecipeLayout layout, EntityPlayer player, boolean maxTransfer, boolean doTransfer)
	{
		if (doTransfer)
		{
			ProjectEXNetHandler.NET.sendToServer(new MessageArcaneTableRecipeTransfer(layout.getItemStacks().getGuiIngredients(), tablet.inventorySlots.stream().filter(s -> s.inventory instanceof InventoryCrafting).collect(Collectors.toList()), maxTransfer));
		}
		else if (!layout.getRecipeCategory().getUid().equals(VanillaRecipeCategoryUid.CRAFTING))
		{
			return ERROR_CANNOT_TRANSFER;
		}

		return null;
	}

	@Override
	public List<Rectangle> getGuiExtraAreas(GuiArcaneTablet gui)
	{
		return Collections.singletonList(new Rectangle(gui.getGuiLeft() - 75, gui.getGuiTop() + 10, 76, 89));
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(GuiArcaneTablet gui, int x, int y)
	{
		for (GuiButton button : gui.getButtons())
		{
			if (button instanceof ButtonCreateItem && x >= button.x && x < button.x + button.width && y >= button.y && y < button.y + button.height)
			{
				if (!((ButtonCreateItem) button).type.isEmpty())
				{
					return ((ButtonCreateItem) button).type;
				}
			}
		}

		return null;
	}
}