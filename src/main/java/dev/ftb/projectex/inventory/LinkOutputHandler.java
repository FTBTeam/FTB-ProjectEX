package dev.ftb.projectex.inventory;

import dev.ftb.projectex.block.entity.AbstractLinkInvBlockEntity;
import dev.ftb.projectex.config.ConfigHelper;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import net.minecraft.world.item.ItemStack;
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
        if (owningBlockEntity.getStoredEmc() < value) {
            try {
                knowledgeProvider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owningBlockEntity.getOwnerId());
                if (knowledgeProvider.getEmc().longValue() < value) {
                    return ItemStack.EMPTY;
                }
            } catch (NullPointerException e) {
                // ugly, but it's possible for getKnowledgeProviderFor() to throw an NPE immediately after player death
                return ItemStack.EMPTY;
            }
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
                    knowledgeProvider.setEmc(knowledgeProvider.getEmc().subtract(BigInteger.valueOf(totalValue)));
                } else {
                    // shouldn't get here, but...
                    return ItemStack.EMPTY;
                }
            }
            return toExtract;
        }

        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (owningBlockEntity.getLevel().isClientSide()) {
            return getItemForDisplay(slot);
        }

        validateSlotIndex(slot);

        if (ConfigHelper.server().general.emcLinkMaxOutput.get() <= 0) {
            return ItemStack.EMPTY;
        }

        long value = ProjectEAPI.getEMCProxy().getValue(stacks.get(slot));
        if (value > 0L) {
            try {
                IKnowledgeProvider provider = ProjectEAPI.getTransmutationProxy().getKnowledgeProviderFor(owningBlockEntity.getOwnerId());
                int c = capAmount(provider, value, ConfigHelper.server().general.emcLinkMaxOutput.get());
                if (c > 0) {
                    return ItemHandlerHelper.copyStackWithSize(stacks.get(slot), c);
                }
            } catch (NullPointerException e) {
                // ugly, but it's possible for getKnowledgeProviderFor() to throw an NPE immediately after player death
                // fall through & return an empty stack
            }
            return ItemStack.EMPTY;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getItemForDisplay(int slot) {
        // used to display slot contents in the GUI
        return super.getStackInSlot(slot);
    }

    private int capAmount(@Nullable IKnowledgeProvider knowledgeProvider, long value, long limit) {
        long emc = knowledgeProvider == null ? owningBlockEntity.getStoredEmc() : knowledgeProvider.getEmc().longValue();
        return emc < value ? 0 : (int) (Math.min(limit, emc / value));
    }
}
