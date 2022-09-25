package dev.ftb.projectex.config;

import dev.ftb.projectex.client.EMCOverlayPosition;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static class General {
        public ForgeConfigSpec.EnumValue<EMCOverlayPosition> screenPosition;
        public ForgeConfigSpec.BooleanValue onlyShowEMCWhenHoldingModItem;
        public ForgeConfigSpec.EnumValue<SearchType> searchType;
    }

    public final ClientConfig.General general = new General();

    ClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("General");

        general.screenPosition = builder
                .comment("Where (or whether) to show the player's personal EMC")
                .defineEnum("screen_position", EMCOverlayPosition.TOP_LEFT);
        general.onlyShowEMCWhenHoldingModItem = builder
                .comment("If true, on-screen personal EMC is only shown when holding an item from ProjectE or ProjectEX")
                .define("only_show_emc_when_holding_mod_item", false);
        general.searchType = builder
                .comment("The search type to use in the Stone Table")
                .defineEnum("search_type", SearchType.NORMAL);

        builder.pop();
    }
}
