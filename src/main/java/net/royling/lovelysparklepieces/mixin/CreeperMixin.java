package net.royling.lovelysparklepieces.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Creeper.class)
public class CreeperMixin {
    private static final int DETECTION_RANGE = 8;

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
}
