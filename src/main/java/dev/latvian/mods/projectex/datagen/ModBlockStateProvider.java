package dev.latvian.mods.projectex.datagen;

import dev.latvian.mods.projectex.block.ProjectEXBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ProjectEXBlocks.ENERGY_LINK.get());
        simpleBlock(ProjectEXBlocks.PERSONAL_LINK.get());
        simpleBlock(ProjectEXBlocks.REFINED_LINK.get());
        simpleBlock(ProjectEXBlocks.COMPRESSED_REFINED_LINK.get());

        ProjectEXBlocks.COLLECTOR.forEach((k, v) ->
                simpleBlock(v.get(), models().cubeAll(v.get().getRegistryName().getPath(), modLoc("block/collector/" + k.name))));
        ProjectEXBlocks.RELAY.forEach((k, v) ->
                simpleBlock(v.get(), models().cubeAll(v.get().getRegistryName().getPath(), modLoc("block/relay/" + k.name))));
        ProjectEXBlocks.POWER_FLOWER.forEach((k, v) ->
                simpleBlock(v.get(), models().getBuilder(k.name + "_power_flower").parent(models().getExistingFile(modLoc("block/power_flower"))).texture("collector", modLoc("block/collector/" + k.name)).texture("relay", modLoc("block/relay/" + k.name))));

        getVariantBuilder(ProjectEXBlocks.STONE_TABLE.get())
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(models().getExistingFile(modLoc("block/stone_table")))
                            .rotationX(dir == Direction.DOWN ? 0 : dir.getAxis().isHorizontal() ? 90 : 180)
                            .rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot()) % 360)
                            .build();
                });

        simpleBlock(ProjectEXBlocks.ALCHEMY_TABLE.get(), models().getExistingFile(modLoc("block/alchemy_table")));
    }
}
