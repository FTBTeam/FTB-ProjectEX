package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.block.entity.PowerFlowerBlockEntity;
import moze_intel.projecte.utils.TransmutationEMCFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerFlowerBlock extends AbstractProjectEXBlock {
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

    public PowerFlowerBlock(Matter matter) {
        super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
        this.matter = matter;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PowerFlowerBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(new TranslatableComponent("block.projectex.collector.tooltip").withStyle(ChatFormatting.GRAY));
        list.add(new TranslatableComponent("block.projectex.collector.emc_produced", new TextComponent("")
                .append(TransmutationEMCFormatter.formatEMC(matter.getPowerFlowerOutput())).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
