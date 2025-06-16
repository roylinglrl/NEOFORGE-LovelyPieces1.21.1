package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import net.royling.lovelysparklepieces.PlayerData.SoulData;
import top.theillusivec4.curios.api.CuriosApi;

public class BCEvents {
    public static boolean hasBlasphemousContract(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .flatMap(inventory -> inventory.findFirstCurio(ModCurios.BLASPHEMOUS_CONTRACT.get()))
                .isPresent();
    }
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getSource().getEntity() instanceof Player player && hasBlasphemousContract(player)) {
            for (ItemEntity drop : event.getDrops()) {
                drop.getItem().setCount(drop.getItem().getCount() * 2);
            }
        }
    }
    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        if (!event.getEntity().level().isClientSide) {
            Player player = event.getAttackingPlayer();
            if (player != null && hasBlasphemousContract(player)) {
                event.setDroppedExperience(event.getDroppedExperience() * 2);
            }
        }
    }
    @SubscribeEvent
    public static void onPostDamage(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof Player player && hasBlasphemousContract(player)) {
            if (player.level().random.nextFloat() < 0.25f) {
                player.heal(4);
            }
        }
    }
    //亵渎契约减少回复
    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player && hasBlasphemousContract(player)) {
            float count = (float) (ModCurios.hasCurio(player,ModCurios.ERODED_FACE.get()) ? 0.5:0.25);
            event.setAmount(event.getAmount() * count);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(Component.translatable("msg.lsp.heal_deal").withStyle(ChatFormatting.DARK_RED), true);
            }
        }
    }
    @SubscribeEvent
    public static void onTrySleep(CanPlayerSleepEvent event) {
        Player player = event.getEntity();
        if (ModCurios.hasCurio(player,ModCurios.DREAM_HEART.get())) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(Component.translatable("msg.lsp.dream_heart_sleep_allowed").withStyle(ChatFormatting.GREEN), true);
            }
            return;
        }
        if(hasBlasphemousContract(player)) {
                event.setProblem(Player.BedSleepingProblem.NOT_POSSIBLE_HERE);
        }
            if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(Component.translatable("msg.lsp.cant_sleep").withStyle(ChatFormatting.DARK_RED), true);
        }
    }
    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player && hasBlasphemousContract(player)) {
            SoulData.addSoul(player,1);
            if(player.getMainHandItem().is(ModItems.DEATH_HOOK.get())){
                SoulData.addSoul(player,1);
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return; // 只在服务器端处理

        // 检查玩家是否佩戴了“美梦之心”
        if (ModCurios.hasCurio(player,ModCurios.DREAM_HEART.get())) {
            // 获得十分钟的力量效果 (Power I)
            // 20 ticks/秒 * 60 秒/分钟 * 10 分钟 = 12000 ticks
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 12000, 0));

            // 获得十分钟的速度效果 (Speed I)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 0));

            // 可以添加一个提示信息给玩家
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.displayClientMessage(Component.translatable("msg.lsp.dream_heart_awakening").withColor(ColorUtil.getRainbow(2f)), true);
            }
        }
    }
}
