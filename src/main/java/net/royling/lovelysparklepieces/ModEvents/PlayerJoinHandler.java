package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ClientEvent.PlayerChip;
import net.royling.lovelysparklepieces.ClientEvent.PlayerLavadef;
import net.royling.lovelysparklepieces.ClientEvent.PlayerSoul;
import net.royling.lovelysparklepieces.ClientEvent.PlayerTemperature;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModConfigs.LSPConfig;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.PlayerData.ChipsData;
import net.royling.lovelysparklepieces.PlayerData.SoulData;
import net.royling.lovelysparklepieces.PlayerData.TemperatureData;

public class PlayerJoinHandler {
    private static final ResourceLocation START_ITEM = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"start_item");
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            player.sendSystemMessage(Component.translatable("message.lsp.welcome").withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.translatable("message.lsp.welcome1").withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.translatable("message.lsp.welcome2").withStyle(ChatFormatting.GOLD));
            player.sendSystemMessage(Component.translatable("message.lsp.welcome3").withStyle(ChatFormatting.GOLD));
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerSoul(SoulData.getSouls(player)));
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerChip(ChipsData.getChips(player)));
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerTemperature(TemperatureData.gettemperatures(player)));
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));


            if(LSPConfig.IS_START_ITEM.get()){
                if(!player.getPersistentData().getBoolean("lsp_startitem")){
                    player.addItem(ModCurios.BLASPHEMOUS_CONTRACT.get().getDefaultInstance());
                    player.getPersistentData().putBoolean("lsp_startitem",true);
                }
            }
        }
    }
}
