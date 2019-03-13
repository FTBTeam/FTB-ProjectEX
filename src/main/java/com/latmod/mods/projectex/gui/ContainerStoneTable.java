package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEXConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class ContainerStoneTable extends ContainerTableBase
{
	public ContainerStoneTable(EntityPlayer p)
	{
		super(p);

		for (int k = 0; k < 3; ++k)
		{
			for (int i1 = 0; i1 < 9; ++i1)
			{
				addSlotToContainer(new Slot(player.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 135 + k * 18));
			}
		}

		for (int l = 0; l < 9; ++l)
		{
			addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 193));
		}
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return ProjectEXConfig.general.isStoneTableWhitelisted(stack);
	}
}