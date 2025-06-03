package net.royling.lovelysparklepieces.ModEntity.Bullet;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

public class BulletEntity extends ThrowableProjectile {
    private double damageAmount = 2.0D;
    private BulletDamageType currentDamageType = BulletDamageType.PROJECTILE;
    private boolean isNoIv = false;



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
    @Override
    public boolean isPickable() {
        return false;
    }
}
