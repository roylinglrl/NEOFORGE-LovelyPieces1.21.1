package net.royling.lovelysparklepieces.PlayerData;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.PlayerTemperature;

public class TemperatureData {
    public static int gettemperatures(Player player){
        if(player.getPersistentData().contains("lsp_temperature_count")){
            return player.getPersistentData().getInt("lsp_temperature_count");
        }
        else return  0;
    }
    public static void addTemperature(Player player,int count){
        int ori_count = gettemperatures(player);
        player.getPersistentData().putInt("lsp_temperature_count",Math.min(150,ori_count+count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerTemperature(Math.min(150,ori_count+count)));
    }
    public static void removeTemperature(Player player,int count){
        int ori_count = gettemperatures(player);
        player.getPersistentData().putInt("lsp_temperature_count",Math.max(0,ori_count-count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerTemperature(Math.max(0,ori_count-count)));
    }
    public static void setTemperature(Player player,int count){
        player.getPersistentData().putInt("lsp_temperature_count",Math.max(0, Math.min(150, count)));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerTemperature(Math.max(0, Math.min(150, count))));
    }
    @SubscribeEvent
    public static void initPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().contains("lsp_temperature_count")) {
            player.getPersistentData().putInt("lsp_temperature_count", 0);
        }
    }
}
