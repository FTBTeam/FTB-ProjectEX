package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.Star;
import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface ProjectEXItems {
	DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectEX.MOD_ID);

	static Supplier<Item> basicItem(String id) {
		return REGISTRY.register(id, () -> new Item(new Item.Properties().tab(ProjectEX.tab)));
	}

	static Supplier<Item> glowingBasicItem(String id) {
		return REGISTRY.register(id, () -> new GlowingItem(new Item.Properties().tab(ProjectEX.tab)));
	}

	static Supplier<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(ProjectEX.tab)));
	}

	Supplier<BlockItem> ENERGY_LINK = blockItem("energy_link", ProjectEXBlocks.ENERGY_LINK);
	Supplier<BlockItem> PERSONAL_LINK = blockItem("personal_link", ProjectEXBlocks.PERSONAL_LINK);
	Supplier<BlockItem> REFINED_LINK = blockItem("refined_link", ProjectEXBlocks.REFINED_LINK);
	Supplier<BlockItem> COMPRESSED_REFINED_LINK = blockItem("compressed_refined_link", ProjectEXBlocks.COMPRESSED_REFINED_LINK);

	Map<Matter, Supplier<BlockItem>> COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_collector", ProjectEXBlocks.COLLECTOR.get(matter)));
		}
	});

	Map<Matter, Supplier<BlockItem>> RELAY = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_relay", ProjectEXBlocks.RELAY.get(matter)));
		}
	});

	Map<Matter, Supplier<BlockItem>> POWER_FLOWER = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_power_flower", ProjectEXBlocks.POWER_FLOWER.get(matter)));
		}
	});

	Supplier<BlockItem> STONE_TABLE = blockItem("stone_table", ProjectEXBlocks.STONE_TABLE);
	Supplier<BlockItem> ALCHEMY_TABLE = blockItem("alchemy_table", ProjectEXBlocks.ALCHEMY_TABLE);

	Map<Star, Supplier<Item>> MAGNUM_STAR = Util.make(new LinkedHashMap<>(), map -> {
		for (Star star : Star.VALUES) {
			map.put(star, basicItem("magnum_star_" + star.name));
		}
	});

	Map<Star, Supplier<Item>> COLOSSAL_STAR = Util.make(new LinkedHashMap<>(), map -> {
		for (Star star : Star.VALUES) {
			map.put(star, basicItem("colossal_star_" + star.name));
		}
	});

	Map<Matter, Supplier<Item>> MATTER = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			if (matter.hasMatterItem) {
				map.put(matter, basicItem(matter.name + "_matter"));
			}
		}
	});

	Supplier<Item> FINAL_STAR_SHARD = basicItem("final_star_shard");
	Supplier<Item> FINAL_STAR = basicItem("final_star");
	Supplier<Item> KNOWLEDGE_SHARING_BOOK = basicItem("knowledge_sharing_book");
	Supplier<Item> ARCANE_TABLET = basicItem("arcane_tablet");

	Map<Matter, Supplier<Item>> COMPRESSED_COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, glowingBasicItem(matter.name + "_compressed_collector"));
		}
	});
}
