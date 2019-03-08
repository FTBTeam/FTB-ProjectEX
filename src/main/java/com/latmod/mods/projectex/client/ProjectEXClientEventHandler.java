package com.latmod.mods.projectex.client;

import com.latmod.mods.projectex.ProjectEX;
import com.latmod.mods.projectex.block.EnumTier;
import com.latmod.mods.projectex.item.ItemMatter;
import com.latmod.mods.projectex.item.ProjectEXItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
	private static void addModel(Item item, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		addModel(ProjectEXItems.PERSONAL_LINK, "normal");
		addModel(ProjectEXItems.REFINED_LINK, "normal");
		addModel(ProjectEXItems.COMPRESSED_REFINED_LINK, "normal");

		for (EnumTier tier : EnumTier.VALUES)
		{
			ModelLoader.setCustomModelResourceLocation(ProjectEXItems.COLLECTOR, tier.metadata, new ModelResourceLocation(ProjectEXItems.COLLECTOR.getRegistryName(), "tier=" + tier.getName()));
			ModelLoader.setCustomModelResourceLocation(ProjectEXItems.RELAY, tier.metadata, new ModelResourceLocation(ProjectEXItems.RELAY.getRegistryName(), "tier=" + tier.getName()));
		}

		addModel(ProjectEXItems.STONE_TABLE, "normal");

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

		for (int i = 0; i < ItemMatter.NAMES.length; i++)
		{
			ModelLoader.setCustomModelResourceLocation(ProjectEXItems.MATTER, i, new ModelResourceLocation(ProjectEX.MOD_ID + ":matter/" + ItemMatter.NAMES[i] + "#inventory"));
		}

		addModel(ProjectEXItems.FINAL_STAR_SHARD, "inventory");
		addModel(ProjectEXItems.FINAL_STAR, "inventory");
		addModel(ProjectEXItems.KNOWLEDGE_SHARING_BOOK, "inventory");
		addModel(ProjectEXItems.TABLET_MK2, "inventory");
	}
}