package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;

import java.util.List;

public class PolymerizationItem extends Item {
    public static int MAX_DAMAGE = 12;
    public PolymerizationItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON).durability(MAX_DAMAGE));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int damage = itemStack.getDamageValue();
        if(itemStack.getDamageValue()+1>=itemStack.getMaxDamage()){
            return ItemStack.EMPTY;
        }
        ItemStack newStack = new ItemStack(itemStack.getItem());
        newStack.setDamageValue(itemStack.getDamageValue()+1);
        return newStack;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.polymerization.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.polymerization.des2").withColor(ColorUtil.getRainbow()));
    }
}
