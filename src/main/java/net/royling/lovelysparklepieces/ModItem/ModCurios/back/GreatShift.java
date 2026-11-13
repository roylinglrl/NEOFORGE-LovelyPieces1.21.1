package net.royling.lovelysparklepieces.ModItem.ModCurios.back;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;

import java.util.List;

public class GreatShift extends UniversalCurio {
    public GreatShift(Properties properties) {
        super(properties.durability(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.great_shift.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.great_shift.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.great_shift.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.great_shift.des3").withColor(ColorUtil.getRainbow(2f)));
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return super.getUseDuration(stack, entity);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}


