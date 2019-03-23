package com.latmod.mods.projectex.client;

import com.latmod.mods.projectex.ProjectEXCommon;
import com.latmod.mods.projectex.integration.PersonalEMC;
import net.minecraft.client.Minecraft;

/**
 * @author LatvianModder
 */
public class ProjectEXClient extends ProjectEXCommon
{
	@Override
	public void updateEMC(double emc)
	{
		PersonalEMC.get(Minecraft.getMinecraft().player).setEmc(emc);
	}
}