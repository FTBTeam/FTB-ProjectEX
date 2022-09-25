package dev.ftb.projectex.datagen;

import dev.ftb.projectex.Matter;
import dev.ftb.projectex.Star;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Star star : Star.VALUES) {
            singleTexture("magnum_star_" + star.name, mcLoc("item/generated"), "layer0", modLoc("item/magnum_star/" + star.name));
            singleTexture("colossal_star_" + star.name, mcLoc("item/generated"), "layer0", modLoc("item/colossal_star/" + star.name));
        }

        for (Matter matter : Matter.VALUES) {
            if (matter.hasMatterItem) {
                singleTexture(matter.name + "_matter", mcLoc("item/generated"), "layer0", modLoc("item/matter/" + matter.name));
            }
        }

        singleTexture("final_star_shard", mcLoc("item/generated"), "layer0", modLoc("item/final_star_shard"));
        singleTexture("final_star", mcLoc("item/generated"), "layer0", modLoc("item/final_star"));
        singleTexture("knowledge_sharing_book", mcLoc("item/generated"), "layer0", modLoc("item/knowledge_sharing_book"));
        singleTexture("arcane_tablet", mcLoc("item/generated"), "layer0", modLoc("item/arcane_tablet"));

        for (Matter matter : Matter.VALUES) {
            withExistingParent(matter.name + "_compressed_collector", modLoc("block/" + matter.name + "_collector"));
        }

        withExistingParent("energy_link", modLoc("block/energy_link"));
        withExistingParent("personal_link", modLoc("block/personal_link"));
        withExistingParent("refined_link", modLoc("block/refined_link"));
        withExistingParent("compressed_refined_link", modLoc("block/compressed_refined_link"));

        for (Matter matter : Matter.VALUES) {
            withExistingParent(matter.name + "_collector", modLoc("block/" + matter.name + "_collector"));
            withExistingParent(matter.name + "_relay", modLoc("block/" + matter.name + "_relay"));
            withExistingParent(matter.name + "_power_flower", modLoc("block/" + matter.name + "_power_flower"));
        }

        withExistingParent("stone_table", modLoc("block/stone_table"));
        withExistingParent("alchemy_table", modLoc("block/alchemy_table"));
    }
}
