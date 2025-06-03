package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary.EmberCoreItem;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.PlayerData.SoulData;

public class EmberCoreEvent {

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Post event){
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        if(SoulData.getSouls(player)<1)return;
        if(ModCurios.hasCurio(player,ModCurios.EMBER_CORE.get())){
            if(player.getRandom().nextDouble()<0.25){
                SoulData.removeSoul(player,1);
                EmberCoreItem.tryTriggerEffect(player);
            }
        }
    }
}
