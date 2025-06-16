package net.royling.lovelysparklepieces.ModEntity.Bullet;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BulletEntity extends ThrowableProjectile {
    private double damageAmount = 2.0D;
    private BulletDamageType currentDamageType = BulletDamageType.PROJECTILE;
    private boolean isNoIv = false;
    private boolean isTrack = false;


    public enum BulletDamageType {
        PROJECTILE,
        LIGHTNING,
        MAGIC,
        FIRE,
        PURE_FIRE,
        PURE_LIGHTNING,
        SILVER
    }
    public BulletEntity(EntityType<? extends BulletEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BulletEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BULLET.get(), shooter, level);
    }

    public BulletEntity(Level level, double x, double y, double z) {
        super(ModEntities.BULLET.get(), x, y, z, level);
    }

    public BulletEntity(Level level, LivingEntity shooter, BulletDamageType damageType) {
        super(ModEntities.BULLET.get(), shooter, level);
        this.currentDamageType = damageType;
    }
    public BulletEntity(Level level, LivingEntity shooter, double damage, BulletDamageType damageType) {
        super(ModEntities.BULLET.get(), shooter, level);
        this.damageAmount = damage;
        this.currentDamageType = damageType;
    }
    public void setDamageType(BulletDamageType damageType) {
        this.currentDamageType = damageType;
    }

    public void setDamage(double damage) {
        this.damageAmount = damage;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity owner = this.getOwner();

        if (owner != null && target.is(owner) && this.level().isClientSide) {
            // 防止击中自己 (主要用于客户端预测)
            return;
        }
        if (target instanceof LivingEntity livingTarget) {
            DamageSource source;
            switch (this.currentDamageType) {
                case PURE_LIGHTNING:
                    source = this.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE,
                            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"pure_lightning_damage")),this, owner);
                    break;
                case PURE_FIRE:
                    source = this.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE,
                            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"pure_fire_damage")), this,owner);
                    break;
                case SILVER:
                    source =this.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE,
                            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"silver_damage")), this,owner);
                    break;
                case LIGHTNING:
                    source = this.damageSources().lightningBolt();
                    break;
                case MAGIC:
                    source = this.damageSources().indirectMagic(this, owner);
                    break;
                case FIRE:
                    source = this.damageSources().inFire();
                    break;
                case PROJECTILE:
                default:
                    source = this.damageSources().thrown(this, owner);
                    break;
            }
            // 对目标造成伤害，使用 thrown 伤害类型
            livingTarget.hurt(source, (float) this.damageAmount);
            if(isNoIv){
                livingTarget.invulnerableTime = 0;
            }
        }
        this.discard();
    }


    //设置子弹是否会触发无敌时间
    public void setNoIv(boolean noIv){
        this.isNoIv = noIv;
    }
    public void setTrack(boolean isTrack){this.isTrack = isTrack;}
    public boolean getTrack(){
        return this.isTrack;
    }


    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
         if (!this.level().isClientSide) {
             this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BAMBOO_BREAK, SoundSource.NEUTRAL, 0.7F, 1.0F);
         }
         this.level().gameEvent(GameEvent.PROJECTILE_LAND, this.position(), GameEvent.Context.of(this));
        this.discard();

    }

    @Override
    protected double getDefaultGravity() {
        return 0.0d;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isTrack && this.tickCount >= 5) {
            trackNearestEnemy();
        }
        // 如果子弹存活时间过长，也让它消失
        if (this.tickCount > 100) {
            // 约 5 秒
            this.discard();
        }
        // 在客户端添加尾迹粒子效果
        if (this.level().isClientSide && !this.isNoGravity()) {
            // 或者直接判断速度是否显著
            // 例如: this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        }
    }
    private void trackNearestEnemy() {
        double searchRadius = 16.0; // 搜索半径
        Entity owner = this.getOwner();

        // 获取范围内所有生物实体
        List<LivingEntity> entities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(searchRadius),
                EntitySelector.NO_CREATIVE_OR_SPECTATOR
        );

        // 过滤条件：排除发射者、队友、死亡的实体
        List<LivingEntity> enemies = entities.stream()
                .filter(e -> e != owner)
                .filter(e -> !(owner instanceof Mob) || ((Mob) owner).getTarget() != e) // 非队友
                .filter(LivingEntity::isAlive)
                .collect(Collectors.toList());
        if (enemies.isEmpty()) return;

        // 找到最近的敌人
        LivingEntity target = enemies.stream()
                .min(Comparator.comparingDouble(e -> e.distanceToSqr(this)))
                .orElse(null);

        if (target != null) {
            // 计算方向向量
            Vec3 direction = target.position()
                    .add(0, target.getBbHeight() / 2, 0) // 瞄准身体中心
                    .subtract(this.position())
                    .normalize();

            // 设置速度（保持原速度大小）
            Vec3 currentMotion = this.getDeltaMovement();
            double speed = currentMotion.length();
            Vec3 newMotion = direction.scale(speed);

            this.setDeltaMovement(newMotion);

            // 更新子弹旋转角度
            this.setYRot((float) Math.toDegrees(Math.atan2(newMotion.x, newMotion.z)));
            this.setXRot((float) Math.toDegrees(Math.atan2(newMotion.y, Math.sqrt(newMotion.x * newMotion.x + newMotion.z * newMotion.z))));
        }
    }


    @Override
    public boolean isPickable() {
        return false;
    }
}
