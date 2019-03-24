package com.latmod.mods.projectex.integration;

import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.playerData.Transmutation;
import moze_intel.projecte.utils.EMCHelper;
import moze_intel.projecte.utils.ItemHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class OfflineKnowledgeProvider implements IKnowledgeProvider
{
	public static void copy(IKnowledgeProvider from, IKnowledgeProvider to)
	{
		to.deserializeNBT(from.serializeNBT());
	}

	public final UUID playerId;
	private final List<ItemStack> knowledge;
	private final IItemHandlerModifiable inputLocks;
	private double emc;
	private boolean fullKnowledge;
	public boolean shouldSave;

	public OfflineKnowledgeProvider(UUID id)
	{
		playerId = id;
		knowledge = new ArrayList<>();
		inputLocks = new ItemStackHandler(9);
		emc = 0D;
		fullKnowledge = false;
		shouldSave = false;
	}

	@Override
	public boolean hasFullKnowledge()
	{
		return fullKnowledge;
	}

	@Override
	public void setFullKnowledge(boolean b)
	{
		fullKnowledge = b;
		shouldSave = true;
	}

	@Override
	public void clearKnowledge()
	{
		knowledge.clear();
		fullKnowledge = false;
		shouldSave = true;
	}

	@Override
	public boolean hasKnowledge(ItemStack stack)
	{
		if (stack.isEmpty())
		{
			return false;
		}
		else if (fullKnowledge)
		{
			return true;
		}

		Iterator var2 = knowledge.iterator();

		ItemStack s;
		do
		{
			if (!var2.hasNext())
			{
				return false;
			}

			s = (ItemStack) var2.next();
		}
		while (!ItemHelper.basicAreStacksEqual(s, stack));
		return true;
	}

	@Override
	public boolean addKnowledge(ItemStack stack)
	{
		if (fullKnowledge)
		{
			return false;
		}
		else if (stack.getItem() == ObjHandler.tome)
		{
			if (!hasKnowledge(stack))
			{
				knowledge.add(stack);
			}

			fullKnowledge = true;
			shouldSave = true;
			return true;
		}
		else if (!hasKnowledge(stack))
		{
			knowledge.add(stack);
			shouldSave = true;
			return true;
		}

		return false;
	}

	@Override
	public boolean removeKnowledge(ItemStack stack)
	{
		boolean removed = false;
		if (stack.getItem() == ObjHandler.tome)
		{
			fullKnowledge = false;
			removed = true;
		}

		if (fullKnowledge)
		{
			return false;
		}

		Iterator iter = knowledge.iterator();

		while (iter.hasNext())
		{
			if (ItemStack.areItemStacksEqual(stack, (ItemStack) iter.next()))
			{
				iter.remove();
				removed = true;
			}
		}

		if (removed)
		{
			shouldSave = true;
		}

		return removed;
	}

	@Override
	public List<ItemStack> getKnowledge()
	{
		return fullKnowledge ? Transmutation.getCachedTomeKnowledge() : Collections.unmodifiableList(knowledge);
	}

	@Override
	public IItemHandlerModifiable getInputAndLocks()
	{
		return inputLocks;
	}

	@Override
	public double getEmc()
	{
		return emc;
	}

	@Override
	public void setEmc(double e)
	{
		emc = e;
		shouldSave = true;
	}

	@Override
	public void sync(EntityPlayerMP player)
	{
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("transmutationEmc", emc);
		NBTTagList knowledgeWrite = new NBTTagList();

		for (ItemStack is : knowledge)
		{
			knowledgeWrite.appendTag(is.serializeNBT());
		}

		nbt.setTag("knowledge", knowledgeWrite);
		nbt.setTag("inputlock", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inputLocks, null));
		nbt.setBoolean("fullknowledge", fullKnowledge);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		emc = nbt.getDouble("transmutationEmc");
		NBTTagList list = nbt.getTagList("knowledge", 10);

		for (int i = 0; i < list.tagCount(); i++)
		{
			ItemStack stack = new ItemStack(list.getCompoundTagAt(i));

			if (!stack.isEmpty())
			{
				knowledge.add(stack);
			}
		}

		pruneStaleKnowledge();
		pruneDuplicateKnowledge();

		for (int i = 0; i < inputLocks.getSlots(); ++i)
		{
			inputLocks.setStackInSlot(i, ItemStack.EMPTY);
		}

		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inputLocks, null, nbt.getTagList("inputlock", 10));
		fullKnowledge = nbt.getBoolean("fullknowledge");
	}

	private void pruneDuplicateKnowledge()
	{
		ItemHelper.compactItemListNoStacksize(knowledge);

		for (ItemStack is : knowledge)
		{
			if (is.getCount() > 1)
			{
				is.setCount(1);
			}
		}
	}

	private void pruneStaleKnowledge()
	{
		knowledge.removeIf((stack) -> !EMCHelper.doesItemHaveEmc(stack));
	}
}