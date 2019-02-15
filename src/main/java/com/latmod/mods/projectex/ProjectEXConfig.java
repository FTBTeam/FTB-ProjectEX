package com.latmod.mods.projectex;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
@Config(modid = ProjectEX.MOD_ID, category = "")
public class ProjectEXConfig
{
	@Config.LangKey("stat.generalButton")
	public static final General general = new General();

	@Config.RequiresMcRestart
	public static final ItemsConfig items = new ItemsConfig();

	public static class General
	{
		public final MKBlock mk1 = new MKBlock(4, 1, 64);
		public final MKBlock mk2 = new MKBlock(12, 3, 192);
		public final MKBlock mk3 = new MKBlock(40, 10, 640);
		public final MKBlock mk4 = new MKBlock(160, 40, 2560);
		public final MKBlock mk5 = new MKBlock(640, 150, 10240);
		public final MKBlock mk6 = new MKBlock(2560, 750, 40960);
		public final MKBlock mk7 = new MKBlock(10240, 3750, 163840);
		public final MKBlock mk8 = new MKBlock(40960, 15000, 655360);
		public final MKBlock mk9 = new MKBlock(163840, 60000, 2621440);
		public final MKBlock mk10 = new MKBlock(655360, 240000, 10485760);
		public final MKBlock mk_final = new MKBlock(1000000000000D, 1000000000000D, Double.MAX_VALUE);
	}

	public static class MKBlock
	{
		@Config.LangKey("projectex.general.collector_output")
		public double collector_output;

		@Config.LangKey("projectex.general.relay_bonus")
		public double relay_bonus;

		@Config.LangKey("projectex.general.relay_transfer")
		public double relay_transfer;

		public MKBlock(double co, double rb, double rt)
		{
			collector_output = co;
			relay_bonus = rb;
			relay_transfer = rt;
		}
	}

	public static class ItemsConfig
	{
		@Config.LangKey("tile.projectex.personal_link.name")
		public boolean link = true;

		@Config.LangKey("item.projectex.knowledge_sharing_book.name")
		public boolean knowledge_sharing_book = true;

		public boolean stars = true;

		@Config.LangKey("item.projectex.final_star.name")
		public boolean final_star = true;

		public boolean collectors = true;
		public boolean relays = true;
		public boolean tome = true;
	}

	public static void sync()
	{
		ConfigManager.sync(ProjectEX.MOD_ID, Config.Type.INSTANCE);
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.getModID().equals(ProjectEX.MOD_ID))
		{
			sync();
		}
	}
}