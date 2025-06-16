package net.royling.lovelysparklepieces.ModItem.ModCurios.Heads;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class NightVisionItem extends UniversalCurio {
    public NightVisionItem(Properties properties) {
        super(properties.stacksTo(1).durability(3600));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity().level().isClientSide)return;
        if(slotContext.entity().tickCount%20!=0)return;
        if(stack.getMaxDamage()-stack.getDamageValue()>1) {
            slotContext.entity().addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 240, 0, false, false));
            stack.hurtAndBreak(1,slotContext.entity(), EquipmentSlot.MAINHAND);
        }
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        //slotContext.entity().removeEffect(MobEffects.NIGHT_VISION);
    }

    @Override@OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level2").withStyle(ChatFormatting.DARK_PURPLE));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.night_vision.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.night_vision.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.night_vision.des2").withColor(ColorUtil.getRainbow()));
    }

    @Override
    public float getXpRepairRatio(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return repairCandidate.getItem()== ModItems.BATTERY.get();
    }
}
