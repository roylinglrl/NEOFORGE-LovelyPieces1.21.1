package net.royling.lovelysparklepieces.ModEvents.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerSoul(int soulCount) implements CustomPacketPayload {
    public PlayerSoul{
        if(soulCount< 0) throw new IllegalArgumentException("Invalid soulCount value");
    }
    public static final CustomPacketPayload.Type<PlayerSoul> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_soul_count")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf,PlayerSoul> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_INT,PlayerSoul::soulCount,
            PlayerSoul::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(PlayerSoul playerSoul, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()-> {
                        Player clientplayer = context.player();
                        if(clientplayer!=null){
                            clientplayer.getPersistentData().putInt("lsp_soul_count",playerSoul.soulCount);
                        }
                    }
            );
        }
    }
}
