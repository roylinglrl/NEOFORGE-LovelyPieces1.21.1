package net.royling.lovelysparklepieces.ModEvents.head;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class GildedPipeEvent {

    private static final ThreadLocal<Boolean> IS_APPLYING_MODIFIED_EFFECT = ThreadLocal.withInitial(() -> false);
    @SubscribeEvent
    public static void onMobEffect(MobEffectEvent.Added event) {
        if (IS_APPLYING_MODIFIED_EFFECT.get()) {
            return;
        }
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }
        if (ModCurios.hasCurio(player, ModCurios.GILDED_PIPE.get())) {
            MobEffectInstance newEffect = event.getEffectInstance();
            if (newEffect.getDuration() > 1) {
                System.out.println("Test Add Effect");
                int originalDuration = newEffect.getDuration();
                int newDuration = (int) (originalDuration * 1.20);
                IS_APPLYING_MODIFIED_EFFECT.set(true);
                try {
                    System.out.println("Test add new Effect");
                    player.removeEffect(newEffect.getEffect());
                    System.out.println(originalDuration+"/"+newDuration);
                    player.addEffect(new MobEffectInstance(
                            newEffect.getEffect(),
                            newDuration,
                            newEffect.getAmplifier(),
                            newEffect.isAmbient(),
                            newEffect.isVisible(),
                            newEffect.showIcon()
                    ));
                } finally {
                    IS_APPLYING_MODIFIED_EFFECT.set(false);
                }
            }
        }
    }
}
