package com.latmod.mods.projectex.net;

import com.latmod.mods.projectex.ProjectEX;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * @author LatvianModder
 */
public class ProjectEXNetHandler
{
	public static SimpleNetworkWrapper NET;

	public static void init()
	{
		NET = new SimpleNetworkWrapper(ProjectEX.MOD_ID);
	}
}