package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModConfigs.LSPConfig;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.*;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class NewbieUmbrellaEvent {
    private static final Map<UUID, ItemListHolder> ITEM_CACHE = new HashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // A canceled earlier death must never restore its snapshot after a later death.
        ITEM_CACHE.remove(player.getUUID());
        if (event.isCanceled()) return;

        CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.findFirstCurio(ModCurios.NEWBIE_UMBRELLA.get()))
                .ifPresent(curio -> ITEM_CACHE.put(player.getUUID(), new ItemListHolder(player, curio.stack())));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCurioDropRules(DropRulesEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && ITEM_CACHE.containsKey(player.getUUID())) {
            // Curios owns the clone of equipment, cosmetic slots and slot modifiers.
            event.addOverride(stack -> true, ICurio.DropRule.ALWAYS_KEEP);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemListHolder cache = ITEM_CACHE.get(player.getUUID());
        if (cache == null) return;

        // Commit only once actual drops begin, after Curios has applied the keep rule.
        if (!cache.protectionApplied) {
            cache.protectionApplied = true;
            ItemStack umbrella = cache.umbrella;
            umbrella.setDamageValue(umbrella.getDamageValue() + 1);
            if (umbrella.getDamageValue() >= umbrella.getMaxDamage()) {
                umbrella.shrink(1);
            }
            sendInsultMessage(player);
        }
        event.getDrops().clear();
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        ItemListHolder cache = ITEM_CACHE.remove(player.getUUID());
        if (cache == null || !cache.protectionApplied || event.isEndConquered()) return;

        restoreInventory(player.getInventory().items, cache.mainInventory);
        restoreInventory(player.getInventory().armor, cache.armorInventory);
        restoreInventory(player.getInventory().offhand, cache.offhandInventory);
    }

    private static void restoreInventory(List<ItemStack> target, List<ItemStack> snapshot) {
        for (int i = 0; i < snapshot.size(); i++) {
            target.set(i, snapshot.get(i).copy());
        }
    }

    private static class ItemListHolder {
        final List<ItemStack> mainInventory;
        final List<ItemStack> armorInventory;
        final List<ItemStack> offhandInventory;
        final ItemStack umbrella;
        boolean protectionApplied;

        ItemListHolder(ServerPlayer player, ItemStack umbrella) {
            this.mainInventory = player.getInventory().items.stream().map(ItemStack::copy).toList();
            this.armorInventory = player.getInventory().armor.stream().map(ItemStack::copy).toList();
            this.offhandInventory = player.getInventory().offhand.stream().map(ItemStack::copy).toList();
            this.umbrella = umbrella;
        }
    }

    private static void sendInsultMessage(ServerPlayer player) {
        List<? extends String> messages = LSPConfig.INSULT_MESSAGES.get();
        Component message = messages.isEmpty() ?
                createDefaultMessage() :
                createRandomMessage(messages, player.level().getRandom());
        player.sendSystemMessage(message, false);
    }
    private static Component createDefaultMessage() {
        return Component.translatable("lsp.newbie.newbie").withStyle(ChatFormatting.AQUA);
    }
    private static Component createRandomMessage(List<? extends String> messages, RandomSource random) {
        String rawMessage = messages.get(random.nextInt(messages.size()));
        return rawMessage.startsWith("text:") ?
                Component.literal(rawMessage.substring(5)).withStyle(ChatFormatting.AQUA) :
                Component.translatable(rawMessage).withStyle(ChatFormatting.AQUA);
    }
}
