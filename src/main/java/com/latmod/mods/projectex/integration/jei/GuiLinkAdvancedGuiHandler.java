package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.GuiLink;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class GuiLinkAdvancedGuiHandler implements IAdvancedGuiHandler<GuiLink>
{
	@Override
	public Class<GuiLink> getGuiContainerClass()
	{
		return GuiLink.class;
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(GuiLink gui, int x, int y)
	{
		for (GuiButton button : gui.getButtons())
		{
			if (button instanceof GuiLink.ButtonFilter && x >= button.x && x < button.x + button.width && y >= button.y && y < button.y + button.height)
			{
				ItemStack stack = gui.container.link.outputSlots[button.id];

				if (!stack.isEmpty())
				{
					return stack;
				}
			}
		}

		return null;
	}
}