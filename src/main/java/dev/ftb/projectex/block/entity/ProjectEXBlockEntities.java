package dev.ftb.projectex.block.entity;

import dev.ftb.projectex.ProjectEX;
import dev.ftb.projectex.block.ProjectEXBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectEXBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ProjectEX.MOD_ID);

    public static final RegistryObject<BlockEntityType<EnergyLinkBlockEntity>> ENERGY_LINK
            = register("energy_link", EnergyLinkBlockEntity::new, ProjectEXBlocks.ENERGY_LINK);
    public static final RegistryObject<BlockEntityType<PersonalLinkBlockEntity>> PERSONAL_LINK
            = register("personal_link", PersonalLinkBlockEntity::new, ProjectEXBlocks.PERSONAL_LINK);
    public static final RegistryObject<BlockEntityType<RefinedLinkBlockEntity>> REFINED_LINK
            = register("refined_link", RefinedLinkBlockEntity::new, ProjectEXBlocks.REFINED_LINK);
    public static final RegistryObject<BlockEntityType<CompressedRefinedLinkBlockEntity>> COMPRESSED_REFINED_LINK
            = register("compressed_refined_link", CompressedRefinedLinkBlockEntity::new, ProjectEXBlocks.COMPRESSED_REFINED_LINK);
    public static final RegistryObject<BlockEntityType<AlchemyTableBlockEntity>> ALCHEMY_TABLE
            = register("alchemy_table", AlchemyTableBlockEntity::new, ProjectEXBlocks.ALCHEMY_TABLE);

    public static final RegistryObject<BlockEntityType<CollectorBlockEntity>> COLLECTOR
            = register("collector", CollectorBlockEntity::new, ProjectEXBlocks.COLLECTOR.values());
    public static final RegistryObject<BlockEntityType<RelayBlockEntity>> RELAY
            = register("relay", RelayBlockEntity::new, ProjectEXBlocks.RELAY.values());
    public static final RegistryObject<BlockEntityType<PowerFlowerBlockEntity>> POWER_FLOWER
            = register("power_flower", PowerFlowerBlockEntity::new, ProjectEXBlocks.POWER_FLOWER.values());

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, RegistryObject<? extends Block> block) {
        return register(name, supplier, Set.of(block));
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Collection<RegistryObject<? extends Block>> blocks) {
        return REGISTRY.register(name, () -> new BlockEntityType<T>(supplier, blocks.stream().map(RegistryObject::get).collect(Collectors.toSet()), null));
    }
}
