package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.ButtonWithStack;
import com.latmod.mods.projectex.gui.GuiArcaneTablet;
import com.latmod.mods.projectex.gui.GuiTableBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;

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
	public List<Rectangle> getGuiExtraAreas(GuiTableBase gui)
	{
		if (gui instanceof GuiArcaneTablet)
		{
			return Collections.singletonList(new Rectangle(gui.getGuiLeft() - 67, gui.getGuiTop() + 10, 68, 89));
		}

		return null;
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