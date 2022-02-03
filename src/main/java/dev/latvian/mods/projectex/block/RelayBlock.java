package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.block.entity.RelayBlockEntity;
import moze_intel.projecte.utils.TransmutationEMCFormatter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RelayBlock extends BaseEntityBlock
{
	public final Matter matter;

	public RelayBlock(Matter m) {
		super(Properties.of(Material.STONE).strength(5F).sound(SoundType.STONE));
		matter = m;
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type)
	{
		return (level1, blockPos, blockState, t) ->
		{
			if(!level.isClientSide)
			{
				if (t instanceof RelayBlockEntity tile)
				{
					tile.tick();
				}
			}
		};
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
	{
		return new RelayBlockEntity(blockPos, blockState);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TranslatableComponent("block.projectex.relay.tooltip").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("block.projectex.relay.relay_bonus", new TextComponent("").append(TransmutationEMCFormatter.formatEMC(matter.relayBonus)).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("block.projectex.relay.max_transfer", new TextComponent("").append(TransmutationEMCFormatter.formatEMC(matter.relayTransfer)).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState)
	{
		return RenderShape.MODEL;
	}
}
