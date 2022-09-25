package dev.ftb.projectex.block;

import dev.ftb.projectex.Matter;
import dev.ftb.projectex.ProjectEX;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectEXBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ProjectEX.MOD_ID);

    public static final RegistryObject<Block> ENERGY_LINK = REGISTRY.register("energy_link", EnergyLinkBlock::new);
    public static final RegistryObject<Block> PERSONAL_LINK = REGISTRY.register("personal_link", PersonalLinkBlock::new);
    public static final RegistryObject<Block> REFINED_LINK = REGISTRY.register("refined_link", RefinedLinkBlock::new);
    public static final RegistryObject<Block> COMPRESSED_REFINED_LINK = REGISTRY.register("compressed_refined_link", CompressedRefinedLinkBlock::new);

    public static final Map<Matter, RegistryObject<? extends Block>> COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, REGISTRY.register(matter.name + "_collector", () -> new CollectorBlock(matter)));
        }
    });

    public static final Map<Matter, RegistryObject<? extends Block>> RELAY = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, REGISTRY.register(matter.name + "_relay", () -> new RelayBlock(matter)));
        }
    });

    public static final Map<Matter, RegistryObject<? extends Block>> POWER_FLOWER = Util.make(new LinkedHashMap<>(), map -> {
        for (Matter matter : Matter.VALUES) {
            map.put(matter, REGISTRY.register(matter.name + "_power_flower", () -> new PowerFlowerBlock(matter)));
        }
    });

    public static final RegistryObject<Block> STONE_TABLE = REGISTRY.register("stone_table", StoneTableBlock::new);
    public static final RegistryObject<Block> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", AlchemyTableBlock::new);
}
