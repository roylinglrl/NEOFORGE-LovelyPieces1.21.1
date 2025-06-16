package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;

import java.util.List;

public class FireballStaffItem extends Item {
    public FireballStaffItem(Properties properties) {
        super(properties.stacksTo(1).durability(250));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if(!level.isClientSide){
            Vec3 lookVec = player.getLookAngle();
            double x = player.getX() + lookVec.x;
            double y = player.getY() + player.getEyeHeight() - 0.1D + lookVec.y;
            double z = player.getZ() + lookVec.z;
            SmallFireball fireball = new SmallFireball(level,player,new Vec3(1,1,1));
            fireball.setPos(x,y,z);
            fireball.setDeltaMovement(lookVec.scale(1.5));
            level.addFreshEntity(fireball);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            stack.hurtAndBreak(1,player, EquipmentSlot.MAINHAND);
            player.getCooldowns().addCooldown(stack.getItem(),20);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level3").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fire_ball_staff.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.fire_ball_staff.des1").withColor(ColorUtil.getRainbow(2f)));
    }
}
