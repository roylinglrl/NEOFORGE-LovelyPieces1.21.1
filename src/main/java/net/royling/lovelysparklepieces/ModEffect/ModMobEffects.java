package net.royling.lovelysparklepieces.ModEffect;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;

import java.util.function.Supplier;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, LovelySparklePieces.MODID);
    public static final Holder<MobEffect> OVERHEAT_EFFECT = MOB_EFFECTS.register("overheat",
            () -> new OverheatEffect(MobEffectCategory.HARMFUL, 0xFF0000));
}
