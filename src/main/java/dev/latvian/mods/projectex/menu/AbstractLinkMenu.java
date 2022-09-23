package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.block.entity.AbstractLinkInvBlockEntity;
import dev.latvian.mods.projectex.inventory.FilterSlot;
import dev.latvian.mods.projectex.offline.PersonalEMC;
import dev.latvian.mods.projectex.util.ProjectEXUtils;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import moze_intel.projecte.config.ProjectEConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class AbstractLinkMenu<T extends AbstractLinkInvBlockEntity> extends AbstractProjectEXMenu<T> {
    public AbstractLinkMenu(MenuType<?> type, int windowId, Inventory invPlayer, BlockPos tilePos) {
        super(type, windowId, invPlayer, tilePos);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();

            long value = ProjectEAPI.getEMCProxy().getValue(stack);
            if (value == 0) return ItemStack.EMPTY;

            ItemStack oldStack = stack.copy();
            player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                ItemStack fixedStack = ProjectEXUtils.fixOutput(stack);
                provider.addKnowledge(fixedStack);
                getBlockEntity().addToOutput(fixedStack);
                long actualEmc = (long) (stack.getCount() * value * ProjectEConfig.server.difficulty.covalenceLoss.get());
                getBlockEntity().insertEmc(actualEmc, IEmcStorage.EmcAction.EXECUTE);
            });

            slot.set(ItemStack.EMPTY);
            return oldStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (player instanceof ServerPlayer serverPlayer && slotId >= 0 && slotId < slots.size()) {
            Slot slot = slots.get(slotId);
            if (slot instanceof FilterSlot filterSlot) {
                switch (clickType) {
                    case QUICK_MOVE -> slot.set(ItemStack.EMPTY);
                    case PICKUP -> {
                        if (!getCarried().isEmpty()) {
                            slot.set(ItemHandlerHelper.copyStackWithSize(getCarried(), 1));
                            IKnowledgeProvider provider = PersonalEMC.getKnowledge(player);
                            if (provider.addKnowledge(getCarried())) {
                                provider.sync(serverPlayer);
                            }
                        } else if (slot.hasItem()) {
                            int amount = button == 0 ? slot.getItem().getMaxStackSize() : 1;
                            ItemStack extracted = filterSlot.remove(amount);
                            ItemHandlerHelper.giveItemToPlayer(player, extracted);
                        }
                    }
                }
                return;
            }
        }
        super.clicked(slotId, button, clickType, player);
    }
}
