package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.CollectorBlock;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import moze_intel.projecte.gameObjs.block_entities.RelayMK1BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends AbstractEMCBlockEntity {
    private LazyOptional<IEmcStorage> emcStorageCapability;

    public CollectorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.COLLECTOR.get(), blockPos, blockState);
    }

    @Override
    public void tickServer() {
        Level level = nonNullLevel();
        if (level.getGameTime() % 20 == 0) {
            BlockState state = getBlockState();

            if (state.getBlock() instanceof CollectorBlock collector) {
                storedEMC += collector.matter.collectorOutput;

                List<IEmcStorage> validTargets = findValidTargets(level);
                if (!validTargets.isEmpty() && storedEMC >= validTargets.size()) {
                    long perTarget = storedEMC / validTargets.size();
                    for (IEmcStorage target : validTargets) {
                        long a = target.insertEmc(perTarget, EmcAction.EXECUTE);
                        if (a > 0L) {
                            storedEMC -= a;
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

    @NotNull
    private List<IEmcStorage> findValidTargets(Level level) {
        List<IEmcStorage> validTargets = new ArrayList<>(6);
        for (Direction direction : ProjectEX.DIRECTIONS) {
            BlockEntity blockEntity = level.getBlockEntity(worldPosition.relative(direction));
            if (blockEntity != null) {
                blockEntity.getCapability(PECapabilities.EMC_STORAGE_CAPABILITY).ifPresent(storage -> {
                    validTargets.add(storage);
                    if (blockEntity instanceof RelayMK1BlockEntity relay) {
                        for (int i = 0; i < 20; i++) {
                            relay.addBonus();
                        }
                        blockEntity.setChanged();
                    } else if (blockEntity instanceof RelayBlockEntity relay) {
                        relay.addBonus();
                        blockEntity.setChanged();
                    }
                });
            }
        }
        return validTargets;
    }
}
