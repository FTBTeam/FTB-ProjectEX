package com.latmod.mods.projectex.client;

import com.latmod.mods.projectex.ProjectEX;
import net.minecraftforge.common.config.Config;

/**
 * @author LatvianModder
 */
@Config(modid = ProjectEX.MOD_ID, category = "", name = "../local/client/" + ProjectEX.MOD_ID)
@Config.LangKey(ProjectEX.MOD_ID + "_client")
public class ProjectEXClientConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	public static class General
	{
		@Config.RangeInt(min = 0, max = 2)
		@Config.Comment({"0 - Disabled", "1 - Top Left", "2 - Top Right"})
		public int emc_on_screen = 1;
	}
}