package net.royling.lovelysparklepieces.ModEntity.Client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.Abigail.AbigailModel;
import net.royling.lovelysparklepieces.ModEntity.Abigail.AbigialRenderer;
import net.royling.lovelysparklepieces.ModEntity.Bullet.BulletRenderer;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyModel;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyRenderer;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ModEntityClientEvent {
    public static final ModelLayerLocation BULLET_LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "bullet"), "main");
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SoulButterflyModel.LAYER_LOCATION, SoulButterflyModel::createBodyLayer);
        event.registerLayerDefinition(AbigailModel.LAYER_LOCATION, AbigailModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BULLET.get(), BulletRenderer::new);
        event.registerEntityRenderer(ModEntities.ABIGAIL.get(), AbigialRenderer::new);
        event.registerEntityRenderer(ModEntities.BUTTERFLY.get(), SoulButterflyRenderer::new);
    }
}
