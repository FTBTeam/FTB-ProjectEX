package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.ProjectEXConfig;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.api.tile.IEmcProvider;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * @author LatvianModder
 */
public class TileRelayMK4 extends TileEntity implements ITickable, IEmcAcceptor, IEmcProvider
{
	public static final IEmcAcceptor[] TEMP = new IEmcAcceptor[6];
	public double stored = 0D;

	@Override
	public void update()
	{
		if (world.isRemote || stored <= 0D || world.getTotalWorldTime() % 20L != 5L)
		{
			return;
		}

		int tempSize = 0;

		for (int i = 0; i < 6; i++)
		{
			TEMP[i] = null;
			TileEntity tileEntity = world.getTileEntity(pos.offset(EnumFacing.VALUES[i]));

			if (tileEntity instanceof IEmcAcceptor && !(tileEntity instanceof TileRelayMK4) && !(tileEntity instanceof RelayMK1Tile))
			{
				TEMP[i] = (IEmcAcceptor) tileEntity;
				tempSize++;
			}
		}

		if (tempSize > 0)
		{
			double s = stored / tempSize;

			for (int i = 0; i < 6; i++)
			{
				if (TEMP[i] != null)
				{
					double a = TEMP[i].acceptEMC(EnumFacing.VALUES[i].getOpposite(), s);

					if (a > 0D)
					{
						stored -= a;
						markDirty();
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
	public double acceptEMC(EnumFacing facing, double v)
	{
		double v1 = Math.min(getMaximumEmc() - stored, v);

		if (v1 > 0D)
		{
			stored += v1;
			markDirty();
		}

		return v1;
	}

	@Override
	public double provideEMC(EnumFacing facing, double v)
	{
		double v1 = Math.min(stored, v);

		if (v1 > 0D)
		{
			stored -= v1;
			markDirty();
		}

		return v1;
	}

	@Override
	public double getStoredEmc()
	{
		return stored;
	}

	@Override
	public double getMaximumEmc()
	{
		return ProjectEXConfig.general.mk4.capacity;
	}

	public double getRelayBonus()
	{
		return ProjectEXConfig.general.mk4.relay_bonus;
	}
}