package net.royling.lovelysparklepieces.PlayerData;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ClientEvent.PlayerChip;
import net.royling.lovelysparklepieces.ClientEvent.PlayerSoul;

public class ChipsData {
    public static int getChips(Player player){
        if(player.getPersistentData().contains("lsp_chip_count")){
            return player.getPersistentData().getInt("lsp_chip_count");
        }
        else return  0;
    }
    public static void addChip(Player player,int count){
        int oricount = getChips(player);
        player.getPersistentData().putInt("lsp_chip_count",Math.min(1000,oricount+count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerChip(Math.min(1000,oricount+count)));
    }
    public static void removeChip(Player player,int count){
        int oricount = getChips(player);
        player.getPersistentData().putInt("lsp_chip_count",Math.max(0,oricount-count));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerChip(Math.max(0,oricount-count)));
    }
    public static void setChip(Player player,int count){
        player.getPersistentData().putInt("lsp_chip_count",Math.max(0, Math.min(1000, count)));
        PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerChip(Math.max(0, Math.min(1000, count))));
    }
    @SubscribeEvent
    public static void initPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.getPersistentData().contains("lsp_chip_count")) {
            player.getPersistentData().putInt("lsp_chip_count", 0);
        }
    }
}
