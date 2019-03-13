package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.net.MessageSyncEMC;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.event.PlayerAttemptLearnEvent;
import moze_intel.projecte.api.item.IItemEmc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class ContainerTableBase extends Container
{
	public static final int BURN = 1;
	public static final int TAKE_STACK = 2;
	public static final int TAKE_ONE = 3;

	public final EntityPlayer player;
	public final IKnowledgeProvider playerData;

	public ContainerTableBase(EntityPlayer p)
	{
		player = p;
		playerData = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(player.getUniqueID());
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public boolean isItemValid(ItemStack stack)
	{
		return true;
	}

	public int clickGuiSlot(ItemStack type, int mode)
	{
		if (mode == BURN)
		{
			ItemStack stack = player.inventory.getItemStack();

			if (stack.isEmpty() || stack.getItem() instanceof IItemEmc || !isItemValid(stack) || !ProjectEAPI.getEMCProxy().hasValue(stack))
			{
				return 0;
			}

			int r = 1;

			if (!playerData.hasKnowledge(stack))
			{
				if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack)))
				{
					return 0;
				}

				playerData.addKnowledge(ItemHandlerHelper.copyStackWithSize(stack, 1));
				r = 2;
			}

			playerData.setEmc(playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());
			player.inventory.setItemStack(ItemStack.EMPTY);
			return r;
		}
		else if (mode == TAKE_STACK || mode == TAKE_ONE)
		{
			if (!player.inventory.getItemStack().isEmpty())
			{
				return 0;
			}

			int amount = mode == TAKE_STACK ? 64 : 1;

			if (type.isEmpty())
			{
				return 0;
			}

			double value = ProjectEAPI.getEMCProxy().getValue(type);

			if (value <= 0D)
			{
				return 0;
			}

			double max = playerData.getEmc() / value;

			if (amount > max)
			{
				amount = (int) Math.min(amount, max);
			}

			if (amount <= 0)
			{
				return 0;
			}

			playerData.setEmc(playerData.getEmc() - value * amount);

			if (player instanceof EntityPlayerMP)
			{
				ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(playerData.getEmc()), (EntityPlayerMP) player);
			}

			player.inventory.setItemStack(ItemHandlerHelper.copyStackWithSize(type, amount));
			return 1;
		}

		return 0;
	}
}