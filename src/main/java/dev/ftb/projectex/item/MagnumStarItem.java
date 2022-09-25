package dev.ftb.projectex.item;

import dev.ftb.projectex.Star;
import moze_intel.projecte.api.capabilities.block_entity.IEmcStorage;
import moze_intel.projecte.api.capabilities.item.IItemEmcHolder;
import moze_intel.projecte.capability.EmcHolderItemCapabilityWrapper;
import moze_intel.projecte.gameObjs.items.IBarHelper;
import moze_intel.projecte.gameObjs.items.ItemPE;
import moze_intel.projecte.integration.IntegrationHelper;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class MagnumStarItem extends ItemPE implements IItemEmcHolder, IBarHelper {
    public static final long[] STAR_EMC = new long[12];

    static {
        long emc = 204800000L;

        for (int i = 0; i < STAR_EMC.length; i++) {
            STAR_EMC[i] = emc;
            emc *= 4L;
        }
    }

    public final Star tier;

    public MagnumStarItem(Star t) {
        super(new Properties().stacksTo(1).tab(ProjectEXItems.ItemGroups.CREATIVE_TAB));
        tier = t;
        addItemCapability(EmcHolderItemCapabilityWrapper::new);
        addItemCapability("curios", IntegrationHelper.CURIO_CAP_SUPPLIER);
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public float getWidthForBar(ItemStack stack) {
        long starEmc = getStoredEmc(stack);
        return (float) (starEmc == 0L ? 1D : 1D - (double) starEmc / (double) getMaximumEmc(stack));
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        return this.getScaledBarWidth(stack);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return this.getColorForBar(stack);
    }

    @Override
    public long insertEmc(@Nonnull ItemStack stack, long toInsert, IEmcStorage.EmcAction action) {
        if (toInsert < 0L) {
            return this.extractEmc(stack, -toInsert, action);
        } else {
            long toAdd = Math.min(this.getNeededEmc(stack), toInsert);
            if (action.execute()) {
                addEmcToStack(stack, toAdd);
            }

            return toAdd;
        }
    }

    @Override
    public long extractEmc(@Nonnull ItemStack stack, long toExtract, IEmcStorage.EmcAction action) {
        if (toExtract < 0L) {
            return this.insertEmc(stack, -toExtract, action);
        } else {
            long storedEmc = this.getStoredEmc(stack);
            long toRemove = Math.min(storedEmc, toExtract);
            if (action.execute()) {
                setEmc(stack, storedEmc - toRemove);
            }

            return toRemove;
        }
    }

    @Override
    public long getStoredEmc(@Nonnull ItemStack stack) {
        return getEmc(stack);
    }

    @Override
    public long getMaximumEmc(@Nonnull ItemStack stack) {
        return STAR_EMC[tier.ordinal()];
    }
}
