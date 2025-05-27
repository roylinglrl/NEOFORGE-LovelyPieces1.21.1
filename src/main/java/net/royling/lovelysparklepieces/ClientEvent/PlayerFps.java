package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerFps(long fps,double lsp_memory) implements CustomPacketPayload {
    public PlayerFps {
        if (fps < 0) throw new IllegalArgumentException("Invalid FPS value");
    }
    public static final CustomPacketPayload.Type<PlayerFps> TYPE=
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"fps")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf,PlayerFps> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,PlayerFps::fps,
            ByteBufCodecs.DOUBLE,PlayerFps::lsp_memory,
            PlayerFps::new);

    @Override
    public Type<PlayerFps> type() {
        return TYPE;
    }
    public static void handle(PlayerFps playerFps, IPayloadContext context){
        context.enqueueWork(()->{
            if(context.player()instanceof ServerPlayer player){
                player.getPersistentData().putLong("lsp_fpsvalue",playerFps.fps);
                player.getPersistentData().putDouble("lsp_memory",playerFps.lsp_memory);
            }
        });
    }
}
