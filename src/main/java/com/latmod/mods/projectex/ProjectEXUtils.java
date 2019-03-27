package com.latmod.mods.projectex;

import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class ProjectEXUtils
{
	public static ItemStack fixOutput(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(stack, 1);

		if (!stack1.getHasSubtypes() && stack1.isItemStackDamageable())
		{
			stack1.setItemDamage(0);
		}

		if (stack1.hasTagCompound() && !NBTWhitelist.shouldDupeWithNBT(stack1))
		{
			stack1.setTagCompound(new NBTTagCompound());
		}

		return stack1;
	}
}