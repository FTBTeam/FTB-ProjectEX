package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.block.entity.LinkBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LinkBaseBlock extends BaseEntityBlock
{
	public LinkBaseBlock()
	{
		super(Properties.of(Material.STONE).strength(5F).sound(SoundType.STONE));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof LinkBaseBlockEntity) {
				player.displayClientMessage(new TextComponent(((LinkBaseBlockEntity) blockEntity).ownerName), true);
			}
		}

		return super.use(state, level, pos, player, hand, hit);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if (entity != null) {
			BlockEntity e = level.getBlockEntity(pos);

			if (e instanceof LinkBaseBlockEntity) {
				((LinkBaseBlockEntity) e).owner = entity.getUUID();
				((LinkBaseBlockEntity) e).ownerName = entity.getScoreboardName();
			}
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_)
	{
		return null;
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState)
	{
		return RenderShape.MODEL;
	}
}
