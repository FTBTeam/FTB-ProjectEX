package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;

/**
 * @author LatvianModder
 */
public class TileRelayFinal extends TileRelayMK6
{
	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk_final.capacity;
	}

	@Override
	public double getRelayBonus()
	{
		return ProjectEXConfig.general.mk_final.relay_bonus;
	}
}