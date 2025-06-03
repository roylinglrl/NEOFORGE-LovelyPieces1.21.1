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
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEntity.Bullet.BulletEntity;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.DBSHCountData;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ModDataComponents;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ShootCDData;

import java.util.List;

public class DemonBreakingSilverHunter extends GunItem {
    public DemonBreakingSilverHunter(Properties properties) {
        super(properties.stacksTo(1).durability(510));
    }

    @Override
    public double getShootDamage() {
        return 7;
    }

    @Override
    public SoundEvent getShootSound() {
        return SoundEvents.BAMBOO_BREAK;
    }

    @Override
    public boolean isAuto() {
        return false;
    }

    @Override
    public void performShoot(Level level, Player player, InteractionHand usedHand, ItemStack gunstack){
        if (!level.isClientSide) {
            if(gunstack.getOrDefault(ModDataComponents.SHOOT_COOLDOWN.get(),new ShootCDData(0)).shootCooldown()!=0) {
                return;
            }
            // 新增：检查并消耗子弹
            boolean hasBullet = false;
            // 优先检查快捷栏 (0-8)
            for (int i = 0; i < 9; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (stack.getItem() instanceof FlintlockBulletItem) {
                    stack.shrink(1);
                    hasBullet = true;
                    break;
                }
            }

            // 如果快捷栏没有，检查背包剩余部分 (9-35)
            if (!hasBullet) {
                for (int i = 9; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof FlintlockBulletItem) {
                        stack.shrink(1);
                        hasBullet = true;
                        break;
                    }
                }
            }
            if (!hasBullet) {
                return;
            }
            gunstack.hurtAndBreak(1,player, EquipmentSlot.MAINHAND);
            int currentModeIndex = gunstack.getOrDefault(
                    ModDataComponents.MAKAROV_COUNT.get(),
                    new DBSHCountData(DBSHCountData.DEFAULT_MODE_INDEX)
            ).modeIndex();
            BulletEntity.BulletDamageType bulletDamageType;
            if(currentModeIndex == 0){
                bulletDamageType = BulletEntity.BulletDamageType.MAGIC;
            }else {
                bulletDamageType = BulletEntity.BulletDamageType.SILVER;
            }
            BulletEntity bullet = new BulletEntity(level, player,getShootDamage(), bulletDamageType);
            Vec3 lookVec = player.getLookAngle();
            Vec3 upVec = new Vec3(0, 1, 0);
            Vec3 rightVec = lookVec.cross(upVec).normalize().scale(0.3);

            bullet.setPos(
                    bullet.getX() + rightVec.x,
                    bullet.getY() + rightVec.y,
                    bullet.getZ() + rightVec.z
            );
            float angle = 1.4f;
            bullet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, angle);
            level.addFreshEntity(bullet);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), getShootSound(), SoundSource.PLAYERS, 1.0F, 1.0F);
            gunstack.set(ModDataComponents.SHOOT_COOLDOWN.get(),new ShootCDData(10));
            int nextModeIndex = (currentModeIndex + 1) % 2;
            gunstack.set(ModDataComponents.MAKAROV_COUNT.get(), new DBSHCountData(nextModeIndex));
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

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.inf").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.inf1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.inf2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.inf3").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.des3").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.makarov.des4").withColor(ColorUtil.getRainbow(2f)));
    }
}
