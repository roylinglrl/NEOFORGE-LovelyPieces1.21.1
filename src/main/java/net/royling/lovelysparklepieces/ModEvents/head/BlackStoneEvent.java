package net.royling.lovelysparklepieces.ModEvents.head;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class BlackStoneEvent {
    @SubscribeEvent
    public static void onPlayerHurt(LivingIncomingDamageEvent event) {
        // 仅处理玩家伤害
        if (!(event.getEntity() instanceof Player player)) return;

        // 仅处理来自岩浆块的伤害
        DamageSource source = event.getSource();
        if (!source.is(DamageTypes.HOT_FLOOR)) return;
        if (ModCurios.hasCurio(player,ModCurios.BLACKSTONE_HEART.get())) {
            event.setCanceled(true);
        }
    }
}
