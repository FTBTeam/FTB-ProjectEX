package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RefinedLinkBlockEntity extends LinkBaseBlockEntity
{
	public RefinedLinkBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		super(ProjectEXBlockEntities.REFINED_LINK.get(), blockPos, blockState);
	}
}
