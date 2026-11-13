package net.royling.lovelysparklepieces.ModEnchantment.weapon;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEnchantment.ModEnchantments;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class MagicBladeEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void exThunderDamage(LivingIncomingDamageEvent event){
        if(event.getEntity().level().isClientSide)return;
        DamageSource incomingSource = event.getSource();
        // Only convert direct melee hits, never this enchantment's own magic damage.
        if (!incomingSource.is(DamageTypes.PLAYER_ATTACK)
                && !incomingSource.is(DamageTypes.MOB_ATTACK)
                && !incomingSource.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) return;
        if(incomingSource.getEntity() instanceof LivingEntity livingEntity
                && incomingSource.getDirectEntity() == livingEntity){
            if(livingEntity instanceof Player player) {
                if (!incomingSource.is(DamageTypes.PLAYER_ATTACK)) return;
            }
            RegistryAccess registryAccess = event.getEntity().registryAccess();
            Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
            Holder<Enchantment> magic = enchantmentRegistry.getHolderOrThrow(ModEnchantments.MAGIC_BLADE);
            if(magic==null)return;
            int count = EnchantmentHelper.getEnchantmentLevel(magic,livingEntity);
            if(count<1)return;
            DamageSource source = livingEntity.damageSources().indirectMagic(livingEntity,livingEntity);
            event.setCanceled(true);
            event.getEntity().hurt(source,event.getAmount());
        }
    }
}
