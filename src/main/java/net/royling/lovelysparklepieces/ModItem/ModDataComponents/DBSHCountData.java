package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record DBSHCountData(
        int modeIndex
) {
    public static final Codec<DBSHCountData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("mode_index").forGetter(DBSHCountData::modeIndex)
    ).apply(instance, DBSHCountData::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DBSHCountData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, DBSHCountData::modeIndex,
            DBSHCountData::new
    );
    public static final int DEFAULT_MODE_INDEX = 0;
}
