package dev.latvian.mods.projectex.config;

public enum SearchType {
    NORMAL("normal", false, false),
    AUTOSELECTED("autoselected", true, false),
    NORMAL_JEI_SYNC("normal_jei_sync", false, true),
    AUTOSELECTED_JEI_SYNC("autoselected_jei_sync", true, true);

    public static final SearchType[] VALUES = values();

    public final String translationKey;
    public final boolean autoselected;
    public final boolean jeiSync;

    SearchType(String name, boolean autoSelected, boolean jeiSync) {
        this.translationKey = "projectex.general.search_type." + name;
        this.autoselected = autoSelected;
        this.jeiSync = jeiSync;
    }
}
