package net.royling.lovelysparklepieces.ModItem.ModCurios.Heads;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Objects;


public class FPSEyeItem extends UniversalCurio {
    public FPSEyeItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    public static double calculateDamageModifier(int fps) {
        if (fps <= 20) {return 0.5;} else if (fps < 120) {return 0.5 * (1 - (fps - 20) / 100.0);} else if (fps < 220) {return -0.5 * ((fps - 120) / 100.0);} else {return -0.5;}
    }
    ResourceLocation ATTID = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"fps_eye");
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity()instanceof Player player) {
            if (player.level().isClientSide) return;
            long fps = player.getPersistentData().getLong("lsp_fpsvalue");
            AttributeModifier newModifier = new AttributeModifier(ATTID,
                    calculateDamageModifier((int) fps), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
            Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(newModifier);
            Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).addOrReplacePermanentModifier(newModifier);
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            long fps = player.getPersistentData().getLong("lsp_fpsvalue");
            if(Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).getModifier(ATTID)==null){
                Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).addOrReplacePermanentModifier(new AttributeModifier(
                        ATTID,calculateDamageModifier((int) fps), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(ATTID);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level7"));
        int fps = Minecraft.getInstance().getFps();
        double modifier = calculateDamageModifier(fps);
        String percent = String.format("%+.1f%%", modifier * 100);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fpseye.basic").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fpseye.basic2",fps).withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fpseye.basic3",percent).withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fpseye.basic4",percent).withColor(ColorUtil.getRainbow()));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
