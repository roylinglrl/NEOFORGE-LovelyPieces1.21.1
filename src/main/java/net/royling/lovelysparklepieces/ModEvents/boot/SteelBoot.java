package net.royling.lovelysparklepieces.ModEvents.boot;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.boot.GoatBootItem;

public class SteelBoot {
    @SubscribeEvent
    public static void onLivingHurt(LivingIncomingDamageEvent event){
        if(event.getEntity()instanceof Player player){
        }
        if(event.getEntity()instanceof Player player&&event.getSource().is(DamageTypes.FALL)&&
                (GoatBootItem.hasBoot(player)|| ModCurios.hasCurio(player,ModCurios.SKY_BEAST_SHOES.get()) ||ModCurios.hasCurio(player,ModCurios.JUMPING_FOOTWEAR.get()))){
            event.setCanceled(true);
        }
    }
}
