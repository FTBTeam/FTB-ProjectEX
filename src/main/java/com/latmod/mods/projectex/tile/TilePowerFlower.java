package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.block.EnumTier;
import com.latmod.mods.projectex.integration.PersonalEMC;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class TilePowerFlower extends TileEntity implements ITickable
{
	public UUID owner = new UUID(0L, 0L);
	public String name = "";
	public double storedEMC = 0D;

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		owner = nbt.getUniqueId("owner");
		name = nbt.getString("name");
		storedEMC = nbt.getDouble("emc");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId("owner", owner);
		nbt.setString("name", name);

		if (storedEMC > 0D)
		{
			nbt.setDouble("emc", storedEMC);
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
		if (world.isRemote || world.getTotalWorldTime() % 20L != TileRelay.mod(hashCode(), 20))
		{
			return;
		}

		storedEMC += EnumTier.byMeta(getBlockMetadata()).properties.powerFlowerOutput();

		EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

		if (player != null)
		{
			IKnowledgeProvider knowledgeProvider = PersonalEMC.get(player);
			knowledgeProvider.setEmc(knowledgeProvider.getEmc() + storedEMC);
			storedEMC = 0;
		}
		else
		{
			world.markChunkDirty(pos, this);
		}
	}
}