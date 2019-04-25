package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import net.minecraft.entity.player.EntityPlayerMP;
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
	public long storedEMC = 0L;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		owner = nbt.getUniqueId("owner");
		name = nbt.getString("name");
		double storedEMC1 = nbt.getDouble("emc");
		storedEMC = storedEMC1 > Long.MAX_VALUE ? Long.MAX_VALUE : (long) storedEMC1;
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);

		if (storedEMC > 0L)
		{
			nbt.setLong("emc", storedEMC);
		}

		return super.writeToNBT(nbt);
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
		if (world.isRemote || world.getTotalWorldTime() % 20L != TileRelay.mod(hashCode(), 20))
		{
			return;
		}

		if (storedEMC > 0L)
		{
			EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

			if (player != null)
			{
				IKnowledgeProvider knowledgeProvider = PersonalEMC.get(player);
				knowledgeProvider.setEmc(knowledgeProvider.getEmc() + storedEMC);
				storedEMC = 0L;
				markDirty();
			}
		}
	}

	@Override
	public long acceptEMC(EnumFacing facing, long v)
	{
		if (!world.isRemote)
		{
			storedEMC += v;
		}

		return v;
	}

	@Override
	public long getStoredEmc()
	{
		return storedEMC;
	}

	@Override
	public long getMaximumEmc()
	{
		return Long.MAX_VALUE;
	}
}