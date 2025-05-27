package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record BoundaryStoneData(
        BlockPos anchorPos,
        ResourceLocation anchorDimension
) {
    public static final Codec<BoundaryStoneData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("anchor_pos").forGetter(BoundaryStoneData::anchorPos),
            ResourceLocation.CODEC.fieldOf("anchor_dimension").forGetter(BoundaryStoneData::anchorDimension)
    ).apply(instance, BoundaryStoneData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BoundaryStoneData> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BoundaryStoneData::anchorPos,
            ResourceLocation.STREAM_CODEC, BoundaryStoneData::anchorDimension,
            BoundaryStoneData::new
    );

    public BoundaryStoneData() {
        this(BlockPos.ZERO, ResourceLocation.fromNamespaceAndPath("minecraft", "overworld"));
    }
}
