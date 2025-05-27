package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record DoubleJump() implements CustomPacketPayload {
    public static final Type<DoubleJump> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_double_jump"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DoubleJump> STREAM_CODEC=StreamCodec.unit(new DoubleJump());
    @Override
    public Type<DoubleJump> type() {
        return TYPE;
    }
    public static void handle(DoubleJump doubleJump, IPayloadContext context){
        context.enqueueWork(()->{
            if (context.player()instanceof ServerPlayer player){
                if(player.onGround())return;
                player.getPersistentData().putBoolean("lsp_double_jump",true);
            }
        });
    }

}
