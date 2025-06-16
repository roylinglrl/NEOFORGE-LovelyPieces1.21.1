package net.royling.lovelysparklepieces.ModItem.ModUsingItem.MetalWeapon;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;

import java.util.List;

public class GravediggersShovel extends ShovelItem {
    public GravediggersShovel() {
        super(GRAVEDIGGER,new Properties().stacksTo(1).attributes(AxeItem.createAttributes(GRAVEDIGGER,3,-3.0f)));
    }
    public static final Tier GRAVEDIGGER = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL,
            2147483647,18,0f,16,()-> Ingredient.of(Items.DIAMOND));

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gravedigger.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gravedigger.des1").withColor(ColorUtil.getRainbow()));
    }
}
