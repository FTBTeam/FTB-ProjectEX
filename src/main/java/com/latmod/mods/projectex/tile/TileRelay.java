package com.latmod.mods.projectex.tile;

import com.latmod.mods.projectex.block.EnumTier;
import moze_intel.projecte.api.tile.IEmcAcceptor;
import moze_intel.projecte.api.tile.IEmcProvider;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * @author LatvianModder
 */
public class TileRelay extends TileEntity implements ITickable, IEmcAcceptor, IEmcProvider
{
	public static final IEmcAcceptor[] TEMP = new IEmcAcceptor[6];

	public static int mod(int i, int n)
	{
		i = i % n;
		return i < 0 ? i + n : i;
	}

	public long stored = 0L;
	private boolean isDirty = false;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("stored_emc", stored);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		stored = nbt.getLong("stored_emc");
		super.readFromNBT(nbt);
	}

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
		if (world.isRemote || stored <= 0L || world.getTotalWorldTime() % 20L != mod(hashCode(), 20))
		{
			return;
		}

		int tempSize = 0;

		for (int i = 0; i < 6; i++)
		{
			TEMP[i] = null;
			TileEntity tileEntity = world.getTileEntity(pos.offset(EnumFacing.VALUES[i]));

			if (tileEntity instanceof IEmcAcceptor && !(tileEntity instanceof TileRelay) && !(tileEntity instanceof RelayMK1Tile))
			{
				TEMP[i] = (IEmcAcceptor) tileEntity;
				tempSize++;
			}
		}

		if (tempSize > 0)
		{
			long s = (long) (Math.min(stored / tempSize, Math.min(Long.MAX_VALUE, EnumTier.byMeta(getBlockMetadata()).properties.relay_transfer)));

			for (int i = 0; i < 6; i++)
			{
				if (TEMP[i] != null)
				{
					long a = TEMP[i].acceptEMC(EnumFacing.VALUES[i].getOpposite(), s);

					if (a > 0L)
					{
						stored -= a;
						markDirty();
					}
				}
			}
		}

		if (isDirty)
		{
			isDirty = false;
			world.markChunkDirty(pos, this);
		}
	}

	@Override
	public void markDirty()
	{
		isDirty = true;
	}

	@Override
	public long acceptEMC(EnumFacing facing, long v)
	{
		long v1 = Math.min(getMaximumEmc() - stored, v);

		if (v1 > 0L)
		{
			stored += v1;
			markDirty();
		}

		return v1;
	}

	@Override
	public long provideEMC(EnumFacing facing, long v)
	{
		long v1 = Math.min(stored, v);

		if (v1 > 0L)
		{
			stored -= v1;
			markDirty();
		}

		return v1;
	}

	@Override
	public long getStoredEmc()
	{
		return stored;
	}

	@Override
	public long getMaximumEmc()
	{
		return Long.MAX_VALUE;
	}

	public void addRelayBonus(EnumFacing facing)
	{
		acceptEMC(facing, (long) EnumTier.byMeta(getBlockMetadata()).properties.relay_bonus);
	}
}