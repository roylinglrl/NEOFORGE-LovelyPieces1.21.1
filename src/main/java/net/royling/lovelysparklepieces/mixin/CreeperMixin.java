package net.royling.lovelysparklepieces.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;


@Mixin(Creeper.class)
public class CreeperMixin {
    private static final int DETECTION_RANGE = 8;
    private static final int AMULET_DETECTION_RANGE = 10;

    private static final int QUESTION_MARK_DURATION_TICKS = 20 * 3; // 3 秒
    private long questionMarkDisappearTime = -1;
    public long getQuestionMarkDisappearTime() {
        return this.questionMarkDisappearTime;
    }

    // Utility method to cast 'this' to Creeper type
    private Creeper getThisCreeper() {
        return (Creeper) (Object) this;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Creeper;explodeCreeper()V"
            ),
            cancellable = true
    )
    private void onExplodeCheck(CallbackInfo ci) {
        Creeper creeper = (Creeper) (Object) this;
        Level level = creeper.level();
        if (!level.isClientSide && hasAbsorberNear(creeper.blockPosition(), level)) {
            handleAbsorption(creeper,(ServerLevel) level);
            ci.cancel();
            return;
        }
        if(!level.isClientSide&&hasFearAmuletNear(creeper,level)){
            handleCreeperFear(creeper,(ServerLevel)level);
            ci.cancel();
            return;
        }
        if(!level.isClientSide&&hasGreatShift(creeper,level)){
            handleDeathAvoidanceEffect(creeper, (ServerLevel) level);
            ci.cancel();
            return;
        }
    }

    private boolean hasAbsorberNear(BlockPos pos, Level level) {
        return BlockPos.betweenClosedStream(
                pos.offset(-DETECTION_RANGE, -DETECTION_RANGE, -DETECTION_RANGE),
                pos.offset(DETECTION_RANGE, DETECTION_RANGE, DETECTION_RANGE)
        ).anyMatch(checkPos ->
                level.getBlockState(checkPos).getBlock() == ModBlocks.BLAST_ABSORBER.get()
        );
    }

    private void handleAbsorption(Creeper creeper, ServerLevel level) {
        creeper.discard();
        ItemStack battery = new ItemStack(ModItems.BATTERY.get());
        RandomSource randomSource = level.getRandom();
        for (int i = 0; i < 2; i++) {
            ItemEntity item = new ItemEntity(
                    level,
                    creeper.getX(),
                    creeper.getY() + 0.5,
                    creeper.getZ(),
                    battery.copy()
            );
            item.setDeltaMovement(
                    (randomSource.nextDouble() - 0.5) * 0.2,
                    0.2 + randomSource.nextDouble() * 0.4,
                    (randomSource.nextDouble() - 0.5) * 0.2
            );
            level.addFreshEntity(item);
            level.playSound(null, creeper.blockPosition(),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.BLOCKS, 1.0F, 1.2F);
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    creeper.getX(), creeper.getY() + 0.5, creeper.getZ(),
                    15, 0.5, 0.5, 0.5, 0.1);
        }
    }

    private boolean hasFearAmuletNear(Creeper creeper,Level level){
        AABB detectionBox = creeper.getBoundingBox().inflate(AMULET_DETECTION_RANGE);
        List<? extends Player> players = level.getEntitiesOfClass(Player.class, detectionBox,
                player -> player != null && !player.isSpectator());
        for (Player player : players) {
            // 检查玩家是否佩戴了苦力怕怕护符
            // 假设 ModItems.CREEPER_FEAR_AMULET 是你的护符物品注册表对象
            boolean hasAmulet = ModCurios.hasCurio(player,ModCurios.CREEPFEAR_AMULET.get());
            if (hasAmulet) {
                CuriosApi.getCuriosInventory(player).
                        flatMap(inv -> inv.findFirstCurio(
                                ModCurios.CREEPFEAR_AMULET.get()))
                        .ifPresent(item -> item.stack().hurtAndBreak(1, player, EquipmentSlot.MAINHAND));
                return true; // 找到一个佩戴护符的玩家，立即返回
            }
        }
        return false; // 没有找到佩戴护符的玩家

    }
    private boolean hasGreatShift(Creeper creeper,Level level){
        AABB detectionBox = creeper.getBoundingBox().inflate(AMULET_DETECTION_RANGE);
        List<? extends Player> players = level.getEntitiesOfClass(Player.class, detectionBox,
                player -> player != null && !player.isSpectator());
        for (Player player : players) {
            // 检查玩家是否佩戴了苦力怕怕护符
            // 假设 ModItems.CREEPER_FEAR_AMULET 是你的护符物品注册表对象
            boolean hasAmulet = ModCurios.hasCurio(player,ModCurios.GREAT_SHIFT.get());
            if (hasAmulet) {
                return true; // 找到一个佩戴护符的玩家，立即返回
            }
        }
        return false; // 没有找到佩戴护符的玩家

    }

    private void handleCreeperFear(Creeper creeper,ServerLevel level){
        CreeperAccessor accessor = (CreeperAccessor) creeper;
        accessor.setOldSwell(accessor.getSwell());
        accessor.setSwell(0);
        creeper.setSwellDir(-1);
        creeper.setTarget(null);
        this.questionMarkDisappearTime = level.getGameTime() + QUESTION_MARK_DURATION_TICKS;
    }
    private void handleDeathAvoidanceEffect(Creeper creeper,ServerLevel level){
        RandomSource random = level.getRandom();
        double offsetX = (random.nextDouble() - 0.5) * 64; // -32 到 +32
        double offsetY = (random.nextDouble() - 0.5) * 32; // -16 到 +16
        double offsetZ = (random.nextDouble() - 0.5) * 64; // -32 到 +32
        double newX = creeper.getX() + offsetX;
        double newY = Mth.clamp(creeper.getY() + offsetY, level.getMinBuildHeight(), level.getMaxBuildHeight());
        double newZ = creeper.getZ() + offsetZ;
        Explosion.BlockInteraction explosionMode = Explosion.BlockInteraction.DESTROY;
        Explosion explosion = new Explosion(
                level,
                creeper,
                newX,
                newY,
                newZ,
                3.0f, // 爆炸强度
                false, // 是否产生火灾
                explosionMode // 方块破坏模式
        );
        explosion.explode();
        explosion.finalizeExplosion(true);
        creeper.discard();
    }
}
