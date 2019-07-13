package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXUtils;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author LatvianModder
 */
public class TileAlchemyTable extends TileEntity implements ITickable, IEmcAcceptor
{
	public long storedEMC = 0L;
	public int progress = 0;

	public long totalCost = 0L;
	public int totalProgress = 0;

	public final ItemStackHandler items = new ItemStackHandler(2)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (slot == 1 || !AlchemyTableRecipes.INSTANCE.hasOutput(stack))
			{
				return stack;
			}

			return super.insertItem(slot, stack, simulate);
		}

		@Override
		public boolean isItemValid(int slot, ItemStack stack)
		{
			return slot == 0 && AlchemyTableRecipes.INSTANCE.hasOutput(stack);
		}
	};

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		storedEMC = nbt.getLong("emc");
		progress = nbt.getInteger("progress");

		NBTTagCompound itemsTag = new NBTTagCompound();
		itemsTag.setTag("Items", nbt.getTagList("items", Constants.NBT.TAG_COMPOUND));
		items.deserializeNBT(itemsTag);

		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (storedEMC > 0L)
		{
			nbt.setLong("emc", storedEMC);
		}

		if (progress > 0)
		{
			nbt.setInteger("progress", progress);
		}

		nbt.setTag("items", items.serializeNBT().getTag("Items"));
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
		if (world.isRemote)
		{
			return;
		}

		totalCost = 0L;
		totalProgress = 0;

		ItemStack output = items.getStackInSlot(1);
		boolean hasOutput = !output.isEmpty();

		if (hasOutput && output.getCount() >= output.getMaxStackSize())
		{
			return;
		}

		ItemStack input = items.getStackInSlot(0);

		AlchemyTableRecipe recipe = AlchemyTableRecipes.INSTANCE.getOutput(input);

		if (recipe == null)
		{
			return;
		}

		if (hasOutput && (recipe.output.isEmpty() || recipe.output.getItem() != output.getItem() || recipe.output.getMetadata() != output.getMetadata()))
		{
			return;
		}

		totalCost = recipe.getTotalCost();
		totalProgress = recipe.getTotalProgress();

		if (storedEMC < totalCost)
		{
			return;
		}

		progress++;

		if (progress >= totalProgress)
		{
			storedEMC -= totalCost;
			progress = 0;

			input.shrink(1);
			items.setStackInSlot(0, input);

			if (hasOutput)
			{
				output.grow(1);
				items.setStackInSlot(1, output);
			}
			else
			{
				items.setStackInSlot(1, ProjectEXUtils.fixOutput(recipe.output));
			}
		}

		markDirty();
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
	public long acceptEMC(EnumFacing facing, long v)
	{
		if (!world.isRemote)
		{
			if (totalCost <= 0L)
			{
				return 0L;
			}

			long d = Math.min(v, getMaximumEmc() - storedEMC);
			storedEMC += d;
			markDirty();
			return d;
		}

		return 0L;
	}

	@Override
	public long getStoredEmc()
	{
		return storedEMC;
	}

	@Override
	public long getMaximumEmc()
	{
		return totalCost * 8L;
	}
}