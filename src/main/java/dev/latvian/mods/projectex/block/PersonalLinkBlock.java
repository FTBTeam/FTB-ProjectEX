package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.block.entity.PersonalLinkBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PersonalLinkBlock extends LinkBaseBlock {
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new PersonalLinkBlockEntity();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TranslatableComponent("block.projectex.personal_link.tooltip").withStyle(ChatFormatting.GRAY));
		list.add(new TextComponent("WIP!").withStyle(ChatFormatting.RED));
	}
}
