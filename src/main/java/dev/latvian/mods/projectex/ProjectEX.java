package dev.latvian.mods.projectex;

import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import dev.latvian.mods.projectex.item.ProjectEXItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ProjectEX.MOD_ID)
public class ProjectEX {
	public static final String MOD_ID = "projectex";

	//public static ProjectEXCommon PROXY;

	public static CreativeModeTab tab;

	public ProjectEX() {
		//PROXY = DistExecutor.safeRunForDist(() -> FTBJarModClient::new, () -> FTBJarModCommon::new);

		tab = new CreativeModeTab(MOD_ID) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack makeIcon() {
				return new ItemStack(ProjectEXItems.ARCANE_TABLET.get());
			}
		};

		ProjectEXBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		ProjectEXItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		// ProjectEXBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		// ProjectEXRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		// ProjectEXMenus.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());

		//ProjectEXNet.init();
		//PROXY.init();
	}
}
