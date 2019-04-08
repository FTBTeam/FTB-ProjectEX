package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class TileEnergyLink extends TileEntity implements ITickable, IEmcAcceptor
{
	public UUID owner = new UUID(0L, 0L);
	public String name = "";
	private boolean isDirty = false;
	public double addEMC = 0D;

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

	public boolean hasOwner()
	{
		return owner.getLeastSignificantBits() != 0L || owner.getMostSignificantBits() != 0L;
	}

	@Override
	public void markDirty()
	{
		isDirty = true;
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
			if (addEMC > 0D)
			{
				IKnowledgeProvider knowledgeProvider = PersonalEMC.get(world, owner);
				knowledgeProvider.setEmc(knowledgeProvider.getEmc() + addEMC);
				addEMC = 0D;
				markDirty();
			}
		}

		if (isDirty)
		{
			isDirty = false;
			world.markChunkDirty(pos, this);
		}
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