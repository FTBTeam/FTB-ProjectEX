package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileCollectorMK6 extends TileCollectorMK5
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk6.capacity;
	}

	@Override
	public double getOutput()
	{
		return ProjectEXConfig.general.mk6.collector_output;
	}
}