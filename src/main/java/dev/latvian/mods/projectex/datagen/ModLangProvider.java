package dev.latvian.mods.projectex.datagen;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.Star;
import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import dev.latvian.mods.projectex.item.ProjectEXItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen, String modid, String locale) {
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
