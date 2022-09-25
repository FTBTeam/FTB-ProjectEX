package dev.ftb.projectex.item;

import dev.ftb.projectex.Matter;
import dev.ftb.projectex.ProjectEX;
import dev.ftb.projectex.Star;
import dev.ftb.projectex.block.ProjectEXBlocks;
import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectEXItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectEX.MOD_ID);

    private static RegistryObject<BlockItem> blockItem(String id, RegistryObject<? extends Block> sup) {
        return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(ItemGroups.CREATIVE_TAB)));
    }

    public static final RegistryObject<BlockItem> ENERGY_LINK = blockItem("energy_link", ProjectEXBlocks.ENERGY_LINK);
    public static final RegistryObject<BlockItem> PERSONAL_LINK = blockItem("personal_link", ProjectEXBlocks.PERSONAL_LINK);
    public static final RegistryObject<BlockItem> REFINED_LINK = blockItem("refined_link", ProjectEXBlocks.REFINED_LINK);
    public static final RegistryObject<BlockItem> COMPRESSED_REFINED_LINK = blockItem("compressed_refined_link", ProjectEXBlocks.COMPRESSED_REFINED_LINK);

    public static final Map<Matter, RegistryObject<BlockItem>> COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, blockItem(matter.name + "_collector", ProjectEXBlocks.COLLECTOR.get(matter)));
        }
    });

    public static final Map<Matter, RegistryObject<BlockItem>> RELAY = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, blockItem(matter.name + "_relay", ProjectEXBlocks.RELAY.get(matter)));
        }
    });

    public static final Map<Matter, RegistryObject<BlockItem>> POWER_FLOWER = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, blockItem(matter.name + "_power_flower", ProjectEXBlocks.POWER_FLOWER.get(matter)));
        }
    });

    public static final RegistryObject<BlockItem> STONE_TABLE = blockItem("stone_table", ProjectEXBlocks.STONE_TABLE);
    public static final RegistryObject<BlockItem> ALCHEMY_TABLE = blockItem("alchemy_table", ProjectEXBlocks.ALCHEMY_TABLE);

    public static final Map<Star, RegistryObject<Item>> MAGNUM_STAR = Util.make(new LinkedHashMap<>(), map -> {
        for (Star star : Star.VALUES) {
            map.put(star, REGISTRY.register("magnum_star_" + star.name, () -> new MagnumStarItem(star)));
        }
    });

    public static final Map<Star, RegistryObject<Item>> COLOSSAL_STAR = Util.make(new LinkedHashMap<>(), map -> {
        for (Star star : Star.VALUES) {
            map.put(star, REGISTRY.register("colossal_star_" + star.name, () -> new ColossalStarItem(star)));
        }
    });

    public static final Map<Matter, RegistryObject<Item>> MATTER = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            if (matter.hasMatterItem) {
                map.put(matter, REGISTRY.register(matter.name + "_matter", BasicItem::new));
            }
        }
    });

    public static final RegistryObject<Item> FINAL_STAR_SHARD = REGISTRY.register("final_star_shard", BasicItem::new);
    public static final RegistryObject<Item> FINAL_STAR = REGISTRY.register("final_star", FinalStarItem::new);
    public static final RegistryObject<Item> KNOWLEDGE_SHARING_BOOK = REGISTRY.register("knowledge_sharing_book", KnowledgeSharingBookItem::new);
    public static final RegistryObject<Item> ARCANE_TABLET = REGISTRY.register("arcane_tablet", ArcaneTabletItem::new);

    public static final Map<Matter, RegistryObject<Item>> COMPRESSED_COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, REGISTRY.register(matter.name + "_compressed_collector", FoilItem::new));
        }
    });

    static class ItemGroups {
        static final CreativeModeTab CREATIVE_TAB = new CreativeModeTab(ProjectEX.MOD_ID) {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ProjectEXItems.ARCANE_TABLET.get());
            }
        };
    }
}
