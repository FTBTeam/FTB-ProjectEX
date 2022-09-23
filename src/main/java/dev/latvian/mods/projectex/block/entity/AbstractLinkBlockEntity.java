package dev.latvian.mods.projectex.block.entity;

import moze_intel.projecte.api.capabilities.PECapabilities;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.math.BigInteger;
import java.util.UUID;

/**
 * An EMC-linked block; owned by a specific player
 */
public class AbstractLinkBlockEntity extends AbstractEMCBlockEntity {
    private UUID ownerId = Util.NIL_UUID;
    private String ownerName = "";

    public AbstractLinkBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ownerId = tag.getUUID("Owner");
        ownerName = tag.getString("OwnerName");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putUUID("Owner", ownerId);
        tag.putString("OwnerName", ownerName);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("OwnerName", ownerName);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        ownerName = tag.getString("OwnerName");
    }

    @Override
    public void tickServer() {
        if (nonNullLevel().getGameTime() % 20 == 0) {
            // move any locally stored EMC in the block into the player's network, if they're online
            if (storedEMC > 0L) {
                ServerPlayer player = nonNullLevel().getServer().getPlayerList().getPlayer(ownerId);
                if (player != null) {
                    player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                        provider.setEmc(provider.getEmc().add(BigInteger.valueOf(storedEMC)));
                        storedEMC = 0L;
                        setChanged();
                        provider.syncEmc(player);
                    });
                }
            }
        }
    }

    @Override
    public void setChanged() {
        // overridden for efficiency, since link blocks don't have comparator output
        if (level != null) {
            level.blockEntityChanged(worldPosition);
        }
    }

    @Override
    public long getStoredEmc() {
        return storedEMC;
    }

    @Override
    public long getMaximumEmc() {
        return Long.MAX_VALUE;
    }

    @Override
    public long extractEmc(long emc, EmcAction action) {
        long toExtract = Math.min(storedEMC, emc);

        if (toExtract > 0L) {
            storedEMC -= toExtract;
            setChanged();
        }

        return toExtract;
    }

    @Override
    public long insertEmc(long emc, EmcAction action) {
        long toInsert = Math.min(getMaximumEmc() - storedEMC, emc);

        if (toInsert > 0L && action.execute()) {
            storedEMC += toInsert;
            setChanged();
        }

        return toInsert;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwner(LivingEntity entity) {
        ownerId = entity.getUUID();
        ownerName = entity.getScoreboardName();
    }
}
