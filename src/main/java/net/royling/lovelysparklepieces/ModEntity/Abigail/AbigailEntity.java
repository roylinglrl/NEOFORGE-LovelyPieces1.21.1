package net.royling.lovelysparklepieces.ModEntity.Abigail;

import icyllis.modernui.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyFollowOwnerGoal;

import java.util.Optional;
import java.util.UUID;

public class AbigailEntity extends PathfinderMob  {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID =
            SynchedEntityData.defineId(AbigailEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    public AbigailEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }
    public void setOwner(Player owner) {
        this.entityData.set(OWNER_UUID, Optional.of(owner.getUUID()));
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10,true,false,entity->entity instanceof Enemy));
        this.goalSelector.addGoal(3,new AbigailFollowOwnerGoal(this,2.5d));
    }


    @Nullable
    public Player getOwner() {
        Optional<UUID> uuid = this.entityData.get(OWNER_UUID);
        if (uuid.isPresent() && this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getPlayerByUUID(uuid.get());
        }
        return null;
    }
    public boolean hasOwner() {
        return this.entityData.get(OWNER_UUID).isPresent();
    }
    public boolean isOwnedBy(Player player) {
        Optional<UUID> uuid = this.entityData.get(OWNER_UUID);
        return uuid.isPresent() && uuid.get().equals(player.getUUID());
    }
    @Override
    public void tick() {
        super.tick();
        // The owner discard logic can remain here as it's a fundamental state check
        if (!this.level().isClientSide && this.hasOwner()) {
            Player owner = getOwner();
            if (owner == null || owner.isDeadOrDying()) {
                this.discard();
            }
        }
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false; // 仍然免疫伤害
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
        this.fallDistance=0;
        super.travel(travelVector);
    }
    @Override
    public boolean isNoGravity() {
        return true;
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        // 保存主人UUID
        Optional<UUID> ownerUuid = this.entityData.get(OWNER_UUID);
        ownerUuid.ifPresent(uuid -> tag.putUUID("Owner", uuid));
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) {
            this.entityData.set(OWNER_UUID, Optional.of(tag.getUUID("Owner")));
        }
        if (!this.level().isClientSide) {
            this.registerGoals();
        }
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 2.2)
                .add(Attributes.FLYING_SPEED,3)
                .add(Attributes.ATTACK_DAMAGE,6);
    }

}
