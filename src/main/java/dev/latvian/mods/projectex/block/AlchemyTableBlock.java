package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.block.entity.AlchemyTableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AlchemyTableBlock extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			box(0, 9, 0, 16, 13, 16),
			box(2, 0, 2, 4, 9, 4),
			box(2, 0, 12, 4, 9, 14),
			box(12, 0, 12, 14, 9, 14),
			box(12, 0, 2, 14, 9, 4)
	);

	public AlchemyTableBlock() {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new AlchemyTableEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}
}
