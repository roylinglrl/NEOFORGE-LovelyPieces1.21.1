package net.royling.lovelysparklepieces.ModItem.ModCurios;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public abstract  class UniversalCurio extends Item implements ICurioItem{
    public UniversalCurio(Properties properties) {
        super(properties);
    }

}
