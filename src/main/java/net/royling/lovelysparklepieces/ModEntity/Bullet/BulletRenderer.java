package net.royling.lovelysparklepieces.ModEntity.Bullet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "textures/entity/bullet.png");
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

        // 计算平滑的旋转角度（使用部分tick进行插值）
        float yaw = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.rotLerp(partialTick, entity.xRotO, entity.getXRot());

        // 应用旋转：先绕Y轴旋转，再绕X轴旋转
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));

        // 如果子弹处于追踪模式，添加一个小的旋转动画
        if (entity.getTrack()) {
            float spin = (entity.tickCount + partialTick) * 20.0F; // 旋转速度
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
        }

        // 调整子弹大小
        float scale = 0.05f;
        poseStack.scale(scale, scale, scale);

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
        int glowingLight = 0xF000F0;

        // 绘制子弹（正面）
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, -0.5F, -0.5F, 0.0F, 0.0F, 1.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, 0.5F, -0.5F, 0.0F, 1.0F, 1.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, 0.5F, 0.5F, 0.0F, 1.0F, 0.0F);
        addVertex(vertexconsumer, matrix4f, matrix3f, glowingLight, -0.5F, 0.5F, 0.0F, 0.0F, 0.0F);

        // 绘制子弹（背面）
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F)); // 旋转180度绘制背面
        pose = poseStack.last();
        matrix4f = pose.pose();
        matrix3f = pose.normal();

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
