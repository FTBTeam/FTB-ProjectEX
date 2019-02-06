package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.net.MessageSyncEMC;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class TileLink extends TileEntity implements IItemHandlerModifiable, ITickable, IEmcAcceptor
{
	public UUID owner = null;
	public String name = "";
	public ItemStack output = ItemStack.EMPTY;
	private boolean isDirty = false;
	public final ItemStack[] inputSlots = new ItemStack[18];
	public double addEMC = 0D;

	public TileLink()
	{
		Arrays.fill(inputSlots, ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		owner = nbt.getUniqueId("owner");
		name = nbt.getString("name");
		output = new ItemStack(nbt.getCompoundTag("output"));

		if (output.isEmpty())
		{
			output = ItemStack.EMPTY;
		}

		NBTTagList inputList = nbt.getTagList("input", Constants.NBT.TAG_COMPOUND);
		Arrays.fill(inputSlots, ItemStack.EMPTY);

		for (int i = 0; i < inputList.tagCount(); i++)
		{
			NBTTagCompound nbt1 = inputList.getCompoundTagAt(i);
			inputSlots[nbt1.getByte("Slot")] = new ItemStack(nbt1);
		}

		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);
		output.setCount(1);

		if (!output.isEmpty())
		{
			nbt.setTag("output", output.serializeNBT());
		}

		NBTTagList inputList = new NBTTagList();

		for (int i = 0; i < inputSlots.length; i++)
		{
			if (!inputSlots[i].isEmpty())
			{
				NBTTagCompound nbt1 = inputSlots[i].serializeNBT();
				nbt1.setByte("Slot", (byte) i);
				inputList.appendTag(nbt1);
			}
		}

		nbt.setTag("input", inputList);
		return super.writeToNBT(nbt);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return serializeNBT();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, side);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this : super.getCapability(capability, side);
	}

	@Override
	public int getSlots()
	{
		return 19;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot == 18)
		{
			if (world.isRemote || output.isEmpty())
			{
				return ItemStack.EMPTY;
			}

			output.setCount(1);
			long value = ProjectEAPI.getEMCProxy().getValue(output);

			if (value > 0L)
			{
				IKnowledgeProvider knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);

				if (knowledgeProvider.getEmc() < value)
				{
					return ItemStack.EMPTY;
				}

				output.setCount(getCount(knowledgeProvider, value, Integer.MAX_VALUE));
				return output.getCount() <= 0 ? ItemStack.EMPTY : output;
			}

			return ItemStack.EMPTY;
		}

		return inputSlots[slot];
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (slot != 18)
		{
			inputSlots[slot] = stack;
			markDirty();
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (slot == 18 || !ProjectEAPI.getEMCProxy().hasValue(stack))
		{
			return stack;
		}

		int limit = stack.getMaxStackSize();

		if (!inputSlots[slot].isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, inputSlots[slot]))
			{
				return stack;
			}

			limit -= inputSlots[slot].getCount();
		}

		if (limit <= 0)
		{
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate)
		{
			if (inputSlots[slot].isEmpty())
			{
				inputSlots[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
			}
			else
			{
				inputSlots[slot].grow(reachedLimit ? limit : stack.getCount());
			}

			markDirty();
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public void markDirty()
	{
		isDirty = true;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return slot != 18 && ProjectEAPI.getEMCProxy().hasValue(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot == 18 && amount > 0 && !world.isRemote && !output.isEmpty())
		{
			output.setCount(1);
			long value = ProjectEAPI.getEMCProxy().getValue(output);

			if (value > 0L)
			{
				IKnowledgeProvider knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);

				if (knowledgeProvider.getEmc() < value)
				{
					return ItemStack.EMPTY;
				}

				ItemStack stack = output.copy();
				stack.setCount(getCount(knowledgeProvider, value, Math.min(amount, output.getMaxStackSize())));

				if (stack.getCount() >= 1)
				{
					if (stack.hasTagCompound() && !NBTWhitelist.shouldDupeWithNBT(stack))
					{
						stack.setTagCompound(new NBTTagCompound());
					}

					if (!simulate)
					{
						knowledgeProvider.setEmc(knowledgeProvider.getEmc() - value * stack.getCount());
						EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

						if (player != null)
						{
							ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(knowledgeProvider.getEmc()), player);
						}
					}

					return stack;
				}

				return ItemStack.EMPTY;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public void update()
	{
		if (!world.isRemote)
		{
			boolean sync = false;
			IKnowledgeProvider knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);

			if (addEMC > 0D)
			{
				knowledgeProvider.setEmc(knowledgeProvider.getEmc() + addEMC);
				addEMC = 0D;
				sync = true;
			}

			for (int i = 0; i < inputSlots.length; i++)
			{
				if (!inputSlots[i].isEmpty())
				{
					long value = ProjectEAPI.getEMCProxy().getValue(inputSlots[i]);

					if (value > 0L)
					{

						knowledgeProvider.setEmc(knowledgeProvider.getEmc() + (long) ((double) inputSlots[i].getCount() * (double) value * ProjectEConfig.difficulty.covalenceLoss));
						sync = true;
						inputSlots[i] = ItemStack.EMPTY;
						markDirty();
					}
				}
			}

			if (sync)
			{
				EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

				if (player != null)
				{
					ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(knowledgeProvider.getEmc()), player);
				}
			}
		}

		if (isDirty)
		{
			isDirty = false;
			world.markChunkDirty(pos, this);
		}
	}

	public int getCount(IKnowledgeProvider knowledgeProvider, long value, int limit)
	{
		if (knowledgeProvider.getEmc() < value)
		{
			return 0;
		}

		return (int) (Math.min(limit, knowledgeProvider.getEmc() / (double) value));
	}

	@Override
	public double acceptEMC(EnumFacing facing, double v)
	{
		if (!world.isRemote)
		{
			addEMC += v;
		}

		return v;
	}

	@Override
	public double getStoredEmc()
	{
		return 0D;
	}

	@Override
	public double getMaximumEmc()
	{
		return Double.MAX_VALUE;
	}
}