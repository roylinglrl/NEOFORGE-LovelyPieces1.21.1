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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEnchantment.ModEnchantments;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class MagicBladeEvent {
    @SubscribeEvent
    public static void exThunderDamage(LivingIncomingDamageEvent event){
        if(event.getEntity().level().isClientSide)return;
        if(event.getSource().getEntity() instanceof LivingEntity livingEntity){
            if(livingEntity instanceof Player player) {
                if (!event.getSource().is(DamageTypes.PLAYER_ATTACK)) return;
            }
            RegistryAccess registryAccess = event.getEntity().registryAccess();
            Registry<Enchantment> enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
            Holder<Enchantment> magic = enchantmentRegistry.getHolderOrThrow(ModEnchantments.MAGIC_BLADE);
            if(magic==null)return;
            int count = EnchantmentHelper.getEnchantmentLevel(magic,livingEntity);
            if(count<1)return;
            DamageSource source = livingEntity.damageSources().indirectMagic(livingEntity,livingEntity);
            event.getEntity().hurt(source,event.getAmount());
            event.setCanceled(true);
        }
    }
}
