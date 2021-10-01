package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KnowledgeSharingBookItem extends Item {
	public KnowledgeSharingBookItem() {
		super(new Properties().stacksTo(1).tab(ProjectEX.tab));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TextComponent("WIP!").withStyle(ChatFormatting.RED));
	}
}
