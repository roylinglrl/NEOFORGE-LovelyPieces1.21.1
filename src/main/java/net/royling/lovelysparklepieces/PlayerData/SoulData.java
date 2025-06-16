package net.royling.lovelysparklepieces.PlayerData;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.PlayerSoul;

public class SoulData {
    public static int getSouls(Player player){
        if(player.getPersistentData().contains("lsp_soul_count")){
            return player.getPersistentData().getInt("lsp_soul_count");
        }
        else return  0;
    }
    public static void addSoul(Player player,int count){
        int oricount = getSouls(player);
        player.getPersistentData().putInt("lsp_soul_count",Math.min(200,oricount+count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerSoul(Math.min(200,oricount+count)));
    }
    public static void removeSoul(Player player,int count){
        int oricount = getSouls(player);
        player.getPersistentData().putInt("lsp_soul_count",Math.max(0,oricount-count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerSoul(Math.max(0,oricount-count)));
    }
    public static void setSoul(Player player,int count){
        player.getPersistentData().putInt("lsp_soul_count",Math.max(0, Math.min(200, count)));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerSoul(Math.max(0, Math.min(200, count))));
    }
    @SubscribeEvent
    public static void initPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().contains("lsp_soul_count")) {
            player.getPersistentData().putInt("lsp_soul_count", 0);
        }
    }
}
