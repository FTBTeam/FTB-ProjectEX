package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyTableEntity extends BlockEntity {
	public AlchemyTableEntity(BlockPos blockPos, BlockState blockState) {
		super(ProjectEXBlockEntities.ALCHEMY_TABLE.get(), blockPos, blockState);
	}
}
