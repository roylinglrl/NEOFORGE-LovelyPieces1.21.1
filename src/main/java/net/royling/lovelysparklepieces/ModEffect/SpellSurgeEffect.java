package net.royling.lovelysparklepieces.ModEffect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public class SpellSurgeEffect extends MobEffect {
    protected SpellSurgeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    @Override
    public boolean isInstantenous() {
        return false;
    }
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return true;
    }
}
