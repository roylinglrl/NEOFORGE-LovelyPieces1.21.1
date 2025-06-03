package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ShootCDData(
        int shootCooldown
) {
    public static final Codec<ShootCDData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("shoot_cooldown").forGetter(ShootCDData::shootCooldown)
    ).apply(instance, ShootCDData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ShootCDData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ShootCDData::shootCooldown,
            ShootCDData::new
    );
    public static final int DEFAULT_MODE_INDEX = 0;
}
