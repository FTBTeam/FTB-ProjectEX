package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.block.entity.CollectorBlockEntity;
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

public class CollectorBlock extends BaseEntityBlock
{
	public final Matter matter;

	public CollectorBlock(Matter m) {
		super(Properties.of(Material.STONE).strength(3.5F).sound(SoundType.STONE));
		matter = m;
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return (level1, blockPos, blockState, t) ->
		{
			if(!level1.isClientSide)
			{
				if (t instanceof CollectorBlockEntity tile)
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
		return new CollectorBlockEntity(blockPos, blockState);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TranslatableComponent("block.projectex.collector.tooltip").withStyle(ChatFormatting.GRAY));
		list.add(new TranslatableComponent("block.projectex.collector.emc_produced", new TextComponent("").append(TransmutationEMCFormatter.formatEMC(matter.collectorOutput)).withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public RenderShape getRenderShape(BlockState blockState)
	{
		return RenderShape.MODEL;
	}
}
