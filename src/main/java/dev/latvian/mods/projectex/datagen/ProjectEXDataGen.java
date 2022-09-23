package dev.latvian.mods.projectex.datagen;

import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ProjectEXDataGen {
    public static final String MODID = ProjectEX.MOD_ID;

    @SubscribeEvent
    public static void dataGenEvent(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new ModLangProvider(gen, MODID, "en_us"));
            gen.addProvider(new ModBlockStateProvider(gen, MODID, event.getExistingFileHelper()));
            gen.addProvider(new ModBlockModelProvider(gen, MODID, event.getExistingFileHelper()));
            gen.addProvider(new ModItemModelProvider(gen, MODID, event.getExistingFileHelper()));
        }

        if (event.includeServer()) {
            ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, MODID, efh);
            gen.addProvider(blockTags);
            gen.addProvider(new ModItemTagsProvider(gen, blockTags, MODID, efh));
            gen.addProvider(new ModRecipeProvider(gen));
            gen.addProvider(new ModLootTableProvider(gen));
        }
    }
}
