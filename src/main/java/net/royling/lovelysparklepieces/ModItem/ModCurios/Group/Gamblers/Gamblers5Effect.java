package net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Gamblers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.SetBonusStage;
import net.royling.lovelysparklepieces.PlayerData.ChipsData;

import java.util.Arrays;
import java.util.List;

public class Gamblers5Effect implements SetBonusStage {
    public int requiredCount() { return 5; }

    public void onActivated(Player player) {
        player.sendSystemMessage(Component.translatable("set.lsp.gamblers.5effect"));
        player.getPersistentData().putBoolean("gambler_5effect",true);
    }

    public void onTick(Player player) {
        if(player.tickCount % 100 != 0)return;
        if(ChipsData.getChips(player)>=600){
            ChipsData.removeChip(player,100);
            player.addItem(new ItemStack(Items.DIAMOND,1));
        }
    }

    public void onDeactivated(Player player) {
        player.sendSystemMessage(Component.translatable("set.lsp.gamblers.5effect.remove"));
        player.getPersistentData().putBoolean("gambler_5effect",false);
    }
    public List<String> tooltip(){
        return Arrays.asList(
                "lsp.effect.gamblers.5",
                "lsp.effect.gamblers.5.2"
        );
    }
}
