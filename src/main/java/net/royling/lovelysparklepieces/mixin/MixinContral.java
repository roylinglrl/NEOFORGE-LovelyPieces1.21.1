package net.royling.lovelysparklepieces.mixin;

import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinContral implements IMixinConnector {
    @Override
    public void connect() {
        if(ModList.get().isLoaded("modid")){
            Mixins.addConfiguration("new.mixin.json");
        }
    }
}
