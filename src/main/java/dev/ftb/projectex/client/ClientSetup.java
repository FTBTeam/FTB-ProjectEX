package dev.ftb.projectex.client;

import dev.ftb.projectex.client.gui.AlchemyTableScreen;
import dev.ftb.projectex.client.gui.CompressedRefinedLinkScreen;
import dev.ftb.projectex.client.gui.PersonalLinkScreen;
import dev.ftb.projectex.client.gui.RefinedLinkScreen;
import dev.ftb.projectex.menu.ProjectEXMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSetup {
    public static void initEarly() {
        // run on mod construction
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        event.enqueueWork(ClientSetup::initLate);
    }

    private static void initLate() {
        // stuff that needs doing on the main thread
        registerScreenFactories();
    }

    private static void registerScreenFactories() {
        MenuScreens.register(ProjectEXMenuTypes.PERSONAL_LINK.get(), PersonalLinkScreen::new);
        MenuScreens.register(ProjectEXMenuTypes.REFINED_LINK.get(), RefinedLinkScreen::new);
        MenuScreens.register(ProjectEXMenuTypes.COMPRESSED_REFINED_LINK.get(), CompressedRefinedLinkScreen::new);
        MenuScreens.register(ProjectEXMenuTypes.ALCHEMY_TABLE.get(), AlchemyTableScreen::new);
    }
}
