package net.royling.lovelysparklepieces.ModSounds;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;

import java.util.function.Supplier;

import static net.royling.lovelysparklepieces.LovelySparklePieces.MODID;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final Supplier<SoundEvent> TREASURE_OPEN = registerSound("item.fishing_treasure.open");

    private static Supplier<SoundEvent> registerSound(String name) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(location));
    }
}

