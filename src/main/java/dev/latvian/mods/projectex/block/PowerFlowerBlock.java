package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.block.entity.PowerFlowerBlockEntity;
import moze_intel.projecte.utils.TransmutationEMCFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof PowerFlowerBlockEntity) {
				player.displayClientMessage(new TextComponent(((PowerFlowerBlockEntity) blockEntity).ownerName), true);
			}
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (entity != null) {
			BlockEntity e = level.getBlockEntity(pos);

			if (e instanceof PowerFlowerBlockEntity) {
				((PowerFlowerBlockEntity) e).owner = entity.getUUID();
				((PowerFlowerBlockEntity) e).ownerName = entity.getScoreboardName();
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TranslatableComponent("block.projectex.collector.tooltip").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("block.projectex.collector.emc_produced", new TextComponent("").append(TransmutationEMCFormatter.formatEMC(matter.getPowerFlowerOutput())).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
	}
}
