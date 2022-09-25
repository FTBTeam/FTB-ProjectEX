package dev.ftb.projectex.item;

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
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ArcaneTabletItem extends Item {
    public ArcaneTabletItem() {
        super(new Properties().stacksTo(1).tab(ProjectEXItems.ItemGroups.CREATIVE_TAB));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayer) player, new ContainerProvider(hand), (buf) -> buf.writeEnum(hand));
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
        private final InteractionHand hand;

        private ContainerProvider(InteractionHand hand) {
            this.hand = hand;
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory playerInventory, @Nonnull Player player) {
            return new TransmutationContainer(windowId, playerInventory, this.hand, 0);
        }

        @Override
        @Nonnull
        public Component getDisplayName() {
            return PELang.TRANSMUTATION_TRANSMUTE.translate();
        }
    }
}
