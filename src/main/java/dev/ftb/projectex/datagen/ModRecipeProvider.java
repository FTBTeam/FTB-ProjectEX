package dev.ftb.projectex.datagen;

import dev.ftb.projectex.Matter;
import dev.ftb.projectex.Star;
import dev.ftb.projectex.datagen.recipes.AlchemyTableRecipeBuilder;
import dev.ftb.projectex.datagen.recipes.Criteria;
import dev.ftb.projectex.item.ProjectEXItems;
import dev.ftb.projectex.recipes.AlchemyTableRecipe;
import moze_intel.projecte.gameObjs.registries.PEItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.Validate;

import java.util.function.Consumer;

import static dev.ftb.projectex.util.ProjectEXUtils.rl;

class ModRecipeProvider extends RecipeProvider {
    // public final Tag<Item> CAST_IRON_GEAR = ItemTags.bind("forge:gears/cast_iron");

    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
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
                        .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                        .pattern("FFF")
                        .pattern("MMM")
                        .pattern("FFF")
                        .define('F', afuel)
                        .define('M', prevMatterItem)
                        .save(consumer, rl("matter_h/" + matter.name));

                ShapedRecipeBuilder.shaped(matter.getItem().get())
                        .unlockedBy("has_item", has(prevMatterItem))
                        .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                        .pattern("FMF")
                        .pattern("FMF")
                        .pattern("FMF")
                        .define('F', afuel)
                        .define('M', prevMatterItem)
                        .save(consumer, rl("matter_v/" + matter.name));
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
                        .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                        .requires(ProjectEXItems.COLLECTOR.get(prev).get())
                        .requires(matterItem)
                        .save(consumer, rl("collector/" + matter.name));

                ShapelessRecipeBuilder.shapeless(relay)
                        .unlockedBy("has_item", has(matterItem))
                        .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                        .requires(ProjectEXItems.RELAY.get(prev).get())
                        .requires(matterItem)
                        .save(consumer, rl("relay/" + matter.name));
            }

            ShapedRecipeBuilder.shaped(compressedCollector)
                    .unlockedBy("has_item", has(collector))
                    .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                    .pattern("CCC")
                    .pattern("CCC")
                    .pattern("CCC")
                    .define('C', collector)
                    .save(consumer, rl("compressed_collector/" + matter.name));

            ShapedRecipeBuilder.shaped(powerFlower)
                    .unlockedBy("has_item", has(compressedCollector))
                    .group(ProjectEXDataGen.MODID + ":matter/" + matter.name)
                    .pattern("CLC")
                    .pattern("RRR")
                    .pattern("RRR")
                    .define('L', ProjectEXItems.ENERGY_LINK.get())
                    .define('C', compressedCollector)
                    .define('R', relay)
                    .save(consumer, rl("power_flower/" + matter.name));
        }

        for (Star star : Star.VALUES) {
            if (star.getPrev() != null) {
                Item prevMagnum = ProjectEXItems.MAGNUM_STAR.get(star.getPrev()).get();
                Item prevColossal = ProjectEXItems.COLOSSAL_STAR.get(star.getPrev()).get();

                ShapelessRecipeBuilder.shapeless(ProjectEXItems.MAGNUM_STAR.get(star).get())
                        .unlockedBy("has_item", has(prevMagnum))
                        .group(ProjectEXDataGen.MODID + ":magnum_star")
                        .requires(prevMagnum)
                        .requires(prevMagnum)
                        .requires(prevMagnum)
                        .requires(prevMagnum)
                        .save(consumer, rl("magnum_star/" + star.name));

                ShapelessRecipeBuilder.shapeless(ProjectEXItems.COLOSSAL_STAR.get(star).get())
                        .unlockedBy("has_item", has(prevColossal))
                        .group(ProjectEXDataGen.MODID + ":colossal_star")
                        .requires(prevColossal)
                        .requires(prevColossal)
                        .requires(prevColossal)
                        .requires(prevColossal)
                        .save(consumer, rl("colossal_star/" + star.name));
            }
        }

        Item startMagnum = PEItems.KLEIN_STAR_OMEGA.get();
        Item startColossal = ProjectEXItems.MAGNUM_STAR.get(Star.OMEGA).get();

        ShapelessRecipeBuilder.shapeless(ProjectEXItems.MAGNUM_STAR.get(Star.EIN).get())
                .unlockedBy("has_item", has(startMagnum))
                .group(ProjectEXDataGen.MODID + ":magnum_star")
                .requires(startMagnum)
                .requires(startMagnum)
                .requires(startMagnum)
                .requires(startMagnum)
                .save(consumer, rl("magnum_star/ein"));

        ShapelessRecipeBuilder.shapeless(ProjectEXItems.COLOSSAL_STAR.get(Star.EIN).get())
                .unlockedBy("has_item", has(startColossal))
                .group(ProjectEXDataGen.MODID + ":colossal_star")
                .requires(startColossal)
                .requires(startColossal)
                .requires(startColossal)
                .requires(startColossal)
                .save(consumer, rl("colossal_star/ein"));

        alchemyStep(consumer, Items.CHARCOAL, new ItemStack(Items.COAL));

        alchemyChain(consumer,
                Items.REDSTONE,
                Items.GUNPOWDER,
                Items.GLOWSTONE_DUST,
                Items.BLAZE_POWDER,
                Items.BLAZE_ROD
        );

        alchemyChain(consumer,
                Items.LAPIS_LAZULI,
                Items.PRISMARINE_SHARD,
                Items.PRISMARINE_CRYSTALS
        );

        alchemyChain(consumer,
                PEItems.LOW_COVALENCE_DUST,
                PEItems.MEDIUM_COVALENCE_DUST,
                PEItems.HIGH_COVALENCE_DUST
        );

        alchemyChain(consumer,
                Items.BEEF,
                Items.ROTTEN_FLESH,
                Items.LEATHER,
                Items.SPIDER_EYE,
                Items.BONE
        );

        alchemyChain(consumer,
                Items.WHEAT_SEEDS,
                Items.MELON,
                Items.APPLE,
                Items.CARROT,
                Items.BEETROOT,
                Items.POTATO,
                Blocks.PUMPKIN
        );

        alchemyChain(consumer,
                Items.COOKIE,
                Items.BREAD,
                Items.CAKE
        );

        alchemyChain(consumer,
                PEItems.ALCHEMICAL_COAL,
                Blocks.REDSTONE_BLOCK,
                Items.LAVA_BUCKET,
                Blocks.OBSIDIAN
        );

        alchemyChain(consumer,
                Blocks.OAK_LEAVES,
                Blocks.GRASS,
                Blocks.FERN,
                Blocks.VINE,
                Blocks.LILY_PAD
        );

        alchemyStep(consumer, Items.ENDER_EYE, new ItemStack(Items.CHORUS_FRUIT));
        alchemyStep(consumer, Items.STRING, new ItemStack(Items.FEATHER));
        alchemyStep(consumer, Items.STICK, new ItemStack(Blocks.GRASS));
    }

    private void alchemyChain(Consumer<FinishedRecipe> consumer, ItemLike... items) {
        Validate.isTrue(items.length >= 3, "3 or more items required!");
        for (int i = 1; i < items.length; i++) {
            alchemyStep(consumer, items[i-1], new ItemStack(items[i]));
        }
    }
    
    private void alchemyStep(Consumer<FinishedRecipe> consumer, ItemLike in, ItemStack out) {
        String name = "alchemy/" + in.asItem().getRegistryName().getPath() + "_to_" + out.getItem().getRegistryName().getPath();
        ResourceLocation id = rl(name);
        alchemyRecipe(id, Ingredient.of(in), out).build(consumer, id);
    }

    private AlchemyTableRecipeBuilder alchemyRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        return alchemyRecipe(id, input, output, 0L, 200);
    }

    private AlchemyTableRecipeBuilder alchemyRecipe(ResourceLocation id, Ingredient input, ItemStack output, long emcOverride, int craftingTime) {
        return new AlchemyTableRecipeBuilder(new AlchemyTableRecipe(id, input, output, emcOverride, craftingTime))
                .addCriterion(Criteria.has(ProjectEXItems.ALCHEMY_TABLE.get()));
    }
}
