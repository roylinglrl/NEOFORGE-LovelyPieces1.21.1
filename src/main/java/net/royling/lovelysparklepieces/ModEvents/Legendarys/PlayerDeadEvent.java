package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary.SoulMarkItem;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.PlayerData.SoulData;

public class PlayerDeadEvent {

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent.Pre event){
        if (!(event.getEntity() instanceof Player player)) return;
        if(player.level().isClientSide)return;
        if(!ModCurios.hasCurio(player,ModCurios.SOUL_MARK.get()))return;
        if(player.getCooldowns().isOnCooldown(ModCurios.SOUL_MARK.get()))return;
        if(player.getHealth()>event.getNewDamage())return;
        if(SoulData.getSouls(player)<10)return;
        SoulData.removeSoul(player,10);
        event.setNewDamage(0);
        SoulMarkItem.applyProtectionEffect(player);
    }
}
