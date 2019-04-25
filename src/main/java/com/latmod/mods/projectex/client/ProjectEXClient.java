package com.latmod.mods.projectex.client;

import com.latmod.mods.projectex.ProjectEXCommon;
import com.latmod.mods.projectex.integration.PersonalEMC;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class ProjectEXClient extends ProjectEXCommon
{
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void updateEMC(long emc)
	{
		PersonalEMC.get(Minecraft.getMinecraft().player).setEmc(emc);
	}
}