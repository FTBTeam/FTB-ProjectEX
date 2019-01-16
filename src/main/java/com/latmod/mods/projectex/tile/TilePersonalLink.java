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
public class TilePersonalLink extends TileEntity implements IItemHandler
{
	public UUID owner = null;
	public String name = "";

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		owner = nbt.getUniqueId("owner");
		name = nbt.getString("name");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);
		return super.writeToNBT(nbt);
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
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (world.isRemote || owner == null)
		{
			return stack;
		}

		long value = ProjectEAPI.getEMCProxy().getValue(stack);

		if (value > 0L)
		{
			if (!simulate)
			{
				IKnowledgeProvider provider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
				provider.setEmc(provider.getEmc() + stack.getCount() * value * ProjectEConfig.difficulty.covalenceLoss);
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
		return !world.isRemote && owner != null && ProjectEAPI.getEMCProxy().hasValue(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return Integer.MAX_VALUE;
	}
}