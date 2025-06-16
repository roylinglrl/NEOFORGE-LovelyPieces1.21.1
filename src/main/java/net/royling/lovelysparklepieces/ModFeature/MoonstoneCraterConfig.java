package net.royling.lovelysparklepieces.ModFeature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record MoonstoneCraterConfig(int radius, int depth, int minStones, int maxStones)
        implements FeatureConfiguration {

    public static final Codec<MoonstoneCraterConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("radius").forGetter(MoonstoneCraterConfig::radius),
                    Codec.INT.fieldOf("depth").forGetter(MoonstoneCraterConfig::depth), // 新增深度字段
                    Codec.INT.fieldOf("minStones").forGetter(MoonstoneCraterConfig::minStones),
                    Codec.INT.fieldOf("maxStones").forGetter(MoonstoneCraterConfig::maxStones)
            ).apply(instance, MoonstoneCraterConfig::new)
    );
}
