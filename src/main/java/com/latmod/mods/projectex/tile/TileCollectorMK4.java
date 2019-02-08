package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.api.tile.IEmcProvider;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK2Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK3Tile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * @author LatvianModder
 */
public class TileCollectorMK4 extends TileEntity implements ITickable, IEmcProvider
{
	@Override
	public void update()
	{
		if (world.isRemote || world.getTotalWorldTime() % 20L != 4L)
		{
			return;
		}

		double out = getOutput();

		int tempSize = 0;

		for (int i = 0; i < 6; i++)
		{
			TileRelayMK4.TEMP[i] = null;
			TileEntity tileEntity = world.getTileEntity(pos.offset(EnumFacing.VALUES[i]));

			if (tileEntity instanceof IEmcAcceptor)
			{
				TileRelayMK4.TEMP[i] = (IEmcAcceptor) tileEntity;
				tempSize++;
			}
		}

		if (tempSize > 0)
		{
			double s = out / tempSize;

			for (int i = 0; i < 6; i++)
			{
				IEmcAcceptor emcAcceptor = TileRelayMK4.TEMP[i];

				if (emcAcceptor != null)
				{
					EnumFacing facing = EnumFacing.VALUES[i].getOpposite();
					emcAcceptor.acceptEMC(facing, s);

					if (emcAcceptor instanceof TileRelayMK4)
					{
						emcAcceptor.acceptEMC(facing, ((TileRelayMK4) TileRelayMK4.TEMP[i]).getRelayBonus());
					}
					else if (emcAcceptor instanceof RelayMK3Tile)
					{
						emcAcceptor.acceptEMC(facing, 0.5D);
					}
					else if (emcAcceptor instanceof RelayMK2Tile)
					{
						emcAcceptor.acceptEMC(facing, 0.15D);
					}
					else if (emcAcceptor instanceof RelayMK1Tile)
					{
						emcAcceptor.acceptEMC(facing, 0.05D);
					}
				}
			}
		}
	}

	@Override
	public void markDirty()
	{
		if (world != null)
		{
			world.markChunkDirty(pos, this);
		}
	}

	@Override
	public double provideEMC(EnumFacing facing, double v)
	{
		return 0D;
	}

	@Override
	public double getStoredEmc()
	{
		return 0D;
	}

	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk4.capacity;
	}

	public double getOutput()
	{
		return ProjectEXConfig.general.mk4.collector_output;
	}
}