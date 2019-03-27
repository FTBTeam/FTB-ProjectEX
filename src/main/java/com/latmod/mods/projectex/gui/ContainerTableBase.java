package com.latmod.mods.projectex.gui;

import com.latmod.mods.projectex.ProjectEXUtils;
import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.event.PlayerAttemptLearnEvent;
import moze_intel.projecte.api.item.IItemEmc;
import net.minecraft.entity.player.EntityPlayer;
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
	public static final int BURN_ALT = 4;

	public final EntityPlayer player;
	public final IKnowledgeProvider playerData;
	public KnowledgeUpdate knowledgeUpdate;

	public ContainerTableBase(EntityPlayer p)
	{
		player = p;
		playerData = PersonalEMC.get(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		Slot slot = inventorySlots.get(index);
		ItemStack stack = slot.getStack();

		if (!stack.isEmpty())
		{
			if (!ProjectEAPI.getEMCProxy().hasValue(stack))
			{
				return ItemStack.EMPTY;
			}

			ItemStack stack1 = ProjectEXUtils.fixOutput(stack);

			if (!isItemValid(stack1))
			{
				return ItemStack.EMPTY;
			}

			if (!playerData.hasKnowledge(stack1))
			{
				if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack1)))
				{
					return ItemStack.EMPTY;
				}

				playerData.addKnowledge(stack1);

				if (knowledgeUpdate != null)
				{
					knowledgeUpdate.updateKnowledge();
				}
			}

			playerData.setEmc(playerData.getEmc() + ProjectEAPI.getEMCProxy().getValue(stack) * stack.getCount());
			slot.putStack(ItemStack.EMPTY);
			return stack1;
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
			if (stack.isEmpty() || !ProjectEAPI.getEMCProxy().hasValue(stack))
			{
				return false;
			}

			ItemStack stack1 = ProjectEXUtils.fixOutput(stack);

			if (!isItemValid(stack1))
			{
				return false;
			}

			if (!playerData.hasKnowledge(stack1))
			{
				if (MinecraftForge.EVENT_BUS.post(new PlayerAttemptLearnEvent(player, stack1)))
				{
					return false;
				}

				playerData.addKnowledge(stack1);

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

			ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(type, 1);
			double value = ProjectEAPI.getEMCProxy().getValue(stack1);

			if (value <= 0D)
			{
				return false;
			}

			int amount = type.getMaxStackSize();

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
			stack1.setCount(amount);
			ItemHandlerHelper.giveItemToPlayer(player, stack1);
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

			ItemStack stack1 = ItemHandlerHelper.copyStackWithSize(type, 1);
			double value = ProjectEAPI.getEMCProxy().getValue(stack1);

			if (value <= 0D)
			{
				return false;
			}

			if (playerData.getEmc() < value)
			{
				return false;
			}

			playerData.setEmc(playerData.getEmc() - value);

			if (!stack.isEmpty())
			{
				stack.grow(1);
			}
			else
			{
				player.inventory.setItemStack(stack1);
			}

			return true;
		}
		else if (mode == BURN_ALT)
		{
			if (!(stack.getItem() instanceof IItemEmc))
			{
				return false;
			}

			IItemEmc emcItem = (IItemEmc) stack.getItem();
			double stored = emcItem.getStoredEmc(stack);

			if (stored > 0D)
			{
				playerData.setEmc(playerData.getEmc() + emcItem.extractEmc(stack, stored));
			}
			else
			{
				playerData.setEmc(playerData.getEmc() - emcItem.addEmc(stack, Math.min(playerData.getEmc(), emcItem.getMaximumEmc(stack))));
			}

			player.inventory.setItemStack(stack);
			return true;
		}

		return false;
	}
}