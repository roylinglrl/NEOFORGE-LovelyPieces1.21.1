package net.royling.lovelysparklepieces.ModEnchantment.weapon;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import net.royling.lovelysparklepieces.ModSounds.ModSounds;

public record MagicBladeEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<MagicBladeEnchantmentEffect> CODEC =
            MapCodec.unit(MagicBladeEnchantmentEffect::new);
    @Override
    public void apply(ServerLevel serverLevel, int i, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        if(enchantedItemInUse.owner() instanceof Player player){
            if(player.getAttackStrengthScale(0.5f)>0.9f){
                if(entity instanceof LivingEntity target){
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, target.getX(), target.getY() + target.getBbHeight() / 2.0, target.getZ(), 10, 0.5, 0.5, 0.5, 0.0);
                    serverLevel.playSound(null, target.blockPosition(),
                            SoundEvents.GLASS_BREAK,
                            SoundSource.PLAYERS,
                            0.8F + i * 0.1F,
                            0.0F);
                }
            }

        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }


}
