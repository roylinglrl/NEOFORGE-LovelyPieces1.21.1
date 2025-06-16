package net.royling.lovelysparklepieces.ModEntity.Abigail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import org.jetbrains.annotations.Nullable;

public class AbigialRenderer extends MobRenderer<AbigailEntity, AbigailModel<AbigailEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "textures/entity/abigail.png");
    public AbigialRenderer(EntityRendererProvider.Context context) {
        super(context,new AbigailModel<>(context.bakeLayer(AbigailModel.LAYER_LOCATION)),0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbigailEntity abigialEntity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(AbigailEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(TEXTURE);
    }

    @Override
    public void render(AbigailEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        int fullBrightLight = 0xF000F0;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, fullBrightLight);
    }
}
