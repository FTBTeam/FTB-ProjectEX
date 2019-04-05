package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonWithStack;
import com.latmod.mods.projectex.gui.GuiTableBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class TableJEI implements IAdvancedGuiHandler<GuiTableBase>
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