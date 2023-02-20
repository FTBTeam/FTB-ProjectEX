package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.ProjectEX;
import moze_intel.projecte.gameObjs.container.TransmutationContainer;
import moze_intel.projecte.utils.text.PELang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ArcaneTabletItem extends Item {
	public ArcaneTabletItem() {
		super(new Properties().stacksTo(1).tab(ProjectEX.tab));
	}

	@Override
	@Nonnull
	public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
		if (!world.isClientSide) {
			NetworkHooks.openGui((ServerPlayer) player, new ContainerProvider(), b -> {
				b.writeBoolean(true);
				b.writeEnum(hand);
				b.writeByte(player.inventory.selected);
			});
		}

		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, level, list, flag);
		list.add(new TextComponent("WIP!").withStyle(ChatFormatting.RED));
	}

	private static class ContainerProvider implements MenuProvider {

		private ContainerProvider() {
		}

		@Override
		public AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory playerInventory, @Nonnull Player player) {
			return new TransmutationContainer(windowId, playerInventory);
		}

		@Override
		@Nonnull
		public Component getDisplayName() {
			return PELang.TRANSMUTATION_TRANSMUTE.translate();
		}
	}
}
