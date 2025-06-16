package net.royling.lovelysparklepieces.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperAccessor {
    // Getter for the 'swell' field
    @Accessor("swell")
    int getSwell();

    // Setter for the 'swell' field
    @Accessor("swell")
    void setSwell(int swell);

    // Getter for the 'maxSwell' field
    @Accessor("maxSwell")
    int getMaxSwell();

    // Setter for the 'maxSwell' field (if you ever need to change it, though typically not for this use case)
    @Accessor("maxSwell")
    void setMaxSwell(int maxSwell);

    @Accessor("oldSwell")
    int getOldSwell();

    @Accessor("oldSwell")
    void setOldSwell(int oldSwell);

}
