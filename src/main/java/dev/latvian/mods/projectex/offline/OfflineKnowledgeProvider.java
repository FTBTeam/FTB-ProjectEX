package dev.latvian.mods.projectex.offline;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.emc.EMCMappingHandler;
import moze_intel.projecte.gameObjs.items.Tome;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

/**
 * @author LatvianModder
 * @author desht
 */
public class OfflineKnowledgeProvider implements IKnowledgeProvider {
    public final UUID playerId;
    private final Set<ItemInfo> knowledge;
    private final ItemStackHandler inputLocks;
    private BigInteger emc;
    private boolean fullKnowledge;
    public boolean shouldSave;

    public static void copy(IKnowledgeProvider from, IKnowledgeProvider to) {
        to.deserializeNBT(from.serializeNBT());
    }

    public OfflineKnowledgeProvider(UUID id) {
        playerId = id;
        knowledge = new HashSet<>();
        inputLocks = new ItemStackHandler(9);
        emc = BigInteger.ZERO;
        fullKnowledge = false;
        shouldSave = false;
    }

    public static OfflineKnowledgeProvider fromNBT(UUID id, CompoundTag tag) {
        OfflineKnowledgeProvider p = new OfflineKnowledgeProvider(id);
        p.deserializeNBT(tag);
        return p;
    }

    @Override
    public boolean hasFullKnowledge() {
        return fullKnowledge;
    }

    @Override
    public void setFullKnowledge(boolean b) {
        fullKnowledge = b;
        shouldSave = true;
    }

    @Override
    public void clearKnowledge() {
        knowledge.clear();
        fullKnowledge = false;
        shouldSave = true;
    }

    @Override
    public boolean hasKnowledge(ItemInfo stack) {
        if (fullKnowledge) {
            return true;
        } else {
            return knowledge.contains(stack);
        }
    }

    @Override
    public boolean addKnowledge(ItemInfo stack) {
        if (fullKnowledge) {
            return false;
        }
        if (knowledge.add(stack)) {
            if (stack.getItem() instanceof Tome) {
                fullKnowledge = true;
            }
            shouldSave = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeKnowledge(ItemInfo stack) {
        boolean removed = false;
        if (stack.getItem() instanceof Tome) {
            fullKnowledge = false;
            removed = true;
        }

        if (fullKnowledge) {
            return false;
        }

        if (knowledge.remove(stack)) {
            shouldSave = true;
            return true;
        }

        return removed;
    }

    @Override
    public Set<ItemInfo> getKnowledge() {
        if (this.fullKnowledge) {
            Set<ItemInfo> allKnowledge = EMCMappingHandler.getMappedItems();
            allKnowledge.addAll(this.knowledge);
            return Collections.unmodifiableSet(allKnowledge);
        } else {
            return Collections.unmodifiableSet(this.knowledge);
        }
    }

    @Override
    public IItemHandlerModifiable getInputAndLocks() {
        return inputLocks;
    }

    @Override
    public BigInteger getEmc() {
        return emc;
    }

    @Override
    public void setEmc(BigInteger e) {
        emc = e;
        shouldSave = true;
    }

    @Override
    public void sync(ServerPlayer player) {
    }

    @Override
    public void syncEmc(@NotNull ServerPlayer serverPlayer) {
    }

    @Override
    public void syncKnowledgeChange(@NotNull ServerPlayer serverPlayer, ItemInfo itemInfo, boolean b) {
    }

    @Override
    public void syncInputAndLocks(@NotNull ServerPlayer serverPlayer, List<Integer> list, TargetUpdateType targetUpdateType) {
    }

    @Override
    public void receiveInputsAndLocks(Map<Integer, ItemStack> map) {
        int slots = this.inputLocks.getSlots();
        map.forEach((slot, stack) -> {
            if (slot >= 0 && slot < slots) {
                this.inputLocks.setStackInSlot(slot, stack);
            }
        });
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("transmutationEmc", emc.toString());
        ListTag knowledgeWrite = new ListTag();
        for (ItemInfo is : knowledge) {
            knowledgeWrite.add(is.write(new CompoundTag()));
        }
        nbt.put("knowledge", knowledgeWrite);
        nbt.put("inputlock", inputLocks.serializeNBT());
        nbt.putBoolean("fullknowledge", fullKnowledge);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        emc = new BigInteger(nbt.getString("transmutationEmc"));

        ListTag list = nbt.getList("knowledge", 10);

        for (int i = 0; i < list.size(); i++) {
            knowledge.add(ItemInfo.read(list.getCompound(i)));
        }

        pruneStaleKnowledge();

        for (int i = 0; i < inputLocks.getSlots(); ++i) {
            inputLocks.setStackInSlot(i, ItemStack.EMPTY);
        }

        inputLocks.deserializeNBT(nbt.getCompound("inputlock"));
        fullKnowledge = nbt.getBoolean("fullknowledge");
    }

    private void pruneStaleKnowledge() {
        knowledge.removeIf((stack) -> !EMCHelper.doesItemHaveEmc(stack));
    }
}