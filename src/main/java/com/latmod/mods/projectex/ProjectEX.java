package com.latmod.mods.projectex;

import com.latmod.mods.projectex.gui.EMCDecimalFormat;
import com.latmod.mods.projectex.gui.ProjectEXGuiHandler;
import com.latmod.mods.projectex.item.ProjectEXItems;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import com.latmod.mods.projectex.tile.TilePowerFlower;
import moze_intel.projecte.PECore;
import moze_intel.projecte.utils.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(
		modid = ProjectEX.MOD_ID,
		name = ProjectEX.MOD_NAME,
		version = ProjectEX.VERSION,
		dependencies = "required-after:" + PECore.MODID
)
public class ProjectEX
{
	public static final String MOD_ID = "projectex";
	public static final String MOD_NAME = "Project EX";
	public static final String VERSION = "0.0.0.projectex";

	public static final CreativeTabs TAB = new CreativeTabs(MOD_ID)
	{
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ProjectEXItems.FINAL_STAR);
		}
	};

	@Mod.Instance(MOD_ID)
	public static ProjectEX INSTANCE;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		ProjectEXNetHandler.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new ProjectEXGuiHandler());
	}

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			try
			{
				Field field = Constants.class.getDeclaredField("EMC_FORMATTER");
				field.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
				field.set(null, new EMCDecimalFormat());
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		if (ProjectEXConfig.general.blacklist_power_flower_from_watch)
		{
			FMLInterModComms.sendMessage(PECore.MODID, "timewatchblacklist", TilePowerFlower.class.getName());
		}
	}
}