package com.latmod.mods.projectex.client;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.ProjectEXConfig;
import com.latmod.mods.projectex.block.EnumMatter;
import com.latmod.mods.projectex.block.EnumTier;
import com.latmod.mods.projectex.gui.EMCFormat;
import com.latmod.mods.projectex.integration.PersonalEMC;
import com.latmod.mods.projectex.item.ProjectEXItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, value = Side.CLIENT)
public class ProjectEXClientEventHandler
{
	private static double lastEMC;
	private static long lastUpdate;
	private static double[] emcsa = new double[5];
	public static double emcs = 0D;

	private static void addModel(Item item, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		if (ProjectEXConfig.items.link)
		{
			addModel(ProjectEXItems.ENERGY_LINK, "normal");
			addModel(ProjectEXItems.PERSONAL_LINK, "normal");
			addModel(ProjectEXItems.REFINED_LINK, "normal");
			addModel(ProjectEXItems.COMPRESSED_REFINED_LINK, "normal");
		}

		for (EnumTier tier : EnumTier.VALUES)
		{
			if (ProjectEXConfig.items.collectors)
			{
				ModelLoader.setCustomModelResourceLocation(ProjectEXItems.COLLECTOR, tier.ordinal(), new ModelResourceLocation(ProjectEXItems.COLLECTOR.getRegistryName(), "tier=" + tier.getName()));
			}

			if (ProjectEXConfig.items.relays)
			{
				ModelLoader.setCustomModelResourceLocation(ProjectEXItems.RELAY, tier.ordinal(), new ModelResourceLocation(ProjectEXItems.RELAY.getRegistryName(), "tier=" + tier.getName()));
			}

			if (ProjectEXConfig.items.power_flowers)
			{
				ModelLoader.setCustomModelResourceLocation(ProjectEXItems.COMPRESSED_COLLECTOR, tier.ordinal(), new ModelResourceLocation(ProjectEXItems.COLLECTOR.getRegistryName(), "tier=" + tier.getName()));
				ModelLoader.setCustomModelResourceLocation(ProjectEXItems.POWER_FLOWER, tier.ordinal(), new ModelResourceLocation(ProjectEXItems.POWER_FLOWER.getRegistryName(), "tier=" + tier.getName()));
			}
		}

		if (ProjectEXConfig.items.stone_table)
		{
			addModel(ProjectEXItems.STONE_TABLE, "facing=down");
		}

		if (ProjectEXConfig.items.alchemy_table)
		{
			addModel(ProjectEXItems.ALCHEMY_TABLE, "normal");
		}

		if (ProjectEXConfig.items.stars)
		{
			addModel(ProjectEXItems.MAGNUM_STAR_EIN, "inventory");
			addModel(ProjectEXItems.MAGNUM_STAR_ZWEI, "inventory");
			addModel(ProjectEXItems.MAGNUM_STAR_DREI, "inventory");
			addModel(ProjectEXItems.MAGNUM_STAR_VIER, "inventory");
			addModel(ProjectEXItems.MAGNUM_STAR_SPHERE, "inventory");
			addModel(ProjectEXItems.MAGNUM_STAR_OMEGA, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_EIN, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_ZWEI, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_DREI, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_VIER, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_SPHERE, "inventory");
			addModel(ProjectEXItems.COLOSSAL_STAR_OMEGA, "inventory");
		}

		for (EnumMatter matter : EnumMatter.VALUES)
		{
			ModelLoader.setCustomModelResourceLocation(ProjectEXItems.MATTER, matter.ordinal(), new ModelResourceLocation(ProjectEX.MOD_ID + ":matter/" + matter.getName() + "#inventory"));
		}

		if (ProjectEXConfig.items.clay_matter)
		{
			ModelLoader.setCustomModelResourceLocation(ProjectEXItems.CLAY_MATTER, 0, new ModelResourceLocation(ProjectEX.MOD_ID + ":matter/clay#inventory"));
		}

		addModel(ProjectEXItems.FINAL_STAR_SHARD, "inventory");

		if (ProjectEXConfig.items.final_star)
		{
			addModel(ProjectEXItems.FINAL_STAR, "inventory");
		}

		if (ProjectEXConfig.items.knowledge_sharing_book)
		{
			addModel(ProjectEXItems.KNOWLEDGE_SHARING_BOOK, "inventory");
		}

		if (ProjectEXConfig.items.arcane_tablet)
		{
			addModel(ProjectEXItems.ARCANE_TABLET, "inventory");
		}
	}

	@SubscribeEvent
	public static void addInfoText(RenderGameOverlayEvent.Text event)
	{
		if (Minecraft.getMinecraft().player != null)
		{
			long now = System.currentTimeMillis();
			double emc = PersonalEMC.get(Minecraft.getMinecraft().player).getEmc();

			if ((now - lastUpdate) >= 1000L)
			{
				System.arraycopy(emcsa, 1, emcsa, 0, emcsa.length - 1);
				emcsa[emcsa.length - 1] = emc - lastEMC;
				lastEMC = emc;
				lastUpdate = now;

				emcs = 0D;

				for (double d : emcsa)
				{
					emcs += d;
				}

				emcs /= (double) emcsa.length;
			}

			if (ProjectEXClientConfig.general.emc_screen_position != EnumScreenPosition.DISABLED && emc > 0D)
			{
				String s = EMCFormat.INSTANCE.format(emc);

				if (emcs != 0D)
				{
					s += (emcs > 0D ? (TextFormatting.GREEN + "+") : (TextFormatting.RED + "-")) + EMCFormat.INSTANCE.format(Math.abs(emcs)) + "/s";
				}

				(ProjectEXClientConfig.general.emc_screen_position == EnumScreenPosition.TOP_LEFT ? event.getLeft() : event.getRight()).add("EMC: " + s);
			}
		}
	}
}