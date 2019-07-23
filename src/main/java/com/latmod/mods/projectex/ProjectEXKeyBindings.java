package com.latmod.mods.projectex;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public final class ProjectEXKeyBindings
{
	public static final KeyBinding FOCUS_SEARCH_BAR = new KeyBinding("key.projectex.focusSearchBar", KeyConflictContext.GUI, Keyboard.KEY_TAB, ProjectEX.MOD_NAME);

	public static void init()
	{
		ClientRegistry.registerKeyBinding(FOCUS_SEARCH_BAR);
	}
}
