package dev.ftb.projectex.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    public static class General {
        public ForgeConfigSpec.LongValue emcLinkMaxOutput;
        public ForgeConfigSpec.BooleanValue enableStoneTableWhitelist;
        public ForgeConfigSpec.BooleanValue finalStarCopiesAnyItem;
        public ForgeConfigSpec.BooleanValue finalStarCopiesNBT;
        public ForgeConfigSpec.IntValue finalStarUpdateInterval;
    }

    public final ServerConfig.General general = new General();

    ServerConfig(ForgeConfigSpec.Builder builder) {
        builder.push("General");

        general.emcLinkMaxOutput = builder
                .comment("Max EMC which can be extracted from an EMC-linked block in one operation")
                .defineInRange("emc_link_max_output", 2_000_000_000L, 0, Long.MAX_VALUE);
        general.enableStoneTableWhitelist = builder
                .comment("If false, ignore the Stone Table whitelist, which is the 'projectex:stone_table_whitelist' item tag. If true, only items in that tag can be placed in the Stone Table.")
                .define("enable_stone_table_whitelist", false);
        general.finalStarCopiesAnyItem = builder
                .comment("If false, the Final Star can only copy items that have an EMC value")
                .define("final_star_copies_any_item", true);
        general.finalStarCopiesNBT = builder
                .comment("If false, copied items will have no NBT")
                .define("final_star_copies_nbt", false);
        general.finalStarUpdateInterval = builder
                .comment("Update interval in ticks for the Final Star when in a Pedestal. Set to 0 to disable all item copying")
                .defineInRange("final_star_update_interval", 20, 0, Integer.MAX_VALUE);

        builder.pop();
    }
}
