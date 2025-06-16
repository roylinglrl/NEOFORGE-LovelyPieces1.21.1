package net.royling.lovelysparklepieces.datagenerator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LovelySparklePieces.MODID, bus = EventBusSubscriber.Bus.MOD)

public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        generator.addProvider(event.includeClient(),ModLootTableProvider.create(packOutput,lookupProvider));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(),new BlockStateGene(packOutput,existingFileHelper));
        generator.addProvider(event.includeClient(),new ModRecipeProvider(packOutput,lookupProvider));
        generator.addProvider(event.includeServer(),new ModDatapack(packOutput,lookupProvider));
        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new ModBlockTagGenerator(packOutput,lookupProvider,existingFileHelper));
        generator.addProvider(event.includeServer(),new ModItemTagProvider(packOutput,lookupProvider,blockTagGenerator.contentsGetter(),existingFileHelper));

    }
}
