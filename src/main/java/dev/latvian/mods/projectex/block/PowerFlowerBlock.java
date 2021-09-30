package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.block.entity.PowerFlowerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PowerFlowerBlock extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			box(0, 0, 0, 16, 1, 16),
			box(3.5, 4, 6.5, 12.5, 13, 9.5),
			box(6.5, 1, 6.5, 9.5, 16, 9.5),
			box(6.5, 4, 3.5, 9.5, 13, 12.5),
			box(6.5, 7, 0.5, 9.5, 10, 15.5),
			box(3.5, 7, 3.5, 12.5, 10, 12.5),
			box(0.5, 7, 6.5, 15.5, 10, 9.5)
	);

	public final Matter matter;

	public PowerFlowerBlock(Matter m) {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
		matter = m;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new PowerFlowerBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (entity != null) {
			BlockEntity e = level.getBlockEntity(pos);

			if (e instanceof PowerFlowerBlockEntity) {
				((PowerFlowerBlockEntity) e).owner = entity.getUUID();
			}
		}
	}
}
