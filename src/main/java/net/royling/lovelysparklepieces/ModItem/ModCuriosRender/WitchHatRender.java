package net.royling.lovelysparklepieces.ModItem.ModCuriosRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class WitchHatRender implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        LivingEntity entity = slotContext.entity();
        poseStack.pushPose();
        ModelPart hearpart = null;
        if(renderLayerParent.getModel()instanceof HumanoidModel<?> humanoidModel){
            hearpart = humanoidModel.head;
        }
        if(hearpart!=null){
            ICurioRenderer.followHeadRotations(entity,hearpart);
            hearpart.translateAndRotate(poseStack);
        }
        //poseStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
        //poseStack.mulPose(Axis.YP.rotationDegrees(headPitch));
        poseStack.translate(0,-0.3,0);
        float scale = 0.625f;
        poseStack.scale(scale,scale,scale);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(
                entity,
                stack,
                ItemDisplayContext.HEAD,
                false,
                poseStack,
                renderTypeBuffer,
                entity.level(),
                light,
                OverlayTexture.NO_OVERLAY,
                0
        );
        poseStack.popPose();
    }
}
