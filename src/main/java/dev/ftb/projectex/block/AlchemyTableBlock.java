package dev.ftb.projectex.block;

import dev.ftb.projectex.block.entity.AlchemyTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class AlchemyTableBlock extends AbstractProjectEXBlock {
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new AlchemyTableBlockEntity(blockPos, state);
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        double x = pos.getX();
        double y = pos.getY() + 1.15D;
        double z = pos.getZ();

        level.addParticle(ParticleTypes.FLAME, x + 2.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
        level.addParticle(ParticleTypes.FLAME, x + 13.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
        level.addParticle(ParticleTypes.FLAME, x + 13.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
        level.addParticle(ParticleTypes.FLAME, x + 2.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
    }
}
