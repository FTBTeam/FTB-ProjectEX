package dev.ftb.projectex;

import dev.ftb.projectex.block.ProjectEXBlocks;
import dev.ftb.projectex.block.entity.ProjectEXBlockEntities;
import dev.ftb.projectex.client.ClientSetup;
import dev.ftb.projectex.config.ConfigHolder;
import dev.ftb.projectex.item.ProjectEXItems;
import dev.ftb.projectex.menu.ProjectEXMenuTypes;
import dev.ftb.projectex.network.NetworkHandler;
import dev.ftb.projectex.recipes.ProjectEXRecipeSerializers;
import dev.ftb.projectex.recipes.ProjectEXRecipeTypes;
import dev.ftb.projectex.recipes.RecipeCache;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ProjectEX.MOD_ID)
public class ProjectEX {
    public static final String MOD_ID = "projectex";

    public static final Direction[] DIRECTIONS = Direction.values();

    public ProjectEX() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initEarly);

        ConfigHolder.init();

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ProjectEXBlocks.REGISTRY.register(modBus);
        ProjectEXItems.REGISTRY.register(modBus);
        ProjectEXBlockEntities.REGISTRY.register(modBus);
        ProjectEXMenuTypes.REGISTRY.register(modBus);
        ProjectEXRecipeTypes.REGISTRY.register(modBus);
        ProjectEXRecipeSerializers.REGISTRY.register(modBus);

        modBus.addListener(this::commonSetup);

        forgeBus.addListener(this::addReloadListeners);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkHandler.init();
    }

    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(RecipeCache.getCacheReloadListener());
    }
}
