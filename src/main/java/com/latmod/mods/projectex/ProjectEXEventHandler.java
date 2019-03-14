package com.latmod.mods.projectex;

import com.latmod.mods.projectex.block.BlockCollector;
import com.latmod.mods.projectex.block.BlockLinkMK1;
import com.latmod.mods.projectex.block.BlockLinkMK2;
import com.latmod.mods.projectex.block.BlockLinkMK3;
import com.latmod.mods.projectex.block.BlockPowerFlower;
import com.latmod.mods.projectex.block.BlockRelay;
import com.latmod.mods.projectex.block.BlockStoneTable;
import com.latmod.mods.projectex.block.EnumMatter;
import com.latmod.mods.projectex.block.EnumTier;
import com.latmod.mods.projectex.block.ProjectEXBlocks;
import com.latmod.mods.projectex.item.ItemArcaneTablet;
import com.latmod.mods.projectex.item.ItemBlockTier;
import com.latmod.mods.projectex.item.ItemColossalStar;
import com.latmod.mods.projectex.item.ItemCompressedCollector;
import com.latmod.mods.projectex.item.ItemFinalStar;
import com.latmod.mods.projectex.item.ItemKnowledgeSharingBook;
import com.latmod.mods.projectex.item.ItemMagnumStar;
import com.latmod.mods.projectex.item.ItemMatter;
import com.latmod.mods.projectex.item.ProjectEXItems;
import com.latmod.mods.projectex.tile.TileCollector;
import com.latmod.mods.projectex.tile.TileLinkMK1;
import com.latmod.mods.projectex.tile.TileLinkMK2;
import com.latmod.mods.projectex.tile.TileLinkMK3;
import com.latmod.mods.projectex.tile.TilePowerFlower;
import com.latmod.mods.projectex.tile.TileRelay;
import moze_intel.projecte.api.item.IItemEmc;
import moze_intel.projecte.gameObjs.ObjHandler;
import moze_intel.projecte.gameObjs.items.KleinStar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID)
public class ProjectEXEventHandler
{
	private static Block withName(Block item, String name)
	{
		item.setCreativeTab(ProjectEX.TAB);
		item.setRegistryName(name);
		item.setTranslationKey(ProjectEX.MOD_ID + "." + name);
		return item;
	}

	private static Item withName(Item item, String name)
	{
		item.setCreativeTab(ProjectEX.TAB);
		item.setRegistryName(name);
		item.setTranslationKey(ProjectEX.MOD_ID + "." + name);
		return item;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> r = event.getRegistry();

		if (ProjectEXConfig.items.link)
		{
			r.register(withName(new BlockLinkMK1(), "personal_link"));
			r.register(withName(new BlockLinkMK2(), "refined_link"));
			r.register(withName(new BlockLinkMK3(), "compressed_refined_link"));
			GameRegistry.registerTileEntity(TileLinkMK1.class, new ResourceLocation(ProjectEX.MOD_ID, "personal_link"));
			GameRegistry.registerTileEntity(TileLinkMK2.class, new ResourceLocation(ProjectEX.MOD_ID, "refined_link"));
			GameRegistry.registerTileEntity(TileLinkMK3.class, new ResourceLocation(ProjectEX.MOD_ID, "compressed_refined_link"));
		}

		if (ProjectEXConfig.items.collectors)
		{
			r.register(withName(new BlockCollector(), "collector"));
			GameRegistry.registerTileEntity(TileCollector.class, new ResourceLocation(ProjectEX.MOD_ID, "collector"));
		}

		if (ProjectEXConfig.items.relays)
		{
			r.register(withName(new BlockRelay(), "relay"));
			GameRegistry.registerTileEntity(TileRelay.class, new ResourceLocation(ProjectEX.MOD_ID, "relay"));
		}

		if (ProjectEXConfig.items.power_flowers)
		{
			r.register(withName(new BlockPowerFlower(), "power_flower"));
			GameRegistry.registerTileEntity(TilePowerFlower.class, new ResourceLocation(ProjectEX.MOD_ID, "power_flower"));
		}

		if (ProjectEXConfig.items.stone_table)
		{
			r.register(withName(new BlockStoneTable(), "stone_table"));
		}
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> r = event.getRegistry();

		if (ProjectEXConfig.items.link)
		{
			r.register(new ItemBlock(ProjectEXBlocks.PERSONAL_LINK).setRegistryName("personal_link"));
			r.register(new ItemBlock(ProjectEXBlocks.REFINED_LINK).setRegistryName("refined_link"));
			r.register(new ItemBlock(ProjectEXBlocks.COMPRESSED_REFINED_LINK).setRegistryName("compressed_refined_link"));
		}

		if (ProjectEXConfig.items.collectors)
		{
			r.register(new ItemBlockTier(ProjectEXBlocks.COLLECTOR).setRegistryName("collector"));
		}

		if (ProjectEXConfig.items.relays)
		{
			r.register(new ItemBlockTier(ProjectEXBlocks.RELAY).setRegistryName("relay"));
		}

		if (ProjectEXConfig.items.power_flowers)
		{
			r.register(withName(new ItemCompressedCollector(), "compressed_collector"));
			r.register(new ItemBlockTier(ProjectEXBlocks.POWER_FLOWER).setRegistryName("power_flower"));
		}

		if (ProjectEXConfig.items.stone_table)
		{
			r.register(withName(new ItemBlock(ProjectEXBlocks.STONE_TABLE), "stone_table"));
		}

		if (ProjectEXConfig.items.stars)
		{
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.EIN), "magnum_star_ein"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.ZWEI), "magnum_star_zwei"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.DREI), "magnum_star_drei"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.VIER), "magnum_star_vier"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.SPHERE), "magnum_star_sphere"));
			r.register(withName(new ItemMagnumStar(KleinStar.EnumKleinTier.OMEGA), "magnum_star_omega"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.EIN), "colossal_star_ein"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.ZWEI), "colossal_star_zwei"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.DREI), "colossal_star_drei"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.VIER), "colossal_star_vier"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.SPHERE), "colossal_star_sphere"));
			r.register(withName(new ItemColossalStar(KleinStar.EnumKleinTier.OMEGA), "colossal_star_omega"));
		}

		r.register(withName(new ItemMatter(), "matter"));
		r.register(withName(new Item(), "final_star_shard"));

		if (ProjectEXConfig.items.final_star)
		{
			r.register(withName(new ItemFinalStar(), "final_star"));
		}

		if (ProjectEXConfig.items.knowledge_sharing_book)
		{
			r.register(withName(new ItemKnowledgeSharingBook(), "knowledge_sharing_book"));
		}

		if (ProjectEXConfig.items.arcane_tablet)
		{
			r.register(withName(new ItemArcaneTablet(), "arcane_tablet"));
		}
	}

	@SubscribeEvent
	public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
	{
		if (ProjectEXConfig.items.stars && event.crafting.getItem() instanceof ItemMagnumStar)
		{
			IItemEmc star = (IItemEmc) event.crafting.getItem();

			for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++)
			{
				ItemStack stack = event.craftMatrix.getStackInSlot(i);

				if (stack.getItem() instanceof IItemEmc)
				{
					star.addEmc(event.crafting, ((IItemEmc) stack.getItem()).getStoredEmc(stack));
				}
			}
		}
	}

	@SubscribeEvent
	public static void addRecipes(RegistryEvent.Register<IRecipe> event)
	{
		IForgeRegistry<IRecipe> r = event.getRegistry();

		EnumMatter prevMatter = null;

		Ingredient afuel = Ingredient.fromStacks(new ItemStack(ObjHandler.fuels, 1, 2));

		for (EnumMatter matter : EnumMatter.VALUES)
		{
			if (prevMatter != null)
			{
				Ingredient prevMatterIngredient = Ingredient.fromStacks(prevMatter.get());

				NonNullList<Ingredient> listh = NonNullList.create();
				listh.add(afuel);
				listh.add(afuel);
				listh.add(afuel);
				listh.add(prevMatterIngredient);
				listh.add(prevMatterIngredient);
				listh.add(prevMatterIngredient);
				listh.add(afuel);
				listh.add(afuel);
				listh.add(afuel);
				r.register(new ShapedRecipes("projectex:matter", 3, 3, listh, matter.get()).setRegistryName("matter/" + matter.getName() + "_h"));

				NonNullList<Ingredient> listv = NonNullList.create();
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				listv.add(afuel);
				listv.add(prevMatterIngredient);
				listv.add(afuel);
				r.register(new ShapedRecipes("projectex:matter", 3, 3, listv, matter.get()).setRegistryName("matter/" + matter.getName() + "_v"));
			}

			prevMatter = matter;
		}

		EnumTier prevTier = null;

		for (EnumTier tier : EnumTier.VALUES)
		{
			if (ProjectEXConfig.items.power_flowers)
			{
				r.register(new ShapedRecipes("projectex:compressed_collector", 3, 3, NonNullList.withSize(9, Ingredient.fromStacks(new ItemStack(ProjectEXItems.COLLECTOR, 1, tier.ordinal()))), new ItemStack(ProjectEXItems.COMPRESSED_COLLECTOR, 1, tier.ordinal())).setRegistryName("compressed_collector/" + tier.getName()));

				NonNullList<Ingredient> list = NonNullList.create();
				Ingredient ccollector = Ingredient.fromStacks(new ItemStack(ProjectEXItems.COMPRESSED_COLLECTOR, 1, tier.ordinal()));
				Ingredient relay = Ingredient.fromStacks(new ItemStack(ProjectEXItems.RELAY, 1, tier.ordinal()));

				list.add(ccollector);
				list.add(Ingredient.fromItem(ProjectEXItems.PERSONAL_LINK));
				list.add(ccollector);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				list.add(relay);
				r.register(new ShapedRecipes("projectex:power_flower", 3, 3, list, new ItemStack(ProjectEXItems.POWER_FLOWER, 1, tier.ordinal())).setRegistryName("power_flower/" + tier.getName()));
			}

			if (prevTier != null)
			{
				Ingredient matterIngredient = Ingredient.fromStacks(tier.matter.get());

				if (ProjectEXConfig.items.collectors)
				{
					NonNullList<Ingredient> list = NonNullList.create();
					list.add(Ingredient.fromStacks(new ItemStack(ProjectEXItems.COLLECTOR, 1, prevTier.ordinal())));
					list.add(matterIngredient);
					r.register(new ShapelessRecipes("projectex:collector", new ItemStack(ProjectEXItems.COLLECTOR, 1, tier.ordinal()), list).setRegistryName("collector/" + tier.getName()));
				}

				if (ProjectEXConfig.items.relays)
				{
					NonNullList<Ingredient> list = NonNullList.create();
					list.add(Ingredient.fromStacks(new ItemStack(ProjectEXItems.RELAY, 1, prevTier.ordinal())));
					list.add(matterIngredient);
					r.register(new ShapelessRecipes("projectex:relay", new ItemStack(ProjectEXItems.RELAY, 1, tier.ordinal()), list).setRegistryName("relay/" + tier.getName()));
				}
			}

			prevTier = tier;
		}
	}
}