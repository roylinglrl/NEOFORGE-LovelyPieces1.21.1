package net.royling.lovelysparklepieces.ModEntity.Client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterfly;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyEntity;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.MOD)
public class ModEntityClientEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.BUTTERFLY.get(), SoulButterflyEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SoulButterfly.LAYER_LOCATION, SoulButterfly::createBodyLayer);
    }
}
