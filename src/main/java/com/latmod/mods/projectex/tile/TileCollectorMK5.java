package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileCollectorMK5 extends TileCollectorMK4
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk5.capacity;
	}

	@Override
	public double getOutput()
	{
		return ProjectEXConfig.general.mk5.collector_output;
	}
}