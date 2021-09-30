package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface ProjectEXBlocks {
	DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectEX.MOD_ID);

	Supplier<Block> ENERGY_LINK = REGISTRY.register("energy_link", EnergyLinkBlock::new);
	Supplier<Block> PERSONAL_LINK = REGISTRY.register("personal_link", PersonalLinkBlock::new);
	Supplier<Block> REFINED_LINK = REGISTRY.register("refined_link", RefinedLinkBlock::new);
	Supplier<Block> COMPRESSED_REFINED_LINK = REGISTRY.register("compressed_refined_link", CompressedRefinedLinkBlock::new);

	Map<Matter, Supplier<Block>> COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, REGISTRY.register(matter.name + "_collector", () -> new CollectorBlock(matter)));
		}
	});

	Map<Matter, Supplier<Block>> RELAY = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, REGISTRY.register(matter.name + "_relay", () -> new RelayBlock(matter)));
		}
	});

	Map<Matter, Supplier<Block>> POWER_FLOWER = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, REGISTRY.register(matter.name + "_power_flower", () -> new PowerFlowerBlock(matter)));
		}
	});

	Supplier<Block> STONE_TABLE = REGISTRY.register("stone_table", StoneTableBlock::new);
	Supplier<Block> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", AlchemyTableBlock::new);
}
