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
	public int tick = 0;
	public BigInteger storedEMC = BigInteger.valueOf(0L);

	public PowerFlowerBlockEntity() {
		super(ProjectEXBlockEntities.POWER_FLOWER.get());
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		owner = tag.getUUID("Owner");
		tick = tag.getByte("Tick") & 0xFF;
		storedEMC = new BigInteger(tag.getString("StoredEMC"));
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);
		tag.putUUID("Owner", owner);
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
		tick++;

		if (tick >= 20) {
			tick = 0;

			BlockState state = getBlockState();

			if (state.getBlock() instanceof PowerFlowerBlock) {
				storedEMC = storedEMC.add(BigInteger.valueOf(((PowerFlowerBlock) state.getBlock()).matter.getPowerFlowerOutput()));

				ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner);
				IKnowledgeProvider provider = player == null ? null : player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).orElse(null);

				if (provider != null) {
					provider.setEmc(provider.getEmc().add(storedEMC));
					storedEMC = BigInteger.valueOf(0L);
					provider.syncEmc(player);
				}

				level.blockEntityChanged(worldPosition, this);
			}
		}
	}
}
