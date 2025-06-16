package net.royling.lovelysparklepieces.ModEntity.Common;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.Abigail.AbigailEntity;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyEntity;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.MOD)

public class ModEntityCommonEvent {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.BUTTERFLY.get(), SoulButterflyEntity.createAttributes().build());
        event.put(ModEntities.ABIGAIL.get(), AbigailEntity.createAttributes().build());
    }
}
