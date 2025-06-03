package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;

import java.util.List;

public class CapitalistCake extends Item {
    public CapitalistCake(Properties properties) {
        super(properties.stacksTo(64).food(
                new FoodProperties.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build()
        ));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_cake.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_cake.des1").withColor(ColorUtil.getRainbow(2f)));
    }
}
