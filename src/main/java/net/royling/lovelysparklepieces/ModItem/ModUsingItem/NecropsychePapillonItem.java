package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyEntity;

import java.util.List;

public class NecropsychePapillonItem extends Item {
    public NecropsychePapillonItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(level.isClientSide)return InteractionResultHolder.success(player.getItemInHand(usedHand));
        SoulButterflyEntity soulButterflyEntity = ModEntities.BUTTERFLY.get().create(level);
        if(soulButterflyEntity!=null){
            double x = player.getX() + level.random.nextGaussian();
            double y = player.getY() + 1.0;
            double z = player.getZ() + level.random.nextGaussian();
            soulButterflyEntity.moveTo(x, y, z, player.getYRot(), 0.0F);
            soulButterflyEntity.setOwner(player);
            level.addFreshEntity(soulButterflyEntity);
        }
        //player.getCooldowns().addCooldown(this,60);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand),level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level12"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp6").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp5").withColor(ColorUtil.getRainbow()));

    }
}
