package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.block.EnumTier;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK2Tile;
import moze_intel.projecte.gameObjs.tiles.RelayMK3Tile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * @author LatvianModder
 */
public class TileCollector extends TileEntity implements ITickable
{
	@Override
	public void onLoad()
	{
		if (world.isRemote)
		{
			world.tickableTileEntities.remove(this);
		}

		validate();
	}

	@Override
	public void update()
	{
		if (world.isRemote || world.getTotalWorldTime() % 20L != TileRelay.mod(hashCode(), 20))
		{
			return;
		}

		int tempSize = 0;

		for (int i = 0; i < 6; i++)
		{
			TileRelay.TEMP[i] = null;
			TileEntity tileEntity = world.getTileEntity(pos.offset(EnumFacing.VALUES[i]));

			if (tileEntity instanceof IEmcAcceptor)
			{
				IEmcAcceptor emcAcceptor = (IEmcAcceptor) tileEntity;
				TileRelay.TEMP[i] = emcAcceptor;
				tempSize++;

				if (emcAcceptor instanceof TileRelay)
				{
					((TileRelay) emcAcceptor).addRelayBonus(EnumFacing.VALUES[i].getOpposite());
				}
				else if (emcAcceptor instanceof RelayMK3Tile)
				{
					emcAcceptor.acceptEMC(EnumFacing.VALUES[i].getOpposite(), 10L);
				}
				else if (emcAcceptor instanceof RelayMK2Tile)
				{
					emcAcceptor.acceptEMC(EnumFacing.VALUES[i].getOpposite(), 3L);
				}
				else if (emcAcceptor instanceof RelayMK1Tile)
				{
					emcAcceptor.acceptEMC(EnumFacing.VALUES[i].getOpposite(), 1L);
				}
			}
		}

		if (tempSize > 0)
		{
			long s = (long) (EnumTier.byMeta(getBlockMetadata()).properties.collector_output / tempSize);

			for (int i = 0; i < 6; i++)
			{
				IEmcAcceptor emcAcceptor = TileRelay.TEMP[i];

				if (emcAcceptor != null)
				{
					emcAcceptor.acceptEMC(EnumFacing.VALUES[i].getOpposite(), s);
				}
			}
		}
	}

	@Override
	public void markDirty()
	{
	}
}