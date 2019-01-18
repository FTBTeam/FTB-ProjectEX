package com.latmod.mods.projectex.tile;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.config.ProjectEConfig;
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
				IKnowledgeProvider provider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
				provider.setEmc(provider.getEmc() + (long) ((double) stack.getCount() * (double) value * ProjectEConfig.difficulty.covalenceLoss));
				EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

				if (player != null)
				{
					provider.sync(player);
				}
			}

			return ItemStack.EMPTY;
		}

		return stack;
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
				IKnowledgeProvider provider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);

				if (provider.getEmc() < value)
				{
					return ItemStack.EMPTY;
				}

				ItemStack stack = output.copy();
				stack.setCount((int) (Math.min(amount, Math.min(output.getMaxStackSize(), provider.getEmc() / (double) value))));

				if (!simulate)
				{
					provider.setEmc(provider.getEmc() - value * stack.getCount());
					EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

					if (player != null)
					{
						provider.sync(player);
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