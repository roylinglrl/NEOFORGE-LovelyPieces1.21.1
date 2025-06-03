package net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class GunItem extends Item {
    public GunItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    public abstract boolean isAuto();
    protected abstract double getShootDamage();
    protected abstract SoundEvent getShootSound();
    public abstract void performShoot(Level level, Player player, InteractionHand usedHand, ItemStack gunStack);

}
