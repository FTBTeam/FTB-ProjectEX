package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ProjectEXMenuTypes {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProjectEX.MOD_ID);

    public static final RegistryObject<MenuType<PersonalLinkMenu>> PERSONAL_LINK
            = register("personal_link", PersonalLinkMenu::new);
    public static final RegistryObject<MenuType<RefinedLinkMenu>> REFINED_LINK
            = register("refined_link", RefinedLinkMenu::new);
    public static final RegistryObject<MenuType<CompressedRefinedLinkMenu>> COMPRESSED_REFINED_LINK
            = register("compressed_refined_link", CompressedRefinedLinkMenu::new);
    public static final RegistryObject<MenuType<AlchemyTableMenu>> ALCHEMY_TABLE
            = register("alchemy_table", AlchemyTableMenu::new);

    private static <C extends AbstractContainerMenu, T extends MenuType<C>> RegistryObject<T> register(String name, IContainerFactory<? extends C> f) {
        //noinspection unchecked
        return REGISTRY.register(name, () -> (T) IForgeMenuType.create(f));
    }
}
