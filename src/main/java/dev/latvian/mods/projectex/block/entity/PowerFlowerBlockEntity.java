package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.block.PowerFlowerBlock;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.math.BigInteger;
import java.util.UUID;

public class PowerFlowerBlockEntity extends BlockEntity implements TickableBlockEntity {
	public UUID owner = Util.NIL_UUID;
	public String ownerName = "";
	public int tick = 0;
	public BigInteger storedEMC = BigInteger.ZERO;

	public PowerFlowerBlockEntity() {
		super(ProjectEXBlockEntities.POWER_FLOWER.get());
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

			BlockState state = getBlockState();

			if (state.getBlock() instanceof PowerFlowerBlock) {
				long gen = ((PowerFlowerBlock) state.getBlock()).matter.getPowerFlowerOutput();

				ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
				IKnowledgeProvider provider = player == null ? null : player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).orElse(null);

				if (provider != null) {
					provider.setEmc(provider.getEmc().add(BigInteger.valueOf(gen)));

					if (!storedEMC.equals(BigInteger.ZERO)) {
						provider.setEmc(provider.getEmc().add(storedEMC));
						storedEMC = BigInteger.ZERO;
						setChanged();
					}

					provider.syncEmc(player);
				} else {
					storedEMC = storedEMC.add(BigInteger.valueOf(gen));
					setChanged();
				}
			}
		}
	}

	@Override
	public void setChanged() {
		if (level != null) {
			level.blockEntityChanged(worldPosition, this);
		}
	}
}
