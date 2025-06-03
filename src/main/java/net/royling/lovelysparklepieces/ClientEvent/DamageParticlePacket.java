package net.royling.lovelysparklepieces.ClientEvent;

import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.ClientEvent.Particle.DamageNumberParticle;
import net.royling.lovelysparklepieces.ClientEvent.Particle.ModParticles;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import org.joml.Vector3f;

public record DamageParticlePacket(String damageType, double damageCount, double posx,double posy,double posz,double magnification) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamageParticlePacket>TYPE=
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"damage_particle")
            );
    public static final StreamCodec<RegistryFriendlyByteBuf,DamageParticlePacket>STREAM_CODEC=StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,DamageParticlePacket::damageType,
            ByteBufCodecs.DOUBLE,DamageParticlePacket::damageCount,
            ByteBufCodecs.DOUBLE,DamageParticlePacket::posx,
            ByteBufCodecs.DOUBLE,DamageParticlePacket::posy,
            ByteBufCodecs.DOUBLE,DamageParticlePacket::posz,
            ByteBufCodecs.DOUBLE,DamageParticlePacket::magnification,
            DamageParticlePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(DamageParticlePacket damageParticlePacket, IPayloadContext context){
        if(context.flow().isClientbound()){
            context.enqueueWork(()->{
                Player clientplayer = context.player();
                if(clientplayer!=null){
                    Minecraft mc = Minecraft.getInstance();
                    Vec3 position = new Vec3(damageParticlePacket.posx,damageParticlePacket.posy,damageParticlePacket.posz);
                    mc.level.addParticle(ModParticles.DAMAGE_NUMBER_PARTICLE.get(),
                            damageParticlePacket.posx,damageParticlePacket.posy,damageParticlePacket.posz,
                            damageParticlePacket.damageCount,
                            getColorByDamageType(damageParticlePacket.damageType),damageParticlePacket.magnification);
                }
            });
        }
    }
    private static int getColorByDamageType(String damageType) {
        return switch (damageType) {
            case "fire" -> 0xFFA500;
            case "magic" -> 0x00FFFF;
            case "fall" -> 0x808080;
            case "thunder"->0xFFD700;
            case "arrow"->0x8B658B;
            case "explosion"->0x8B3A3A;
            case "frozen"->0x7EC0EE;
            case "silver"->0xF8F8FF;
            case "attack" ->0xAA0000;
            default -> 0xCC3700;
        };
    }
}
