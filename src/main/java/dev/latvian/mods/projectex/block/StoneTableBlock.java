package dev.latvian.mods.projectex.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StoneTableBlock extends Block {
	public static final VoxelShape[] SHAPE = new VoxelShape[]{
			box(0, 0, 0, 16, 4, 16),
			box(0, 12, 0, 16, 16, 16),
			box(0, 0, 0, 16, 16, 4),
			box(0, 0, 12, 16, 16, 16),
			box(0, 0, 0, 4, 16, 16),
			box(12, 0, 0, 16, 16, 16)
	};

	public StoneTableBlock() {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
		registerDefaultState(getStateDefinition().any().setValue(BlockStateProperties.FACING, Direction.DOWN));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
		arg.add(BlockStateProperties.FACING);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE[state.getValue(BlockStateProperties.FACING).ordinal()];
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return defaultBlockState().setValue(BlockStateProperties.FACING, ctx.getClickedFace().getOpposite());
	}
}
