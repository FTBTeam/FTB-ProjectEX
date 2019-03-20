package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonCreateItem;
import com.latmod.mods.projectex.gui.GuiTableBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class GuiTableAdvancedGuiHandler implements IAdvancedGuiHandler<GuiTableBase>
{
	@Override
	public Class<GuiTableBase> getGuiContainerClass()
	{
		return GuiTableBase.class;
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(GuiTableBase gui, int x, int y)
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