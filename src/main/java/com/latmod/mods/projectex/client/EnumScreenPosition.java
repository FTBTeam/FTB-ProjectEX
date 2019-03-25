package com.latmod.mods.projectex.client;

/**
 * @author LatvianModder
 */
public enum EnumScreenPosition
{
	DISABLED("Disabled"),
	TOP_LEFT("Top-Left"),
	TOP_RIGHT("Top-Right");

	private final String string;

	EnumScreenPosition(String s)
	{
		string = s;
	}

	@Override
	public String toString()
	{
		return string;
	}
}