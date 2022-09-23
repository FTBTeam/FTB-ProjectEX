package dev.latvian.mods.projectex.inventory;

import dev.latvian.mods.projectex.block.entity.AbstractLinkInvBlockEntity;
import dev.latvian.mods.projectex.offline.PersonalEMC;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.math.BigInteger;

public class LinkOutputHandler extends BaseItemStackHandler<AbstractLinkInvBlockEntity> {
    public LinkOutputHandler(AbstractLinkInvBlockEntity blockEntity, int outputSize) {
        super(blockEntity, outputSize);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // Items aren't physically extracted from this handler, but instead synthesized out of EMC, either from the
        // block itself, or from the player's own EMC network

        validateSlotIndex(slot);

        ItemStack stack = getStackInSlot(slot);
        if (stack.isEmpty()) return ItemStack.EMPTY;

        long value = ProjectEAPI.getEMCProxy().getValue(stack);
        if (value <= 0L) return ItemStack.EMPTY;

        IKnowledgeProvider knowledgeProvider = null;
        Level level = owningBlockEntity.getLevel();
        if (owningBlockEntity.getStoredEmc() < value
                && ((knowledgeProvider = PersonalEMC.getKnowledge(level, owningBlockEntity.getOwnerId())) == null || knowledgeProvider.getEmc().longValue() < value)) {
            return ItemStack.EMPTY;
        }

        // At this point we know there's either enough EMC in the block, or in the player's personal network
        // to pull at least one of the desired item out. See capAmount() below for actual amount which will be pulled.

        ItemStack toExtract = ItemHandlerHelper.copyStackWithSize(stack, capAmount(knowledgeProvider, value, Math.min(amount, stack.getMaxStackSize())));
        if (!toExtract.isEmpty()) {
            if (!simulate) {
                long totalValue = value * toExtract.getCount();
                if (owningBlockEntity.getStoredEmc() >= totalValue) {
                    owningBlockEntity.extractEmc(totalValue, IEmcStorage.EmcAction.EXECUTE);
                } else if (knowledgeProvider != null) {
                    PersonalEMC.remove(knowledgeProvider, BigInteger.valueOf(totalValue));
                }
            }
            return toExtract;
        }

        return ItemStack.EMPTY;
    }

    private int capAmount(@Nullable IKnowledgeProvider knowledgeProvider, long value, int limit) {
        long emc = knowledgeProvider == null ? owningBlockEntity.getStoredEmc() : knowledgeProvider.getEmc().longValue();

        if (emc < value) {
            return 0;
        }

        return (int) (Math.min(limit, emc / value));
    }
}
