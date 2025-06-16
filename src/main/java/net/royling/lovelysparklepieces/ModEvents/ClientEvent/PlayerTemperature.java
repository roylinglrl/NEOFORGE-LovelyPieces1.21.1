package net.royling.lovelysparklepieces.ModEvents.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public record PlayerTemperature(int temperatureCount) implements CustomPacketPayload {
    public PlayerTemperature {
        if(temperatureCount < 0) throw new IllegalArgumentException("Invalid chipCount value");
    }
    public static final Type<PlayerTemperature> TYPE =
            new Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_temperature_count")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerTemperature> STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.VAR_INT, PlayerTemperature::temperatureCount,
            PlayerTemperature::new);
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(PlayerTemperature playertemperature, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()-> {
                        Player clientplayer = context.player();
                        if(clientplayer!=null){
                            clientplayer.getPersistentData().putInt("lsp_temperature_count",playertemperature.temperatureCount);
                        }
                    }
            );
        }
    }
}
