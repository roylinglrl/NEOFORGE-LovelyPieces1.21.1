package net.royling.lovelysparklepieces.ModEntity.Abigail;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class AbigailFollowOwnerGoal extends Goal {
    private final AbigailEntity abigail;
    private final double speed;
    private float currentAngle;

    public AbigailFollowOwnerGoal(AbigailEntity abigail, double speed) {
        this.abigail = abigail;
        this.speed = speed;
        this.currentAngle = abigail.getRandom().nextFloat() * 360;
    }
    @Override
    public boolean canUse() {
        return abigail.getOwner() != null&&abigail.getTarget() == null;
    }
    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }
    @Override
    public void tick() {
        if (abigail.getTarget() != null) {
            return;
        }
        LivingEntity owner = abigail.getOwner();
        if (owner == null) return;
        double distanceSq = abigail.distanceToSqr(owner);
        if (distanceSq > 24 * 24) {
            // 瞬移到玩家周围1格范围内
            Vec3 teleportPos = owner.position()
                    .add(
                            abigail.getRandom().nextDouble() * 2 - 1, // X: -1~1
                            0.5, // Y: 玩家腰部高度
                            abigail.getRandom().nextDouble() * 2 - 1  // Z: -1~1
                    );
            abigail.teleportTo(teleportPos.x, teleportPos.y, teleportPos.z);
            abigail.getNavigation().stop(); // 停止当前路径
            return; // 瞬移后跳过后续逻辑
        }
        // === 动态计算目标位置 ===
        double radius = 3.0;
        double verticalOffset = 1.5 + Math.sin(abigail.tickCount * 0.2); // 增大垂直波动
        currentAngle += 6.0;
        double radian = Math.toRadians(currentAngle % 360);
        Vec3 targetPos = owner.position().add(
                Math.cos(radian) * radius,
                verticalOffset,
                Math.sin(radian) * radius
        );
        abigail.getNavigation().moveTo(
                targetPos.x,
                targetPos.y,
                targetPos.z,
                speed * 2
        );
        Vec3 toTarget = targetPos.subtract(abigail.position()).normalize();
        abigail.setDeltaMovement(
                abigail.getDeltaMovement().add(
                        toTarget.x * 0.1,
                        toTarget.y * 0.1,
                        toTarget.z * 0.1
                )
        );
        // === 限制最大速度 ===
        Vec3 currentMotion = abigail.getDeltaMovement();
        if (currentMotion.length() > speed) {
            abigail.setDeltaMovement(currentMotion.normalize().scale(speed));
        }
        // === 面向目标 ===
        abigail.lookAt(EntityAnchorArgument.Anchor.EYES, targetPos);
    }

    @Override
    public void stop() {
        abigail.getNavigation().stop();
    }
}
