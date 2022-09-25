package dev.ftb.projectex.datagen;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import dev.ftb.projectex.block.ProjectEXBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return Lists.newArrayList(Pair.of(BlockLootTables::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext tracker) {
        map.forEach((k, v) -> LootTables.validate(tracker, k, v));
    }

    public static class BlockLootTables extends BlockLoot {
        private final Set<Block> blocks = new HashSet<>();

        @Override
        protected void addTables() {
            dropSelf(ProjectEXBlocks.ENERGY_LINK.get());
            dropSelf(ProjectEXBlocks.PERSONAL_LINK.get());
            dropSelf(ProjectEXBlocks.REFINED_LINK.get());
            dropSelf(ProjectEXBlocks.COMPRESSED_REFINED_LINK.get());
            ProjectEXBlocks.COLLECTOR.forEach((k, v) -> dropSelf(v.get()));
            ProjectEXBlocks.RELAY.forEach((k, v) -> dropSelf(v.get()));
            ProjectEXBlocks.POWER_FLOWER.forEach((k, v) -> dropSelf(v.get()));
            dropSelf(ProjectEXBlocks.STONE_TABLE.get());
            dropSelf(ProjectEXBlocks.ALCHEMY_TABLE.get());
        }

        @Override
        protected void add(Block blockIn, LootTable.Builder table) {
            super.add(blockIn, table);
            this.blocks.add(blockIn);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return this.blocks;
        }
    }
}
