package net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set;

import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface  SetBonusStage {
    int requiredCount(); // 达成这个阶段需要的物品数量

    void onActivated(Player player);

    void onTick(Player player);

    void onDeactivated(Player player);

    List<String> tooltip();
}
