package com.latmod.mods.projectex;

import com.latmod.mods.projectex.item.ProjectEXItems;
import com.latmod.mods.projectex.net.ProjectEXNetHandler;
import moze_intel.projecte.PECore;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
			return new ItemStack(ProjectEXItems.MAGNUM_STAR_VIER);
		}
	};

	@Mod.Instance(MOD_ID)
	public static ProjectEX INSTANCE;

	@Mod.EventHandler
	public void onPreInit(FMLPreInitializationEvent event)
	{
		ProjectEXNetHandler.init();
	}
}