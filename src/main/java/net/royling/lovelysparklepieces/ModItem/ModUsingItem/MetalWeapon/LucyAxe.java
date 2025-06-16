package net.royling.lovelysparklepieces.ModItem.ModUsingItem.MetalWeapon;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.SimpleTier;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;

import java.util.List;

public class LucyAxe extends AxeItem {
    public LucyAxe() {
        super(LUCY_AXE,new Properties().stacksTo(1).attributes(AxeItem.createAttributes(LUCY_AXE,5,-3.1f)));
    }
    public static final Tier LUCY_AXE = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL,
            2147483647,18,0f,16,()-> Ingredient.of(Items.DIAMOND));

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if(state.is(BlockTags.LOGS)){
            if (miningEntity.getRandom().nextDouble()<0.38){
                int messageIndex = miningEntity.getRandom().nextInt(20);
                String translationKey = "item." + LovelySparklePieces.MODID + ".lucy_axe.message." + messageIndex;
                Component message = Component.translatable(translationKey)
                        .withColor(0xff3500);
                miningEntity.sendSystemMessage(message);
            }
        }else if(state.is(Blocks.BROWN_MUSHROOM_BLOCK)||state.is(Blocks.RED_MUSHROOM_BLOCK)||state.is(Blocks.MUSHROOM_STEM)){
            if (miningEntity.getRandom().nextDouble()<0.38){
                int messageIndex = miningEntity.getRandom().nextInt(4);
                String translationKey = "item." + LovelySparklePieces.MODID + ".lucy_axe.message.mushroom." + messageIndex;
                Component message = Component.translatable(translationKey)
                        .withColor(0xff3500);
                miningEntity.sendSystemMessage(message);
            }
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.lucy_axe.des").withColor(ColorUtil.getRainbow()));
    }
}
