package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerChip(int chipCount) implements CustomPacketPayload {
    public PlayerChip {
        if(chipCount < 0) throw new IllegalArgumentException("Invalid chipCount value");
    }
    public static final Type<PlayerChip> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"splsp_chip_count")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerChip> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlayerChip::chipCount,
            PlayerChip::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(PlayerChip playerChip, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()-> {
                        Player clientplayer = context.player();
                        if(clientplayer!=null){
                            clientplayer.getPersistentData().putInt("lsp_chip_count",playerChip.chipCount);
                        }
                    }
            );
        }
    }
}
