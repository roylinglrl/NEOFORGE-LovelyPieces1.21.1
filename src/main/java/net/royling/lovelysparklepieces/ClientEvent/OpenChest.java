package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Rings.EnderRingItem;

public record OpenChest() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenChest> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"open_chest"));
    public static final StreamCodec<RegistryFriendlyByteBuf,OpenChest> STREAM_CODEC=StreamCodec.unit(new OpenChest());
    @Override
    public Type<OpenChest> type() {
        return TYPE;
    }

    public static void handle(OpenChest openChest,IPayloadContext context){
        context.enqueueWork(()->{
            if (context.player()instanceof ServerPlayer player){
                EnderRingItem.openEnderChest(player);
            }
        });
    }
}
