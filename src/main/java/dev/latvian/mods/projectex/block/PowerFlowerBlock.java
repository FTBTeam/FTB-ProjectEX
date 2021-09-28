package dev.latvian.mods.projectex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PowerFlowerBlock extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 0, 16, 1, 16)
			// TODO: Finish box
	);

	public PowerFlowerBlock() {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}
}
