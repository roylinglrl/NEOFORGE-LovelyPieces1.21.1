package net.royling.lovelysparklepieces.ModItem.ModCurios.body;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;

import java.util.List;

public class PDAItem extends UniversalCurio {
    public PDAItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level6"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.pda.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.pda.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.pda.des3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.pda.des4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.pda.des5").withColor(ColorUtil.getRainbow()));
    }
}
