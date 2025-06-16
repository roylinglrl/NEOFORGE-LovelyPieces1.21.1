package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class HellFireEvent {
    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event){
        ServerPlayer player = event.getPlayer();
        String message = event.getRawText().trim();
        if (message.equals("1")){
            ItemStack reward = new ItemStack(ModItems.HELL_FIRE.get());
            player.addItem(reward);
        }
    }
}
