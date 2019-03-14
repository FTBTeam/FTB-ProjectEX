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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class ContainerTableBase extends Container
{
	public interface KnowledgeUpdate
	{
		void updateKnowledge();
	}

	public static final int BURN = 1;
	public static final int TAKE_STACK = 2;
	public static final int TAKE_ONE = 3;
	public static final int UNLEARN = 4;

	public final EntityPlayer player;
	public final IKnowledgeProvider playerData;
	public KnowledgeUpdate knowledgeUpdate;

	public ContainerTableBase(EntityPlayer p)
	{
		player = p;
		playerData = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(player.getUniqueID());
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		Slot slot = inventorySlots.get(index);
		ItemStack stack = slot.getStack();

		if (!stack.isEmpty())
		{
			if (stack.getItem() instanceof IItemEmc || !isItemValid(stack) || !ProjectEAPI.getEMCProxy().hasValue(stack))
			{
				return ItemStack.EMPTY;
			}

			if (!playerData.hasKnowledge(stack))
			{
				if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack)))
				{
					return ItemStack.EMPTY;
				}

				playerData.addKnowledge(ItemHandlerHelper.copyStackWithSize(stack, 1));

				if (knowledgeUpdate != null)
				{
					knowledgeUpdate.updateKnowledge();
				}
			}

			playerData.setEmc(playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());
			slot.putStack(ItemStack.EMPTY);
			return stack;
		}

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

	public boolean clickGuiSlot(ItemStack type, int mode)
	{
		ItemStack stack = player.inventory.getItemStack();

		if (mode == BURN)
		{
			if (stack.isEmpty() || stack.getItem() instanceof IItemEmc || !isItemValid(stack) || !ProjectEAPI.getEMCProxy().hasValue(stack))
			{
				return false;
			}

			if (!playerData.hasKnowledge(stack))
			{
				if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack)))
				{
					return false;
				}

				playerData.addKnowledge(ItemHandlerHelper.copyStackWithSize(stack, 1));

				if (knowledgeUpdate != null)
				{
					knowledgeUpdate.updateKnowledge();
				}
			}

			playerData.setEmc(playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());
			player.inventory.setItemStack(ItemStack.EMPTY);
			return true;
		}
		else if (mode == TAKE_STACK)
		{
			if (type.isEmpty())
			{
				return false;
			}

			int amount = type.getMaxStackSize();

			double value = ProjectEAPI.getEMCProxy().getValue(type);

			if (value <= 0D)
			{
				return false;
			}

			double max = playerData.getEmc() / value;

			if (amount > max)
			{
				amount = (int) Math.min(amount, max);
			}

			if (amount <= 0)
			{
				return false;
			}

			playerData.setEmc(playerData.getEmc() - value * amount);

			if (player instanceof EntityPlayerMP)
			{
				ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(playerData.getEmc()), (EntityPlayerMP) player);
			}

			ItemHandlerHelper.giveItemToPlayer(player, ItemHandlerHelper.copyStackWithSize(type, amount));
			return true;
		}
		else if (mode == TAKE_ONE)
		{
			if (type.isEmpty())
			{
				return false;
			}

			if (!stack.isEmpty() && (!ItemHandlerHelper.canItemStacksStack(type, stack) || stack.getCount() >= stack.getMaxStackSize()))
			{
				return false;
			}

			double value = ProjectEAPI.getEMCProxy().getValue(type);

			if (value <= 0D)
			{
				return false;
			}

			if (playerData.getEmc() < value)
			{
				return false;
			}

			playerData.setEmc(playerData.getEmc() - value);

			if (player instanceof EntityPlayerMP)
			{
				ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(playerData.getEmc()), (EntityPlayerMP) player);
			}

			if (!stack.isEmpty())
			{
				stack.grow(1);
			}
			else
			{
				player.inventory.setItemStack(ItemHandlerHelper.copyStackWithSize(type, 1));
			}

			return true;
		}

		return false;
	}
}