package net.royling.lovelysparklepieces.ModEntity.Server;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyEntity;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.MOD)

public class ModEntityServerEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.BUTTERFLY.get(), SoulButterflyEntity.createAttributes().build());
    }
}
