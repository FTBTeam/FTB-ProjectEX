package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PersonalLinkBlockEntity extends LinkBaseBlockEntity {
	public PersonalLinkBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ProjectEXBlockEntities.PERSONAL_LINK.get(), blockPos, blockState);
	}
}
