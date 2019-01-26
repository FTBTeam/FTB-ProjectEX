package com.latmod.mods.projectex.tile;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class TileLink extends TileEntity implements IItemHandler
{
	public UUID owner = null;
	public String name = "";
	public ItemStack output = ItemStack.EMPTY;
	private IKnowledgeProvider knowledgeProvider;

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

		knowledgeProvider = null;

		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);

		if (!output.isEmpty())
		{
			output.setCount(1);
			nbt.setTag("output", output.serializeNBT());
		}

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
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot == 1 && !output.isEmpty())
		{
			output.setCount(output.getMaxStackSize());
			return output;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (world.isRemote || owner == null || slot == 1)
		{
			return stack;
		}

		long value = ProjectEAPI.getEMCProxy().getValue(stack);

		if (value > 0L)
		{
			if (!simulate)
			{
				if (knowledgeProvider == null)
				{
					knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
				}

				knowledgeProvider.setEmc(knowledgeProvider.getEmc() + (long) ((double) stack.getCount() * (double) value * ProjectEConfig.difficulty.covalenceLoss));
				EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

				if (player != null)
				{
					knowledgeProvider.sync(player);
				}
			}

			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public void markDirty()
	{
		if (world != null)
		{
			world.markChunkDirty(pos, this);
		}
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack)
	{
		return !world.isRemote && slot == 0 && owner != null && ProjectEAPI.getEMCProxy().hasValue(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot == 1 && amount > 0 && !world.isRemote && !output.isEmpty())
		{
			output.setCount(1);
			long value = ProjectEAPI.getEMCProxy().getValue(output);

			if (value > 0L)
			{
				if (knowledgeProvider == null)
				{
					knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
				}

				if (knowledgeProvider.getEmc() < value)
				{
					return ItemStack.EMPTY;
				}

				ItemStack stack = output.copy();
				stack.setCount((int) (Math.min(amount, Math.min(output.getMaxStackSize(), knowledgeProvider.getEmc() / (double) value))));

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
						knowledgeProvider.sync(player);
					}
				}

				return stack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return Integer.MAX_VALUE;
	}
}