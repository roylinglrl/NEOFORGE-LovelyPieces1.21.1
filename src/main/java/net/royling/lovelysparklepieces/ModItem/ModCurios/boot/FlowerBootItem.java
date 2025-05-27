package net.royling.lovelysparklepieces.ModItem.ModCurios.boot;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class FlowerBootItem extends UniversalCurio {
    public FlowerBootItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity().tickCount % 10 != 0) return;
        if(slotContext.entity() instanceof Player player){
            BlockPos block = player.blockPosition().offset(0,-1,0);
            if(player.level().getBlockState(block).is(Blocks.DIRT))player.level().setBlockAndUpdate(block,Blocks.GRASS_BLOCK.defaultBlockState());
            if(player.level().getBlockState(block).is(Blocks.COARSE_DIRT))player.level().setBlockAndUpdate(block,Blocks.DIRT.defaultBlockState());
            if(player.level().getBlockState(block).is(Blocks.GRASS_BLOCK)){
                player.level().setBlockAndUpdate(player.blockPosition(),Blocks.SHORT_GRASS.defaultBlockState());
            }

        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level1"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.flowerboot.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.flowerboot.des2").withColor(ColorUtil.getRainbow()));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
