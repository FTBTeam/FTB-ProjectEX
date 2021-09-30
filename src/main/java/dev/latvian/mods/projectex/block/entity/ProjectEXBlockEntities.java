package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public interface ProjectEXBlockEntities {
	DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ProjectEX.MOD_ID);

	Supplier<BlockEntityType<?>> ENERGY_LINK = REGISTRY.register("energy_link", () -> BlockEntityType.Builder.of(EnergyLinkBlockEntity::new, ProjectEXBlocks.ENERGY_LINK.get()).build(null));
	Supplier<BlockEntityType<?>> PERSONAL_LINK = REGISTRY.register("personal_link", () -> BlockEntityType.Builder.of(PersonalLinkBlockEntity::new, ProjectEXBlocks.PERSONAL_LINK.get()).build(null));
	Supplier<BlockEntityType<?>> REFINED_LINK = REGISTRY.register("refined_link", () -> BlockEntityType.Builder.of(RefinedLinkBlockEntity::new, ProjectEXBlocks.REFINED_LINK.get()).build(null));
	Supplier<BlockEntityType<?>> COMPRESSED_REFINED_LINK = REGISTRY.register("compressed_refined_link", () -> BlockEntityType.Builder.of(CompressedRefinedLinkBlockEntity::new, ProjectEXBlocks.COMPRESSED_REFINED_LINK.get()).build(null));
	Supplier<BlockEntityType<?>> COLLECTOR = REGISTRY.register("collector", () -> BlockEntityType.Builder.of(CollectorBlockEntity::new, ProjectEXBlocks.COLLECTOR.values().stream().map(Supplier::get).toArray(Block[]::new)).build(null));
	Supplier<BlockEntityType<?>> RELAY = REGISTRY.register("relay", () -> BlockEntityType.Builder.of(RelayBlockEntity::new, ProjectEXBlocks.RELAY.values().stream().map(Supplier::get).toArray(Block[]::new)).build(null));
	Supplier<BlockEntityType<?>> POWER_FLOWER = REGISTRY.register("power_flower", () -> BlockEntityType.Builder.of(PowerFlowerBlockEntity::new, ProjectEXBlocks.POWER_FLOWER.values().stream().map(Supplier::get).toArray(Block[]::new)).build(null));
	Supplier<BlockEntityType<?>> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", () -> BlockEntityType.Builder.of(AlchemyTableEntity::new, ProjectEXBlocks.ALCHEMY_TABLE.get()).build(null));
}
