package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.ProjectEX;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

public class FinalStarItem extends Item {
	public FinalStarItem() {
		super(new Properties().stacksTo(1).tab(ProjectEX.tab));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TranslatableComponent("item.projectex.final_star.tooltip").withStyle(ChatFormatting.GRAY));
		list.add(new TextComponent("WIP!").withStyle(ChatFormatting.RED));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		if (!level.isClientSide() && player.isCrouching()) {
			IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).orElse(null);
			provider.setEmc(BigInteger.ZERO);
			provider.syncEmc((ServerPlayer) player);
			return InteractionResultHolder.success(player.getItemInHand(hand));
		}

		return super.use(level, player, hand);
	}
}
