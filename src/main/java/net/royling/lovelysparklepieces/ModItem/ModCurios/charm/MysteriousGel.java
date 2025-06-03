package net.royling.lovelysparklepieces.ModItem.ModCurios.charm;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;

import java.util.List;

public class MysteriousGel extends UniversalCurio {
    public MysteriousGel(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.mysterious_gel.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.mysterious_gel.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.mysterious_gel.des2").withColor(ColorUtil.getRainbow(2f)));
    }
}
