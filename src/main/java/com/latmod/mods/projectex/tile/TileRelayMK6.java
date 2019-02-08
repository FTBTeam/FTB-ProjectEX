package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileRelayMK6 extends TileRelayMK5
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk6.capacity;
	}

	@Override
	public double getRelayBonus()
	{
		return ProjectEXConfig.general.mk6.relay_bonus;
	}
}