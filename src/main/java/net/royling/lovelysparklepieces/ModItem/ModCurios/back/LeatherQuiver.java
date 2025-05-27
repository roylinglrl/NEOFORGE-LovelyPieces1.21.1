package net.royling.lovelysparklepieces.ModItem.ModCurios.back;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;

import java.util.List;

public class LeatherQuiver extends UniversalCurio {
    public LeatherQuiver(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.leather_quiver.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.leather_quiver.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.leather_quiver.des2").withColor(ColorUtil.getRainbow()));
    }
}
