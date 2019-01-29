package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.ProjectEX;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class ProjectEXNetHandler
{
	public static SimpleNetworkWrapper NET;

	public static void init()
	{
		NET = new SimpleNetworkWrapper(ProjectEX.MOD_ID);
		NET.registerMessage(new MessageSyncEMC.Handler(), MessageSyncEMC.class, 0, Side.CLIENT);
	}
}