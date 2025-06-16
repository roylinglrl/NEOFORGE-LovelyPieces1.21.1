package net.royling.lovelysparklepieces.ModEnchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEnchantment.weapon.LightStrikerEnchantmentEffect;
import net.royling.lovelysparklepieces.ModEnchantment.weapon.MagicBladeEnchantmentEffect;

import java.util.function.Supplier;

public class ModEnchantEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENTS=
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, LovelySparklePieces.MODID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> LIGHT_STRIKER =
            ENTITY_ENCHANTMENTS.register("lightning_striker",
                    ()-> LightStrikerEnchantmentEffect.CODEC);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> MAGIC_BLADE =
            ENTITY_ENCHANTMENTS.register("magic_blade",
                    ()-> MagicBladeEnchantmentEffect.CODEC);
}
