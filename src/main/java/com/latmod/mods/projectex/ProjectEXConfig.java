package com.latmod.mods.projectex;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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

	public static final Tiers tiers = new Tiers();

	private static class ItemKey
	{
		private Item item;
		private int metadata;

		@Override
		public int hashCode()
		{
			return item.hashCode() * 31 + (metadata == OreDictionary.WILDCARD_VALUE ? 0 : metadata);
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj instanceof ItemKey)
			{
				ItemKey key = (ItemKey) obj;
				return item == key.item && (metadata == key.metadata || metadata == OreDictionary.WILDCARD_VALUE || key.metadata == OreDictionary.WILDCARD_VALUE);
			}

			return false;
		}

		@Override
		public String toString()
		{
			return item.getRegistryName() + "@" + metadata;
		}
	}

	public static class General
	{
		public boolean override_emc_formatter = true;

		public String[] stone_table_whitelist = {
				"oredict:ingot",
				"oredict:gem",
				"oredict:dust",
				"oredict:nugget",
				"oredict:block",
				"oredict:ore",

				"oredict:stone",
				"oredict:cobblestone",
				"oredict:sand",
				"oredict:dirt",
				"oredict:gravel",
				"oredict:obsidian",
				"oredict:netherrack",
				"oredict:endstone",

				"minecraft:coal@*",
				"minecraft:flint",
				"minecraft:clay_ball",

				"oredict:dye",
				"oredict:string",
				"oredict:leather",
				"oredict:sugercane",
				"oredict:feather",
				"oredict:gunpowder",
				"minecraft:wheat_seeds",

				"oredict:logWood",
				"oredict:treeSapling",
				"oredict:blockGlassColorless",

				"oredict:enderpearl",
				"minecraft:blaze_rod",
				"minecraft:ghast_tear",
		};

		private HashSet<ItemKey> stoneTableCache = null;

		public boolean isStoneTableWhitelisted(ItemStack stack)
		{
			if (stoneTableCache == null)
			{
				stoneTableCache = new HashSet<>();
				HashMap<String, HashSet<ItemKey>> oreDict = new HashMap<>();

				for (String s : OreDictionary.getOreNames())
				{
					HashSet<ItemKey> set = new HashSet<>();

					for (ItemStack stack1 : OreDictionary.getOres(s))
					{
						Item item = stack1.getItem();
						ItemKey key = new ItemKey();
						key.item = item;
						key.metadata = stack1.getMetadata();
						set.add(key);
					}

					oreDict.put(s, set);
				}

				for (String s : general.stone_table_whitelist)
				{
					String[] s1 = s.trim().split("@", 2);

					if (s1[0].isEmpty())
					{
						continue;
					}

					if (s1.length == 1 && s1[0].startsWith("oredict:"))
					{
						String s2 = s1[0].substring(8);

						if (s2.isEmpty())
						{
							continue;
						}

						for (Map.Entry<String, HashSet<ItemKey>> entry : oreDict.entrySet())
						{
							if (!entry.getValue().isEmpty() && entry.getKey().startsWith(s2))
							{
								stoneTableCache.addAll(entry.getValue());
							}
						}
					}
					else
					{
						Item item = Item.getByNameOrId(s1[0]);

						if (item != null && item != Items.AIR)
						{
							ItemKey key = new ItemKey();
							key.item = item;
							key.metadata = s1.length == 1 ? 0 : s1[1].equals("*") ? OreDictionary.WILDCARD_VALUE : Integer.parseInt(s1[1]);
							stoneTableCache.add(key);
						}
					}
				}
			}

			ItemKey key = new ItemKey();
			key.item = stack.getItem();
			key.metadata = stack.getMetadata();
			return stoneTableCache.contains(key);
		}
	}

	public static class Tiers
	{
		public final BlockTier mk1 = new BlockTier(4, 1, 64);
		public final BlockTier mk2 = new BlockTier(12, 3, 192);
		public final BlockTier mk3 = new BlockTier(40, 10, 640);
		public final BlockTier mk4 = new BlockTier(160, 40, 2560);
		public final BlockTier mk5 = new BlockTier(640, 150, 10240);
		public final BlockTier mk6 = new BlockTier(2560, 750, 40960);
		public final BlockTier mk7 = new BlockTier(10240, 3750, 163840);
		public final BlockTier mk8 = new BlockTier(40960, 15000, 655360);
		public final BlockTier mk9 = new BlockTier(163840, 60000, 2621440);
		public final BlockTier mk10 = new BlockTier(655360, 240000, 10485760);
		public final BlockTier mk_final = new BlockTier(1000000000000D, 1000000000000D, Double.MAX_VALUE);
	}

	public static class BlockTier
	{
		@Config.LangKey("projectex.tiers.collector_output")
		public double collector_output;

		@Config.LangKey("projectex.tiers.relay_bonus")
		public double relay_bonus;

		@Config.LangKey("projectex.tiers.relay_transfer")
		public double relay_transfer;

		public BlockTier(double co, double rb, double rt)
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

		@Config.LangKey("tile.projectex.stone_table.name")
		public boolean stone_table = true;

		@Config.LangKey("item.projectex.tablet_mk2.name")
		public boolean tablet_mk2 = true;
	}

	public static void sync()
	{
		ConfigManager.sync(ProjectEX.MOD_ID, Config.Type.INSTANCE);
		general.stoneTableCache = null;
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