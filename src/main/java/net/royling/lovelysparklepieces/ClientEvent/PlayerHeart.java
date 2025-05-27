package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerHeart(int heartCount) implements CustomPacketPayload {
    public PlayerHeart {
        if(heartCount< 0) throw new IllegalArgumentException("Invalid soulCount value");
    }
    public static final Type<PlayerHeart> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_heart_state")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerHeart> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlayerHeart::heartCount,
            PlayerHeart::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayerHeart playerSoul, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()-> {
                        Player clientplayer = context.player();
                        if(clientplayer!=null){
                            clientplayer.getPersistentData().putInt("lsp_heart_state",playerSoul.heartCount);
                        }
                    }
            );
        }
    }
}
