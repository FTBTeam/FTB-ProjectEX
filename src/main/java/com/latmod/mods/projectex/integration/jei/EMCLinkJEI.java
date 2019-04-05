package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.GuiLink;
import com.latmod.mods.projectex.net.MessageSendLinkStack;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import mezz.jei.api.gui.IAdvancedGuiHandler;
import mezz.jei.api.gui.IGhostIngredientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public enum EMCLinkJEI implements IAdvancedGuiHandler<GuiLink>, IGhostIngredientHandler<GuiLink>
{
	INSTANCE;

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

	@Override
	public <I> List<Target<I>> getTargets(GuiLink gui, I ingredient, boolean doStart)
	{
		if (ingredient instanceof ItemStack)
		{
			List<Target<I>> list = new ArrayList<>();

			for (GuiButton button : gui.getButtons())
			{
				if (button instanceof GuiLink.ButtonFilter)
				{
					list.add(new Target<I>()
					{
						@Override
						public Rectangle getArea()
						{
							return new Rectangle(button.x, button.y, button.width, button.height);
						}

						@Override
						public void accept(I ingredient)
						{
							if (gui.container.link.setOutputStack(gui.container.player, button.id, (ItemStack) ingredient, false))
							{
								ProjectEXNetHandler.NET.sendToServer(new MessageSendLinkStack(gui.container.link.getPos(), button.id, (ItemStack) ingredient));
							}
						}
					});
				}
			}

			return list;
		}

		return Collections.emptyList();
	}

	@Override
	public void onComplete()
	{
	}
}