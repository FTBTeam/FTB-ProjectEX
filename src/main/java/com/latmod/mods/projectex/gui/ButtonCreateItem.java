package com.latmod.mods.projectex.gui;

import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ButtonCreateItem extends ButtonWithStack
{
	public ItemStack type;

	public ButtonCreateItem(ContainerTableBase t, int id, int x, int y)
	{
		super(t, id, x, y);
		type = ItemStack.EMPTY;
	}

	@Override
	public ItemStack getStack()
	{
		return type;
	}
}