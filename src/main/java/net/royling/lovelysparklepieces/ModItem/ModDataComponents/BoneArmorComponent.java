package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BoneArmorComponent(
        int chargeTimes,int chargeCount
) {
    public static final Codec<BoneArmorComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("chargeTimes").forGetter(BoneArmorComponent::chargeTimes),
            Codec.INT.fieldOf("chargeCount").forGetter(BoneArmorComponent::chargeCount)
    ).apply(instance, BoneArmorComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, BoneArmorComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BoneArmorComponent::chargeTimes,
            ByteBufCodecs.VAR_INT, BoneArmorComponent::chargeCount,
            BoneArmorComponent::new
    );
}
