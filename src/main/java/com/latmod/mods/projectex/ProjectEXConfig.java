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
		public final MKBlock mk4 = new MKBlock(150D, 2D, 200000D);
		public final MKBlock mk5 = new MKBlock(500D, 7D, 200000D);
		public final MKBlock mk6 = new MKBlock(2000D, 24D, 200000D);
		public final MKBlock mk_final = new MKBlock(1000000000D, 1000000000D, Double.MAX_VALUE);
	}

	public static class MKBlock
	{
		public double collector_output;
		public double relay_bonus;
		public double capacity;

		public MKBlock(double co, double rb, double c)
		{
			collector_output = co;
			relay_bonus = rb;
			capacity = c;
		}
	}

	public static class ItemsConfig
	{
		public boolean link = true;
		public boolean knowledge_sharing_book = true;
		public boolean stars = true;
		public boolean final_star = true;
		public boolean collectors = true;
		public boolean final_collector = true;
		public boolean relays = true;
		public boolean final_relay = true;
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