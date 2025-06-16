package net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEntity.Bullet.BulletEntity;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ModDataComponents;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ShootCDData;

import java.util.List;

public class MiniGunItem extends GunItem {
    public MiniGunItem(Properties properties) {
        super(properties.stacksTo(1).durability(3846));
    }

    @Override
    public boolean isAuto() {
        return true;
    }

    @Override
    protected double getShootDamage() {
        return 2;
    }

    @Override
    protected SoundEvent getShootSound() {
        return SoundEvents.BAMBOO_BREAK;
    }

    @Override
    public void performShoot(Level level, Player player, InteractionHand usedHand, ItemStack gunStack) {
        if (!level.isClientSide) {
            if(gunStack.getOrDefault(ModDataComponents.SHOOT_COOLDOWN.get(),new ShootCDData(0)).shootCooldown()!=0) {
                return;
            }
            boolean hasBullet = false;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof FlintlockBulletItem) {
                    if(player.getRandom().nextDouble()> (double) 1 /3) {
                        stack.shrink(1);
                    }
                    hasBullet = true;
                    break;
                }
            }
            if (!hasBullet) {
                for (int i = 9; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof FlintlockBulletItem) {
                        if(player.getRandom().nextDouble()> (double) 1 /3) {
                            stack.shrink(1);
                        }
                        hasBullet = true;
                        break;
                    }
                }
            }
            if (!hasBullet) {
                return;
            }
            gunStack.hurtAndBreak(1,player, EquipmentSlot.MAINHAND);
            BulletEntity bullet = new BulletEntity(level, player,getShootDamage(), BulletEntity.BulletDamageType.PROJECTILE);
            bullet.setNoIv(true);
            float angle = 2.8f;
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, angle);
            level.addFreshEntity(bullet);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            gunStack.set(ModDataComponents.SHOOT_COOLDOWN.get(),new ShootCDData(2));

        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        int shootCount = stack.getOrDefault(
                ModDataComponents.SHOOT_COOLDOWN.get(),
                new ShootCDData(0)
        ).shootCooldown();
        if(shootCount>0) {
            stack.set(ModDataComponents.SHOOT_COOLDOWN.get(),new ShootCDData(shootCount-1));
        }
    }
    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.inf").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.inf1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.inf2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.inf3").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.minigun.des3").withColor(ColorUtil.getRainbow(2f)));

    }
}
