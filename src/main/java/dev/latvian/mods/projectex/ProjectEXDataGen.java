package dev.latvian.mods.projectex;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import dev.latvian.mods.projectex.item.ProjectEXItems;
import moze_intel.projecte.gameObjs.registries.PEItems;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProjectEXDataGen {
	public static final String MODID = ProjectEX.MOD_ID;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();

		if (event.includeClient()) {
			gen.addProvider(new JMLang(gen, MODID, "en_us"));
			gen.addProvider(new JMBlockStates(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMBlockModels(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMItemModels(gen, MODID, event.getExistingFileHelper()));
		}

		if (event.includeServer()) {
			JMBlockTags blockTags = new JMBlockTags(gen, MODID, efh);
			gen.addProvider(blockTags);
			gen.addProvider(new JMItemTags(gen, blockTags, MODID, efh));
			gen.addProvider(new JMRecipes(gen));
			gen.addProvider(new JMLootTableProvider(gen));
		}
	}

	private static class JMLang extends LanguageProvider {
		public JMLang(DataGenerator gen, String modid, String locale) {
			super(gen, modid, locale);
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.projectex", "ProjectEX");
			addBlock(ProjectEXBlocks.ENERGY_LINK, "Energy EMC Link");
			addBlock(ProjectEXBlocks.PERSONAL_LINK, "Personal EMC Link");
			addBlock(ProjectEXBlocks.REFINED_LINK, "Refined EMC Link");
			addBlock(ProjectEXBlocks.COMPRESSED_REFINED_LINK, "Compressed Refined EMC Link");

			for (Matter matter : Matter.VALUES) {
				if (matter.hasMatterItem) {
					addItem(matter.getItem(), matter.displayName + " Matter");
				}

				addBlock(ProjectEXBlocks.COLLECTOR.get(matter), matter.displayName + " Energy Collector " + matter.getMK());
				addBlock(ProjectEXBlocks.RELAY.get(matter), matter.displayName + " Anti-Matter Relay " + matter.getMK());
				addItem(ProjectEXItems.COMPRESSED_COLLECTOR.get(matter), matter.displayName + " Compressed Energy Collector " + matter.getMK());
				addBlock(ProjectEXBlocks.POWER_FLOWER.get(matter), matter.displayName + " Power Flower " + matter.getMK());
			}

			addBlock(ProjectEXBlocks.STONE_TABLE, "Stone Transmutation Table");
			addBlock(ProjectEXBlocks.ALCHEMY_TABLE, "Alchemy Table");

			for (Star star : Star.VALUES) {
				addItem(ProjectEXItems.MAGNUM_STAR.get(star), "Magnum Star " + star.displayName);
				addItem(ProjectEXItems.COLOSSAL_STAR.get(star), "Colossal Star " + star.displayName);
			}

			addItem(ProjectEXItems.FINAL_STAR_SHARD, "Final Star Shard");
			addItem(ProjectEXItems.FINAL_STAR, "The Final Star");
			addItem(ProjectEXItems.KNOWLEDGE_SHARING_BOOK, "Knowledge Sharing Book");
			addItem(ProjectEXItems.ARCANE_TABLET, "Arcane Transmutation Tablet");

			add("block.projectex.energy_link.tooltip", "You can use this block to add EMC to your Transmutation Table using Collectors.");
			add("block.projectex.personal_link.tooltip", "Same as Basic Energy EMC Link, but also allows to import and export items.");
			add("block.projectex.refined_link.tooltip", "Same as Personal EMC Link, but has 1 input slot and 9 output slots. Designed to be used with Refined Storage-like systems. It also learns items from input slots.");
			add("block.projectex.compressed_refined_link.tooltip", "Same as Refined EMC Link, but has 54 output slots.");

			add("block.projectex.collector.tooltip", "Server TPS friendly. Generates EMC only once a second.");
			add("block.projectex.collector.emc_produced", "Produced EMC: %s/s");

			add("block.projectex.relay.tooltip", "Server TPS friendly. Transfers EMC only once a second.");
			add("block.projectex.relay.max_transfer", "Max EMC Transfer: %s/s");
			add("block.projectex.relay.relay_bonus", "Relay Bonus: %s/s");

			add("block.projectex.stone_table.learn", "Learn");
			add("block.projectex.stone_table.unlearn", "Unlearn");

			add("item.projectex.final_star.tooltip", "Place a chest next to Pedestal and drop item on top of it to clone it infinitely.");

			add("gui.stone_table.cant_use", "Can't use this item in Stone Table!");
			add("gui.arcane_tablet.rotate", "Rotate");
			add("gui.arcane_tablet.balance", "Balance / Spread");
			add("gui.arcane_tablet.clear", "Clear");

			/*
			projectex=Project EX Common
			projectex.general.enable_stone_table_whitelist=Enable Stone Table Whitelist
			projectex.general.stone_table_whitelist=Stone Table Whitelist
			projectex.general.override_emc_formatter=Override EMC Formatter
			projectex.general.blacklist_power_flower_from_watch=Blacklist Power Flower from Watch
			projectex.general.final_star_copy_any_item=Final Star Can Copy Any Item
			projectex.general.final_star_copy_nbt=Final Star Can Copy NBT
			projectex.general.emc_link_max_out=EMC Link Max Item Output

			projectex.tiers.collector_output=Collector Output
			projectex.tiers.relay_bonus=Relay Bonus
			projectex.tiers.relay_transfer=Relay Transfer

			projectex_client=Project EX Client
			projectex.general.emc_screen_position=EMC Screen Position
			projectex.general.search_type=Search Type
			projectex.general.search_type.normal=Normal
			projectex.general.search_type.autoselected=Auto-selected
			projectex.general.search_type.normal_jei_sync=Normal (JEI Sync)
			projectex.general.search_type.autoselected_jei_sync=Auto-selected (JEI Sync)
			 */
		}
	}

	private static class JMBlockStates extends BlockStateProvider {
		public JMBlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
			super(gen, modid, exFileHelper);
		}

		@Override
		protected void registerStatesAndModels() {
			simpleBlock(ProjectEXBlocks.ENERGY_LINK.get());
			simpleBlock(ProjectEXBlocks.PERSONAL_LINK.get());
			simpleBlock(ProjectEXBlocks.REFINED_LINK.get());
			simpleBlock(ProjectEXBlocks.COMPRESSED_REFINED_LINK.get());
			ProjectEXBlocks.COLLECTOR.forEach((k, v) -> simpleBlock(v.get(), models().cubeAll(v.get().getRegistryName().getPath(), modLoc("block/collector/" + k.name))));
			ProjectEXBlocks.RELAY.forEach((k, v) -> simpleBlock(v.get(), models().cubeAll(v.get().getRegistryName().getPath(), modLoc("block/relay/" + k.name))));
			ProjectEXBlocks.POWER_FLOWER.forEach((k, v) -> simpleBlock(v.get(), models().getBuilder(k.name + "_power_flower").parent(models().getExistingFile(modLoc("block/power_flower"))).texture("collector", modLoc("block/collector/" + k.name)).texture("relay", modLoc("block/relay/" + k.name))));

			getVariantBuilder(ProjectEXBlocks.STONE_TABLE.get())
					.forAllStates(state -> {
						Direction dir = state.getValue(BlockStateProperties.FACING);
						return ConfiguredModel.builder()
								.modelFile(models().getExistingFile(modLoc("block/stone_table")))
								.rotationX(dir == Direction.DOWN ? 0 : dir.getAxis().isHorizontal() ? 90 : 180)
								.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot()) % 360)
								.build();
					});

			simpleBlock(ProjectEXBlocks.ALCHEMY_TABLE.get(), models().getExistingFile(modLoc("block/alchemy_table")));
		}
	}

	private static class JMBlockModels extends BlockModelProvider {
		public JMBlockModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
		}
	}

	private static class JMItemModels extends ItemModelProvider {
		public JMItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
			for (Star star : Star.VALUES) {
				singleTexture("magnum_star_" + star.name, mcLoc("item/generated"), "layer0", modLoc("item/magnum_star/" + star.name));
				singleTexture("colossal_star_" + star.name, mcLoc("item/generated"), "layer0", modLoc("item/colossal_star/" + star.name));
			}

			for (Matter matter : Matter.VALUES) {
				if (matter.hasMatterItem) {
					singleTexture(matter.name + "_matter", mcLoc("item/generated"), "layer0", modLoc("item/matter/" + matter.name));
				}
			}

			singleTexture("final_star_shard", mcLoc("item/generated"), "layer0", modLoc("item/final_star_shard"));
			singleTexture("final_star", mcLoc("item/generated"), "layer0", modLoc("item/final_star"));
			singleTexture("knowledge_sharing_book", mcLoc("item/generated"), "layer0", modLoc("item/knowledge_sharing_book"));
			singleTexture("arcane_tablet", mcLoc("item/generated"), "layer0", modLoc("item/arcane_tablet"));

			for (Matter matter : Matter.VALUES) {
				withExistingParent(matter.name + "_compressed_collector", modLoc("block/" + matter.name + "_collector"));
			}

			withExistingParent("energy_link", modLoc("block/energy_link"));
			withExistingParent("personal_link", modLoc("block/personal_link"));
			withExistingParent("refined_link", modLoc("block/refined_link"));
			withExistingParent("compressed_refined_link", modLoc("block/compressed_refined_link"));

			for (Matter matter : Matter.VALUES) {
				withExistingParent(matter.name + "_collector", modLoc("block/" + matter.name + "_collector"));
				withExistingParent(matter.name + "_relay", modLoc("block/" + matter.name + "_relay"));
				withExistingParent(matter.name + "_power_flower", modLoc("block/" + matter.name + "_power_flower"));
			}

			withExistingParent("stone_table", modLoc("block/stone_table"));
			withExistingParent("alchemy_table", modLoc("block/alchemy_table"));
		}
	}

	private static class JMBlockTags extends BlockTagsProvider {
		public JMBlockTags(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
			super(generatorIn, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMItemTags extends ItemTagsProvider {
		public JMItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagProvider, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMRecipes extends RecipeProvider {
		// public final Tag<Item> CAST_IRON_GEAR = ItemTags.bind("forge:gears/cast_iron");

		public JMRecipes(DataGenerator generatorIn) {
			super(generatorIn);
		}

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer)
		{
			/*
			ShapedRecipeBuilder.shaped(FTBJarModItems.CAST_IRON_BLOCK.get())
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":cast_iron")
					.pattern("III")
					.pattern("III")
					.pattern("III")
					.define('I', CAST_IRON_INGOT)
					.save(consumer);

			ShapelessRecipeBuilder.shapeless(FTBJarModItems.CAST_IRON_NUGGET.get(), 9)
					.unlockedBy("has_item", has(CAST_IRON_INGOT))
					.group(MODID + ":cast_iron")
					.requires(CAST_IRON_INGOT)
					.save(consumer, new ResourceLocation(MODID, "cast_iron_nugget_from_ingot"));

			SimpleCookingRecipeBuilder.cooking(Ingredient.of(IRON_INGOT), FTBJarModItems.CAST_IRON_INGOT.get(), 0.1F, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
					.unlockedBy("has_item", has(IRON_INGOT))
					.save(consumer, new ResourceLocation(MODID, "cast_iron_ingot_from_smelting"));
			 */

			Ingredient afuel = Ingredient.of(PEItems.AETERNALIS_FUEL);

			for (Matter matter : Matter.VALUES) {
				if (matter.hasMatterItem && matter.getPrev() != null) {
					Item prevMatterItem = matter.getPrev().getItem().get();

					ShapedRecipeBuilder.shaped(matter.getItem().get())
							.unlockedBy("has_item", has(prevMatterItem))
							.group(MODID + ":matter/" + matter.name)
							.pattern("FFF")
							.pattern("MMM")
							.pattern("FFF")
							.define('F', afuel)
							.define('M', prevMatterItem)
							.save(consumer, new ResourceLocation(MODID, "matter_h/" + matter.name));

					ShapedRecipeBuilder.shaped(matter.getItem().get())
							.unlockedBy("has_item", has(prevMatterItem))
							.group(MODID + ":matter/" + matter.name)
							.pattern("FMF")
							.pattern("FMF")
							.pattern("FMF")
							.define('F', afuel)
							.define('M', prevMatterItem)
							.save(consumer, new ResourceLocation(MODID, "matter_v/" + matter.name));
				}
			}

			for (Matter matter : Matter.VALUES) {
				Item collector = ProjectEXItems.COLLECTOR.get(matter).get();
				Item relay = ProjectEXItems.RELAY.get(matter).get();
				Item powerFlower = ProjectEXItems.POWER_FLOWER.get(matter).get();
				Item compressedCollector = ProjectEXItems.COMPRESSED_COLLECTOR.get(matter).get();

				Matter prev = matter.getPrev();

				if (prev != null) {
					Item matterItem = matter.getItem().get();

					ShapelessRecipeBuilder.shapeless(collector)
							.unlockedBy("has_item", has(matterItem))
							.group(MODID + ":matter/" + matter.name)
							.requires(ProjectEXItems.COLLECTOR.get(prev).get())
							.requires(matterItem)
							.save(consumer, new ResourceLocation(MODID, "collector/" + matter.name));

					ShapelessRecipeBuilder.shapeless(relay)
							.unlockedBy("has_item", has(matterItem))
							.group(MODID + ":matter/" + matter.name)
							.requires(ProjectEXItems.RELAY.get(prev).get())
							.requires(matterItem)
							.save(consumer, new ResourceLocation(MODID, "relay/" + matter.name));
				}

				ShapedRecipeBuilder.shaped(compressedCollector)
						.unlockedBy("has_item", has(collector))
						.group(MODID + ":matter/" + matter.name)
						.pattern("CCC")
						.pattern("CCC")
						.pattern("CCC")
						.define('C', collector)
						.save(consumer, new ResourceLocation(MODID, "compressed_collector/" + matter.name));

				ShapedRecipeBuilder.shaped(powerFlower)
						.unlockedBy("has_item", has(compressedCollector))
						.group(MODID + ":matter/" + matter.name)
						.pattern("CLC")
						.pattern("RRR")
						.pattern("RRR")
						.define('L', ProjectEXItems.ENERGY_LINK.get())
						.define('C', compressedCollector)
						.define('R', relay)
						.save(consumer, new ResourceLocation(MODID, "power_flower/" + matter.name));
			}

			for (Star star : Star.VALUES) {
				if (star.getPrev() != null) {
					Item prevMagnum = ProjectEXItems.MAGNUM_STAR.get(star.getPrev()).get();
					Item prevColossal = ProjectEXItems.COLOSSAL_STAR.get(star.getPrev()).get();

					ShapelessRecipeBuilder.shapeless(ProjectEXItems.MAGNUM_STAR.get(star).get())
							.unlockedBy("has_item", has(prevMagnum))
							.group(MODID + ":magnum_star")
							.requires(prevMagnum)
							.requires(prevMagnum)
							.requires(prevMagnum)
							.requires(prevMagnum)
							.save(consumer, new ResourceLocation(MODID, "magnum_star/" + star.name));

					ShapelessRecipeBuilder.shapeless(ProjectEXItems.COLOSSAL_STAR.get(star).get())
							.unlockedBy("has_item", has(prevColossal))
							.group(MODID + ":colossal_star")
							.requires(prevColossal)
							.requires(prevColossal)
							.requires(prevColossal)
							.requires(prevColossal)
							.save(consumer, new ResourceLocation(MODID, "colossal_star/" + star.name));
				}
			}

			Item startMagnum = PEItems.KLEIN_STAR_OMEGA.get();
			Item startColossal = ProjectEXItems.MAGNUM_STAR.get(Star.OMEGA).get();

			ShapelessRecipeBuilder.shapeless(ProjectEXItems.MAGNUM_STAR.get(Star.EIN).get())
					.unlockedBy("has_item", has(startMagnum))
					.group(MODID + ":magnum_star")
					.requires(startMagnum)
					.requires(startMagnum)
					.requires(startMagnum)
					.requires(startMagnum)
					.save(consumer, new ResourceLocation(MODID, "magnum_star/ein"));

			ShapelessRecipeBuilder.shapeless(ProjectEXItems.COLOSSAL_STAR.get(Star.EIN).get())
					.unlockedBy("has_item", has(startColossal))
					.group(MODID + ":colossal_star")
					.requires(startColossal)
					.requires(startColossal)
					.requires(startColossal)
					.requires(startColossal)
					.save(consumer, new ResourceLocation(MODID, "colossal_star/ein"));
		}
	}

	private static class JMLootTableProvider extends LootTableProvider
	{
		private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTables = Lists.newArrayList(Pair.of(JMBlockLootTableProvider::new, LootContextParamSets.BLOCK));

		public JMLootTableProvider(DataGenerator dataGeneratorIn) {
			super(dataGeneratorIn);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return lootTables;
		}
	}

	public static class JMBlockLootTableProvider extends BlockLoot {
		private final Map<ResourceLocation, LootTable.Builder> tables = Maps.newHashMap();

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
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			addTables();

			for (ResourceLocation rs : new ArrayList<>(tables.keySet())) {
				if (rs != BuiltInLootTables.EMPTY) {
					LootTable.Builder builder = tables.remove(rs);

					if (builder == null) {
						throw new IllegalStateException(String.format("Missing loottable '%s'", rs));
					}

					consumer.accept(rs, builder);
				}
			}

			if (!tables.isEmpty()) {
				throw new IllegalStateException("Created block loot tables for non-blocks: " + tables.keySet());
			}
		}

		@Override
		protected void add(Block blockIn, LootTable.Builder table) {
			tables.put(blockIn.getLootTable(), table);
		}
	}
}
