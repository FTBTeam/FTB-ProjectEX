package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.block.EnumTier;
import com.latmod.mods.projectex.net.MessageSyncEMC;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.api.ProjectEAPI;
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
	public void update()
	{
		if (world.isRemote || world.getTotalWorldTime() % 20L != TileRelay.mod(hashCode(), 20))
		{
			return;
		}

		IKnowledgeProvider knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owner);
		knowledgeProvider.setEmc(knowledgeProvider.getEmc() + EnumTier.byMeta(getBlockMetadata()).properties.powerFlowerOutput());

		EntityPlayerMP player = world.getMinecraftServer().getPlayerList().getPlayerByUUID(owner);

		if (player != null)
		{
			ProjectEXNetHandler.NET.sendTo(new MessageSyncEMC(knowledgeProvider.getEmc()), player);
		}
	}
}