package com.latmod.mods.projectex.integration.jei;

import com.latmod.mods.projectex.gui.GuiLink;
import com.latmod.mods.projectex.net.MessageSendLinkStack;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import mezz.jei.api.gui.IGhostIngredientHandler;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiLinkGhostIngredientHandler implements IGhostIngredientHandler<GuiLink>
{
	@Override
	public <I> List<Target<I>> getTargets(GuiLink gui, I ingredient, boolean doStart)
	{
		if (ingredient instanceof ItemStack)
		{
			return Collections.singletonList(new Target<I>()
			{
				@Override
				public Rectangle getArea()
				{
					return new Rectangle(gui.getGuiLeft() + 152, gui.getGuiTop() + 35, 16, 16);
				}

				@Override
				public void accept(I ingredient)
				{
					if (gui.container.link.setOutputStack(gui.container.player, (ItemStack) ingredient))
					{
						ProjectEXNetHandler.NET.sendToServer(new MessageSendLinkStack(gui.container.link.getPos(), 0, (ItemStack) ingredient));
					}
				}
			});
		}

		return Collections.emptyList();
	}

	@Override
	public void onComplete()
	{
	}
}