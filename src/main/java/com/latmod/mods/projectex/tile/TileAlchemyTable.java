package com.latmod.mods.projectex.tile;

import moze_intel.projecte.api.tile.IEmcAcceptor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * @author LatvianModder
 */
public class TileAlchemyTable extends TileEntity implements ITickable, IItemHandlerModifiable, IEmcAcceptor
{
	public double storedEMC = 0D;
	public ItemStack input = ItemStack.EMPTY;
	public ItemStack output = ItemStack.EMPTY;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		storedEMC = nbt.getDouble("emc");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (storedEMC > 0D)
		{
			nbt.setDouble("emc", storedEMC);
		}

		if (!input.isEmpty())
		{
			nbt.setTag("input", input.serializeNBT());
		}

		if (!output.isEmpty())
		{
			nbt.setTag("output", output.serializeNBT());
		}

		return super.writeToNBT(nbt);
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
	public double acceptEMC(EnumFacing enumFacing, double v)
	{
		return 0;
	}

	@Override
	public double getStoredEmc()
	{
		return storedEMC;
	}

	@Override
	public double getMaximumEmc()
	{
		return 1D;
	}

	@Override
	public int getSlots()
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return slot == 0 ? input : output;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		if (slot == 0)
		{
			input = stack;
		}
		else
		{
			output = stack;
		}
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (stack.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		ItemStack existing = slot == 0 ? input : output;

		int limit = stack.getMaxStackSize();

		if (!existing.isEmpty())
		{
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
			{
				return stack;
			}

			limit -= existing.getCount();
		}

		if (limit <= 0)
		{
			return stack;
		}

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate)
		{
			if (existing.isEmpty())
			{
				//this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			}
			else
			{
				existing.grow(reachedLimit ? limit : stack.getCount());
			}

			markDirty();
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (amount == 0)
		{
			return ItemStack.EMPTY;
		}

		ItemStack existing = slot == 0 ? input : output;

		if (existing.isEmpty())
		{
			return ItemStack.EMPTY;
		}

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract)
		{
			if (!simulate)
			{
				//this.stacks.set(slot, ItemStack.EMPTY);
				markDirty();
			}

			return existing;
		}
		else if (!simulate)
		{
			//this.stacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
			markDirty();
		}

		return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}
}