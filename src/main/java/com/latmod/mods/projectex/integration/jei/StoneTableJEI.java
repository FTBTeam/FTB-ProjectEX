package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonWithStack;
import com.latmod.mods.projectex.gui.GuiStoneTable;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public enum StoneTableJEI implements IAdvancedGuiHandler<GuiStoneTable>
{
	INSTANCE;

	@Override
	public Class<GuiStoneTable> getGuiContainerClass()
	{
		return GuiStoneTable.class;
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(GuiStoneTable gui, int x, int y)
	{
		for (GuiButton button : gui.getButtons())
		{
			if (button instanceof ButtonWithStack && x >= button.x && x < button.x + button.width && y >= button.y && y < button.y + button.height)
			{
				ItemStack stack = ((ButtonWithStack) button).getStack();

				if (!stack.isEmpty())
				{
					return stack;
				}
			}
		}

		return null;
	}
}