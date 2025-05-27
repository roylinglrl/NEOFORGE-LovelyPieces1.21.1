package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerLavadef(int lavadefCount) implements CustomPacketPayload {
    public PlayerLavadef {
        if(lavadefCount< 0) throw new IllegalArgumentException("Invalid soulCount value");
    }
    public static final Type<PlayerLavadef> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_lava_def")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerLavadef> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlayerLavadef::lavadefCount,
            PlayerLavadef::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(PlayerLavadef playerSoul, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()-> {
                        Player clientplayer = context.player();
                        clientplayer.getPersistentData().putInt("lsp_lava_def", playerSoul.lavadefCount);
                    }
            );
        }
    }
}
