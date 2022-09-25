package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.inventory.LinkInputHandler;
import dev.latvian.mods.projectex.inventory.LinkOutputHandler;
import dev.latvian.mods.projectex.util.ProjectEXUtils;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.config.ProjectEConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An EMC-linked block entity with 1 or more input slots and 1 or more output (filter) slots
 */
public abstract class AbstractLinkInvBlockEntity extends AbstractLinkBlockEntity {
    private final LinkInputHandler inputHandler;
    private final LinkOutputHandler outputHandler;
    private final LazyOptional<WrappedItemHandler> itemCap;

    public AbstractLinkInvBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState, int inputSize, int outputSize) {
        super(type, blockPos, blockState);

        inputHandler = new LinkInputHandler(this, inputSize);
        outputHandler = new LinkOutputHandler(this, outputSize);
        itemCap = LazyOptional.of(() -> new WrappedItemHandler(inputHandler, outputHandler));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        inputHandler.deserializeNBT(tag.getCompound("Input"));
        outputHandler.deserializeNBT(tag.getCompound("Output"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Input", inputHandler.serializeNBT());
        tag.put("Output", outputHandler.serializeNBT());
    }

    @Override
    public void tickServer() {
        // scan items in the input inv and convert to EMC, adding to the block's EMC store

        ServerPlayer player = nonNullLevel().getServer().getPlayerList().getPlayer(getOwnerId());
        LazyOptional<IKnowledgeProvider> knowledgeProvider = player == null ? LazyOptional.empty() : player.getCapability(PECapabilities.KNOWLEDGE_CAPABILITY);
        boolean syncKnowledge = false;
        boolean changeDone = false;

        for (int i = 0; i < inputHandler.getSlots(); i++) {
            ItemStack stack = inputHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                long value = ProjectEAPI.getEMCProxy().getValue(stack);
                if (value > 0L) {
                    syncKnowledge = learnItems() && knowledgeProvider.map(p -> p.addKnowledge(ProjectEXUtils.fixOutput(stack))).orElse(false);
                    long actual = (long) (stack.getCount() * value * ProjectEConfig.server.difficulty.covalenceLoss.get());
                    storedEMC += actual;
                    inputHandler.setStackInSlot(i, ItemStack.EMPTY);
                    changeDone = true;
                }
            }
        }
        if (changeDone) setChanged();
        if (player != null && syncKnowledge) knowledgeProvider.ifPresent(p -> p.sync(player));

        // super logic moves local stored EMC into player's EMC if possible
        super.tickServer();
    }

    protected boolean learnItems() {
        return false;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        itemCap.invalidate();
    }

    public boolean hasOwner() {
        return getOwnerId().getLeastSignificantBits() != 0L && getOwnerId().getMostSignificantBits() != 0L;
    }

    public IItemHandler getInputHandler() {
        return inputHandler;
    }

    public IItemHandler getOutputHandler() {
        return outputHandler;
    }

    /**
     * Add an item to the output (filter) handler, iff that item isn't already there
     *
     * @param stack the item to add
     */
    public void addToOutput(ItemStack stack) {
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            if (ItemStack.isSame(outputHandler.getStackInSlot(i), stack)) {
                return;
            }
        }
        for (int i = 0; i < outputHandler.getSlots(); i++) {
            if (outputHandler.getStackInSlot(i).isEmpty()) {
                outputHandler.setStackInSlot(i, stack);
                return;
            }
        }
    }

    public NonNullList<ItemStack> getContentsToDrop() {
        NonNullList<ItemStack> res = NonNullList.create();
        for (int i = 0; i < inputHandler.getSlots(); i++) {
            ItemStack stack = inputHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                res.add(stack);
            }
        }
        return res;
    }

    /**
     * Wrapped item handler for capability exposure (i.e. piped access). Access via GUI handled via slots connected
     * to real input & output handlers.
     */
    private record WrappedItemHandler(LinkInputHandler inputHandler, LinkOutputHandler outputHandler)
            implements IItemHandler {
        @Override
        public int getSlots() {
            return inputHandler.getSlots() + outputHandler.getSlots();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            if (isInput(slot)) {
                return inputHandler.getStackInSlot(slot);
            } else if (isOutput(slot)) {
                return outputHandler.getStackInSlot(slot - inputHandler.getSlots());
            } else {
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
            }
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return isInput(slot) ? inputHandler.insertItem(slot, stack, simulate) : stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int count, boolean simulate) {
            return isOutput(slot) ? outputHandler.extractItem(slot - inputHandler.getSlots(), count, simulate) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            if (isInput(slot)) {
                return inputHandler.getSlotLimit(slot);
            } else if (isOutput(slot)) {
                return outputHandler.getSlotLimit(slot - inputHandler.getSlots());
            } else {
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (isInput(slot)) {
                return inputHandler.isItemValid(slot, stack);
            } else if (isOutput(slot)) {
                return outputHandler.isItemValid(slot - inputHandler.getSlots(), stack);
            } else {
                throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
            }
        }

        private boolean isInput(int slot) {
            return slot >= 0 && slot < inputHandler.getSlots();
        }

        private boolean isOutput(int slot) {
            return slot >= inputHandler.getSlots() && slot < getSlots();
        }
    }
}
