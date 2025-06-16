package net.royling.lovelysparklepieces.ModEvents.ClientEvent.Particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, LovelySparklePieces.MODID);
    public static final Supplier<SimpleParticleType> DAMAGE_NUMBER_PARTICLE =
            PARTICLE_TYPES.register("damage_number",
                    () -> new SimpleParticleType(true)
            );
    public static void register(IEventBus modEventBus) {
        PARTICLE_TYPES.register(modEventBus);
    }
}
