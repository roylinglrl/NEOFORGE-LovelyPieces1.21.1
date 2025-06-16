package net.royling.lovelysparklepieces.ModDimAndBiome;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModDimension {
    @SubscribeEvent
    public static void registerDimension(RegisterDimensionSpecialEffectsEvent event){
        DimensionSpecialEffects wildFireLand =
                new DimensionSpecialEffects(192,true,
                DimensionSpecialEffects.SkyType.END,false,false
                ) {
                    @Override
                    public Vec3 getBrightnessDependentFogColor(Vec3 vec3, float v) {
                        return vec3;
                    }

                    @Override
                    public boolean isFoggyAt(int i, int i1) {
                        return false;
                    }
                };
        event.register(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"the_wildfireland"),wildFireLand);
    }
}
