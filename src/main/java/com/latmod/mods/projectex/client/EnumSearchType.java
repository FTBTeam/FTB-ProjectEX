package com.latmod.mods.projectex.client;

/**
 * @author LatvianModder
 */
public enum EnumSearchType
{
	NORMAL("normal", false, false),
	AUTOSELECTED("autoselected", true, false),
	NORMAL_JEI_SYNC("normal_jei_sync", false, true),
	AUTOSELECTED_JEI_SYNC("autoselected_jei_sync", true, true);

	public static final EnumSearchType[] VALUES = values();

	public final String translationKey;
	public final boolean autoselected;
	public final boolean jeiSync;

	EnumSearchType(String s, boolean a, boolean j)
	{
		translationKey = "projectex.general.search_type." + s;
		autoselected = a;
		jeiSync = j;
	}
}