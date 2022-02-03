package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyLinkBlockEntity extends LinkBaseBlockEntity {
	public EnergyLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ProjectEXBlockEntities.ENERGY_LINK.get(), blockPos, blockState);
	}
}
