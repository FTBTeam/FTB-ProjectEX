package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CompressedRefinedLinkBlockEntity extends LinkBaseBlockEntity {
	public CompressedRefinedLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ProjectEXBlockEntities.COMPRESSED_REFINED_LINK.get(), blockPos, blockState);
	}
}
