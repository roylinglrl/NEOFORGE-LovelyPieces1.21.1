package net.royling.lovelysparklepieces.ModEntity.Butterfly;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SoulButterflyAttackGoal extends Goal {
    private final SoulButterflyEntity butterfly;
    private final double speed;
    private int attackCooldown;

    public SoulButterflyAttackGoal(SoulButterflyEntity butterfly, double speed) {
        this.butterfly = butterfly;
        this.speed = speed;
        this.attackCooldown = 0;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    @Override
    public boolean canUse() {
        LivingEntity target = butterfly.getTarget();
        return target != null && target.isAlive() && butterfly.distanceToSqr(target) <= SoulButterflyEntity.ATTACK_RANGE * SoulButterflyEntity.ATTACK_RANGE;
    }
    @Override
    public boolean canContinueToUse() {
        return canUse();
    }
    @Override
    public void tick() {
        LivingEntity target = butterfly.getTarget();
        if (target != null) {
            System.out.println(target);
            butterfly.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--attackCooldown <= 0) {
                attackCooldown = 5;
                if (butterfly.distanceToSqr(target) <= 4) {
                    Level level = butterfly.level();
                    // 造成魔法伤害
                    target.hurt(butterfly.getOwner().damageSources().indirectMagic(butterfly, butterfly.getOwner()),8.0f);
                    target.invulnerableTime = 0;
                    level.playSound(null, butterfly.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.HOSTILE, 1.0f, 1.0f);
                    // 生成粒子效果（类似魔法击中）
                    ((ServerLevel) level).sendParticles(ParticleTypes.ENCHANT,
                            target.getX(), target.getY() + 1, target.getZ(),
                            20, 0.3, 0.5, 0.3, 0.1);
                    // 数量、偏移、速度
                    // 消耗自身
                    butterfly.discard();
                } else {
                    Vec3 direction = target.position().subtract(butterfly.position()).normalize().scale(0.8);
                    butterfly.setDeltaMovement(direction);
                    butterfly.hasImpulse = true;
                }
            }
        }
    }
    @Override
    public void stop() {
        butterfly.getNavigation().stop();
        attackCooldown = 0;
    }
}
