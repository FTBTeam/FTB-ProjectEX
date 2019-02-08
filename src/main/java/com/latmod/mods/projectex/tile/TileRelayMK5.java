package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileRelayMK5 extends TileRelayMK4
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk5.capacity;
	}

	@Override
	public double getRelayBonus()
	{
		return ProjectEXConfig.general.mk5.relay_bonus;
	}
}