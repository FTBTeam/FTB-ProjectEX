package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonCreateItem;
import com.latmod.mods.projectex.gui.GuiStoneTable;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class GuiTableAdvancedGuiHandler implements IAdvancedGuiHandler<GuiStoneTable>
{
	@Override
	public Class<GuiStoneTable> getGuiContainerClass()
	{
		return GuiStoneTable.class;
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(GuiStoneTable gui, int x, int y)
	{
		for (ButtonCreateItem button : gui.itemButtons)
		{
			if (x >= button.x && x < button.x + button.width && y >= button.y && y < button.y + button.height)
			{
				if (!button.type.isEmpty())
				{
					return button.type;
				}
			}
		}

		return null;
	}
}