package net.royling.lovelysparklepieces.ModEntity.Butterfly;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulButterflyEntity extends PathfinderMob {
    private UUID ownerUUID;
    private final AnimationState flyAnimationState = new AnimationState();
    private final AnimationState idleAnimationState = new AnimationState();
    private static final Map<UUID, Integer> ownerButterflyCount = new HashMap<>();
    private LivingEntity owner;
    private static final int RETRY_INTERVAL = 20;
    private int retryTimer = 0;
    private LivingEntity attackTarget;
    public static final double ATTACK_RANGE = 16.0;
    public int attackCooldown = 0;
    public SoulButterflyEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
    public void setOwner(LivingEntity owner) {
        this.owner = owner;
        if (owner != null) {
            increaseButterflyCount(owner);
        }
    }
    public static int getButterflyCountByOwner(LivingEntity owner) {
        return ownerButterflyCount.getOrDefault(owner.getUUID(), 0);
    }
    private void decreaseButterflyCount(LivingEntity owner) {
        if (owner == null) return;
        UUID ownerId = owner.getUUID();
        int currentCount = ownerButterflyCount.getOrDefault(ownerId, getActualButterflyCount((Player) owner));
        if (currentCount > 0) {
            ownerButterflyCount.put(ownerId, currentCount - 1);
        } else {
            ownerButterflyCount.remove(ownerId);
        }
    }
    public static int getButterflyCountByOwner(Player player) {
        // 优先使用缓存计数，但验证其准确性
        int cachedCount = ownerButterflyCount.getOrDefault(player.getUUID(), 0);
        int actualCount = getActualButterflyCount(player);
        // 如果缓存计数与实际计数不一致，使用实际计数
        if (cachedCount != actualCount) {
            ownerButterflyCount.put(player.getUUID(), actualCount);
            return actualCount;
        }
        return cachedCount;
    }
    private void increaseButterflyCount(LivingEntity owner) {
        if (owner == null) return;
        UUID ownerId = owner.getUUID();
        int currentCount = ownerButterflyCount.getOrDefault(ownerId, getActualButterflyCount((Player) owner));
        ownerButterflyCount.put(ownerId, currentCount + 1);
    }
    public LivingEntity getOwner() {
        if (owner == null && ownerUUID != null && level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) level()).getEntity(ownerUUID);
            if (entity instanceof LivingEntity) {
                owner = (LivingEntity) entity;
            }
        }
        return owner;
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
    public static void resetCountForPlayer(Player player) {
        ownerButterflyCount.remove(player.getUUID());
    }
    public static int getActualButterflyCount(Player player) {
        if (player.level().isClientSide) return 0;
        int count = 0;
        MinecraftServer server = player.getServer();
        if (server == null) return 0;
        // 遍历所有维度
        for (ServerLevel level : server.getAllLevels()) {
            for (SoulButterflyEntity butterfly : level.getEntitiesOfClass(SoulButterflyEntity.class, player.getBoundingBox().inflate(1000))) {
                if (butterfly.getOwner() != null &&
                        butterfly.getOwner().getUUID().equals(player.getUUID())) {
                    count++;
                }
            }
        }
        return count;
    }
    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation1 = new FlyingPathNavigation(this,level);
        navigation1.setCanOpenDoors(false);
        navigation1.setCanFloat(true);
        navigation1.setCanPassDoors(true);
        navigation1.setSpeedModifier(1.5f);
        return navigation1;
    }
    @Override
    public void travel(Vec3 travelVector) {
            this.setNoGravity(true);
        this.fallDistance = 0.0F;
            super.travel(travelVector);
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10,true,false,entity->entity instanceof Enemy));
        this.goalSelector.addGoal(2, new SoulButterflyAttackGoal(this, 8d));
        this.goalSelector.addGoal(3,new SoulButterflyFollowOwnerGoal(this,5d));
    }
    public void teleportTo(double x, double y, double z) {
        // 强制设置位置并重置运动
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.getNavigation().stop();

        // 粒子效果（可选）
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(
                    ParticleTypes.PORTAL,
                    this.getX(), this.getY(), this.getZ(),
                    10, 0.5, 0.5, 0.5, 0.1
            );
        }
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (owner != null) {
            compound.putUUID("OwnerUUID", owner.getUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("OwnerUUID")) {
            ownerUUID = compound.getUUID("OwnerUUID");
        }
    }
    @Override
    public void tick() {
        super.tick();
        LivingEntity butterfly = this;
        if (butterfly.level().isClientSide) {
            if (this.isFlying()) {
                this.idleAnimationState.stop();
                this.flyAnimationState.startIfStopped(this.tickCount);
            } else {
                this.flyAnimationState.stop();
                this.idleAnimationState.startIfStopped(this.tickCount);
            }
            butterfly.level().addParticle(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    butterfly.getX(),
                    butterfly.getY() + 0.1,
                    butterfly.getZ(),
                    (butterfly.getDeltaMovement().x * 0.1),
                    (butterfly.getDeltaMovement().y * 0.1),
                    (butterfly.getDeltaMovement().z * 0.1)
            );
        }
        if(level().isClientSide)return;
        if (!this.level().isClientSide && owner == null && ownerUUID != null) {
            if (retryTimer <= 0) {
                Entity entity = ((ServerLevel) this.level()).getEntity(ownerUUID);
                if (entity instanceof LivingEntity livingOwner) {
                    owner = livingOwner; // 找到后设置owner
                } else {
                    retryTimer = RETRY_INTERVAL; // 未找到则重置计时器
                }
            } else {
                retryTimer--;
            }
            if(attackCooldown>0){
                attackCooldown--;
            }
        }
        if(owner == null)this.discard();
        if (this.horizontalCollision || this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.2));
        }
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 2.2)
                .add(Attributes.FLYING_SPEED,3)
                .add(Attributes.ATTACK_DAMAGE,8);
    }
    public boolean isFlying() {
        return !this.onGround(); // 简单状态判断
    }
    public AnimationState getAnimationState() {
        return this.isFlying() ? flyAnimationState : idleAnimationState;
    }
    @Override
    public void remove(RemovalReason reason) {
        if (owner != null) {
            decreaseButterflyCount(owner);
        }
        super.remove(reason);
    }



}
