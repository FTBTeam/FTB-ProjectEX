package dev.latvian.mods.projectex.block.entity;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.tile.IEmcStorage;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigInteger;
import java.util.UUID;

public class LinkBaseBlockEntity extends BlockEntity implements TickableBlockEntity, IEmcStorage {
	public UUID owner = Util.NIL_UUID;
	public String ownerName = "";
	public int tick = 0;
	public BigInteger storedEMC = BigInteger.ZERO;
	private LazyOptional<IEmcStorage> emcStorageCapability;

	public LinkBaseBlockEntity(BlockEntityType<?> type, int in, int out) {
		super(type);
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		owner = tag.getUUID("Owner");
		ownerName = tag.getString("OwnerName");
		tick = tag.getByte("Tick") & 0xFF;
		String s = tag.getString("StoredEMC");
		storedEMC = s.equals("0") ? BigInteger.ZERO : new BigInteger(s);
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);
		tag.putUUID("Owner", owner);
		tag.putString("OwnerName", ownerName);
		tag.putByte("Tick", (byte) tick);
		tag.putString("StoredEMC", storedEMC.toString());
		return tag;
	}

	@Override
	public void onLoad() {
		if (level != null && level.isClientSide()) {
			level.tickableBlockEntities.remove(this);
		}

		super.onLoad();
	}

	@Override
	public void tick() {
		if (level.isClientSide()) {
			return;
		}

		tick++;

		if (tick >= 20) {
			tick = 0;

			ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
			IKnowledgeProvider provider = player == null ? null : player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).orElse(null);

			if (provider != null && !storedEMC.equals(BigInteger.ZERO)) {
				provider.setEmc(provider.getEmc().add(storedEMC));
				storedEMC = BigInteger.ZERO;
				setChanged();
				provider.syncEmc(player);
			}
		}
	}

	@Override
	public void setChanged() {
		if (level != null) {
			level.blockEntityChanged(worldPosition, this);
		}
	}

	@Override
	public long getStoredEmc() {
		return 0L;
	}

	@Override
	public long getMaximumEmc() {
		return Long.MAX_VALUE;
	}

	@Override
	public long extractEmc(long emc, EmcAction action) {
		return emc < 0L ? insertEmc(-emc, action) : 0L;
	}

	@Override
	public long insertEmc(long emc, EmcAction action) {
		if (emc > 0L) {
			if (action.execute()) {
				storedEMC = storedEMC.add(BigInteger.valueOf(emc));
			}

			return emc;
		}

		return 0L;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ProjectEAPI.EMC_STORAGE_CAPABILITY) {
			if (emcStorageCapability == null || !emcStorageCapability.isPresent()) {
				emcStorageCapability = LazyOptional.of(() -> this);
			}

			return emcStorageCapability.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();

		if (emcStorageCapability != null && emcStorageCapability.isPresent()) {
			emcStorageCapability.invalidate();
			emcStorageCapability = null;
		}
	}
}
