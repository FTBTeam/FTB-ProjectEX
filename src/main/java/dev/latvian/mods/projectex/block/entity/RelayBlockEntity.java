package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.RelayBlock;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RelayBlockEntity extends AbstractEMCBlockEntity {
    public RelayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.RELAY.get(), blockPos, blockState);
    }

    @Override
    public void tickServer() {
        if (storedEMC <= 0L) {
            return;
        }

        Level level = nonNullLevel();
        if (level.getGameTime() % 20 == 0) {
            BlockState state = getBlockState();

            if (state.getBlock() instanceof RelayBlock relay) {
                long relayTransfer = relay.matter.relayTransfer;

                List<IEmcStorage> validTargets = new ArrayList<>(1);

                for (Direction direction : ProjectEX.DIRECTIONS) {
                    BlockEntity tileEntity = level.getBlockEntity(worldPosition.relative(direction));
                    if (tileEntity != null) {
                        tileEntity.getCapability(PECapabilities.EMC_STORAGE_CAPABILITY).ifPresent(storage -> {
                            if (!storage.isRelay() && storage.insertEmc(1L, EmcAction.SIMULATE) > 0L) {
                                validTargets.add(storage);
                            }
                        });
                    }
                }

                if (!validTargets.isEmpty() && storedEMC >= validTargets.size()) {
                    long perTarget = Math.min(storedEMC / validTargets.size(), relayTransfer);
                    for (IEmcStorage storage : validTargets) {
                        long inserted = storage.insertEmc(perTarget, EmcAction.EXECUTE);
                        if (inserted > 0L) {
                            storedEMC -= inserted;
                            setChanged();
                            if (storedEMC < perTarget) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isRelay() {
        return true;
    }

    public void addBonus() {
        if (getBlockState().getBlock() instanceof RelayBlock relayBlock) {
            insertEmc(relayBlock.matter.relayBonus, EmcAction.EXECUTE);
        }
    }
}
