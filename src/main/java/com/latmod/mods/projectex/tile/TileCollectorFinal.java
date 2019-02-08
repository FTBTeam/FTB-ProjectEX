package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileCollectorFinal extends TileCollectorMK6
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk_final.capacity;
	}

	@Override
	public double getOutput()
	{
		return ProjectEXConfig.general.mk_final.collector_output;
	}
}