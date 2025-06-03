package net.royling.lovelysparklepieces.ModEntity.Bullet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
    // 指向你的子弹纹理
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "textures/entity/bullet.png");
    // 可以选择一个合适的RenderType，entityCutoutNoCull 适合不透明边缘的精灵
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(BulletEntity entity) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void render(BulletEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // Billboard 效果 - 使精灵始终面向相机
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        // 如果你的纹理不是正对着前方，可能需要额外旋转
        // 例如，如果纹理是平躺的，需要绕X轴旋转90度:
        // poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        // 如果纹理是上下颠倒的:
        // poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        // 调整子弹精灵的大小
        float scale = 0.05f;
        // 根据你的纹理和期望大小调整
        poseStack.scale(scale, scale, scale);

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
        int glowingLight = 0xF000F0;
        // 绘制一个四边形 (quad)
        // 顶点顺序: BL, BR, TR, TL (底左, 底右, 顶右, 顶左)
        // UV 纹理坐标: (0,1), (1,1), (1,0), (0,0)
        // X, Y, Z, R, G, B, A, U, V, Overlay, Light, NormalX, NormalY, NormalZ

        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, -0.5F, -0.5F, 0.0F, 0.0F, 1.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, 0.5F, -0.5F, 0.0F, 1.0F, 1.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, 0.5F, 0.5F, 0.0F, 1.0F, 0.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, -0.5F, 0.5F, 0.0F, 0.0F, 0.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

    }
    private static void addVertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normalMatrix, int lightmapUV,
                                  float x, float y, float z, float u, float v) {
        consumer.addVertex(pose, x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightmapUV)
                .setNormal(0.0F, 1.0F, 0.0F);
    }
}
