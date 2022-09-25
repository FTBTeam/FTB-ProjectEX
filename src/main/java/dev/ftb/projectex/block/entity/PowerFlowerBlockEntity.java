package dev.ftb.projectex.block.entity;

import dev.ftb.projectex.block.PowerFlowerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PowerFlowerBlockEntity extends AbstractLinkBlockEntity {
    public PowerFlowerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ProjectEXBlockEntities.POWER_FLOWER.get(), blockPos, blockState);
    }

    @Override
    public void tickServer() {
        if (nonNullLevel().getGameTime() % 20 == 0 && getBlockState().getBlock() instanceof PowerFlowerBlock pf) {
            storedEMC += pf.matter.getPowerFlowerOutput();
        }

        super.tickServer();
    }
}
