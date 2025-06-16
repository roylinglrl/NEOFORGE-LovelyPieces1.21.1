package net.royling.lovelysparklepieces.ModEvents.Rings;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class PuregoldRing {
    /**
     * 处理猪灵和猪灵蛮兵的目标设定。
     * 如果玩家佩戴了相应的戒指，则取消目标设定。
     */
    @SubscribeEvent
    public static void onPiglinSetTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().level().isClientSide) return;

        // 检查新的目标是否是玩家
        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            LivingEntity attacker = event.getEntity();

            // 检查攻击者是否是猪灵或猪灵蛮兵
            if (attacker instanceof Piglin || attacker instanceof PiglinBrute) {
                // 如果是普通猪灵，并且玩家佩戴了纯金指环，取消目标设定
                if (attacker instanceof Piglin && ModCurios.hasCurio(player, ModCurios.PUREGOLD_RING.get())) {
                    event.setCanceled(true);
                    return; // 已处理，直接返回
                }
                // 如果是猪灵或猪灵蛮兵，并且玩家佩戴了堡垒戒指，取消目标设定
                if ((attacker instanceof Piglin || attacker instanceof PiglinBrute) && ModCurios.hasCurio(player, ModCurios.BASTION_RING.get())) {
                    event.setCanceled(true);
                    // 不需要返回，因为猪灵目标设定可能同时被两个戒指影响（虽然功能上重复）
                    // 但这里堡垒戒指的优先级更高，它会覆盖纯金戒指的效果。
                }
            }
        }
    }

    /**
     * 处理猪灵和猪灵蛮兵对玩家造成的伤害。
     * 如果玩家佩戴了相应的戒指，则取消伤害。
     */
    @SubscribeEvent
    public static void onPlayerGetAttacked(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;

        // 检查受攻击者是否是玩家
        if (event.getEntity() instanceof Player player) {
            // 检查伤害来源是否是猪灵或猪灵蛮兵
            if (event.getSource().getEntity() instanceof Piglin || event.getSource().getEntity() instanceof PiglinBrute) {
                LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

                // 如果是普通猪灵，并且玩家佩戴了纯金指环，取消伤害
                if (attacker instanceof Piglin && ModCurios.hasCurio(player, ModCurios.PUREGOLD_RING.get())) {
                    event.setCanceled(true);
                    return; // 已处理，直接返回
                }
                // 如果是猪灵或猪灵蛮兵，并且玩家佩戴了堡垒戒指，取消伤害
                if ((attacker instanceof Piglin || attacker instanceof PiglinBrute) && ModCurios.hasCurio(player, ModCurios.BASTION_RING.get())) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
