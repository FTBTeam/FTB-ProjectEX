package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.event.PlayerAttemptCondenserSetEvent;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.config.ProjectEConfig;
import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.MinecraftForge;
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
	public UUID owner = new UUID(0L, 0L);
	public String name = "";
	private boolean isDirty = false;
	public final ItemStack[] inputSlots, outputSlots;
	public double addEMC = 0D;

	public TileLink(int in, int out)
	{
		inputSlots = new ItemStack[in];
		outputSlots = new ItemStack[out];
		Arrays.fill(inputSlots, ItemStack.EMPTY);
		Arrays.fill(outputSlots, ItemStack.EMPTY);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		owner = nbt.getUniqueId("owner");
		name = nbt.getString("name");

		Arrays.fill(inputSlots, ItemStack.EMPTY);
		Arrays.fill(outputSlots, ItemStack.EMPTY);

		NBTTagList inputList = nbt.getTagList("input", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < inputList.tagCount(); i++)
		{
			NBTTagCompound nbt1 = inputList.getCompoundTagAt(i);
			inputSlots[nbt1.getByte("Slot")] = new ItemStack(nbt1);
		}

		NBTTagList outputList = nbt.getTagList("output", Constants.NBT.TAG_COMPOUND);

		if (outputList.isEmpty())
		{
			outputSlots[0] = new ItemStack(nbt.getCompoundTag("output"));

			if (outputSlots[0].isEmpty())
			{
				outputSlots[0] = ItemStack.EMPTY;
			}
		}
		else
		{
			for (int i = 0; i < outputList.tagCount(); i++)
			{
				NBTTagCompound nbt1 = outputList.getCompoundTagAt(i);
				outputSlots[nbt1.getByte("Slot")] = new ItemStack(nbt1);
			}
		}

		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);

		NBTTagList outputList = new NBTTagList();

		for (int i = 0; i < outputSlots.length; i++)
		{
			outputSlots[i].setCount(1);

			if (!outputSlots[i].isEmpty())
			{
				NBTTagCompound nbt1 = outputSlots[i].serializeNBT();
				nbt1.setByte("Slot", (byte) i);
				outputList.appendTag(nbt1);
			}
		}

		nbt.setTag("output", outputList);

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
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
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
		return inputSlots.length + outputSlots.length;
	}

	public boolean hasOwner()
	{
		return owner.getLeastSignificantBits() != 0L || owner.getMostSignificantBits() != 0L;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		if (slot < inputSlots.length)
		{
			return inputSlots[slot];
		}

		int index = slot - inputSlots.length;

		if (world.isRemote || !hasOwner())
		{
			return ItemStack.EMPTY;
		}

		outputSlots[index].setCount(1);

		if (outputSlots[index].isEmpty())
		{
			return ItemStack.EMPTY;
		}

		long value = ProjectEAPI.getEMCProxy().getValue(outputSlots[index]);

		if (value > 0L)
		{
			IKnowledgeProvider knowledgeProvider = PersonalEMC.get(world, owner);

			if (knowledgeProvider.getEmc() < value)
			{
				return ItemStack.EMPTY;
			}

			int c = getCount(knowledgeProvider, value, 2000000000);

			if (c <= 0)
			{
				return ItemStack.EMPTY;
			}

			outputSlots[index].setCount(c);
			return outputSlots[index];
		}

		return ItemStack.EMPTY;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (slot < inputSlots.length)
		{
			inputSlots[slot] = stack;
			markDirty();
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (slot >= inputSlots.length || !ProjectEAPI.getEMCProxy().hasValue(stack))
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
		return slot < inputSlots.length && ProjectEAPI.getEMCProxy().hasValue(stack);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (slot < inputSlots.length || amount <= 0 || world.isRemote || !hasOwner())
		{
			return ItemStack.EMPTY;
		}

		int index = slot - inputSlots.length;
		outputSlots[index].setCount(1);

		if (outputSlots[index].isEmpty())
		{
			return ItemStack.EMPTY;
		}

		long value = ProjectEAPI.getEMCProxy().getValue(outputSlots[index]);

		if (value <= 0L)
		{
			return ItemStack.EMPTY;
		}

		IKnowledgeProvider knowledgeProvider = PersonalEMC.get(world, owner);

		if (knowledgeProvider.getEmc() < value)
		{
			return ItemStack.EMPTY;
		}

		ItemStack stack = outputSlots[index].copy();
		stack.setCount(getCount(knowledgeProvider, value, Math.min(amount, outputSlots[index].getMaxStackSize())));

		if (stack.getCount() >= 1)
		{
			if (stack.hasTagCompound() && !NBTWhitelist.shouldDupeWithNBT(stack))
			{
				stack.setTagCompound(new NBTTagCompound());
			}

			if (!simulate)
			{
				knowledgeProvider.setEmc(knowledgeProvider.getEmc() - value * stack.getCount());
			}

			return stack;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public void onLoad()
	{
		if (world.isRemote)
		{
			world.tickableTileEntities.remove(this);
		}

		validate();
	}

	@Override
	public void update()
	{
		if (world.isRemote)
		{
			return;
		}

		if (hasOwner())
		{
			IKnowledgeProvider knowledgeProvider = PersonalEMC.get(world, owner);
			double emc = knowledgeProvider.getEmc();

			if (addEMC > 0D)
			{
				emc += addEMC;
				addEMC = 0D;
			}

			for (int i = 0; i < inputSlots.length; i++)
			{
				if (!inputSlots[i].isEmpty())
				{
					long value = ProjectEAPI.getEMCProxy().getValue(inputSlots[i]);

					if (value > 0L)
					{
						emc += (double) inputSlots[i].getCount() * (double) value * ProjectEConfig.difficulty.covalenceLoss;
						inputSlots[i] = ItemStack.EMPTY;
						markDirty();
					}
				}
			}

			knowledgeProvider.setEmc(emc);
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

	public boolean setOutputStack(EntityPlayer player, int slot, ItemStack stack)
	{
		if (ProjectEAPI.getEMCProxy().hasValue(stack) && PersonalEMC.get(player).hasKnowledge(stack))
		{
			stack = ItemHandlerHelper.copyStackWithSize(stack, 1);

			if (!MinecraftForge.EVENT_BUS.post(new PlayerAttemptCondenserSetEvent(player, stack)))
			{
				outputSlots[slot] = stack;
				markDirty();
			}

			return true;
		}

		return false;
	}
}