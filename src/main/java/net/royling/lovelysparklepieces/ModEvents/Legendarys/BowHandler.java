package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary.SoulQuiverItem;
import net.royling.lovelysparklepieces.PlayerData.SoulData;

public class BowHandler {
    /*@SubscribeEvent
    public static void onArrowLoose(EntityJoinLevelEvent event){
        if(!((event.getEntity())instanceof Arrow arrow))return;
        if(!(arrow.getOwner()instanceof Player player))return;
        if(player.level().isClientSide)return;
        if(SoulData.getSouls(player)>0 && SoulQuiverItem.hasSoulQuiver(player)){
            if(!player.isCreative()){
                SoulData.removeSoul(player,1);
            }
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            arrow.setRemainingFireTicks(20);
        }
    }*/
    @SubscribeEvent
    public static void onArrowLoose2(ArrowLooseEvent event){
        Player player = event.getEntity();
        if(player.level().isClientSide)return;
        ItemStack bow =event.getBow();
        if(SoulData.getSouls(player)>0&&SoulQuiverItem.hasSoulQuiver(player)){
            event.setCanceled(true);
            int charge = event.getCharge();
            float velocity = BowItem.getPowerForTime(charge);
            if (velocity < 0.1F) return;
            Arrow arrow = new Arrow(EntityType.ARROW,player.level());
            arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);
            arrow.setCritArrow(velocity == 1.0F);
            arrow.setRemainingFireTicks(100);
            Holder<Enchantment> power = player.getServer().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.POWER);
            Holder<Enchantment> punch = player.getServer().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.PUNCH);
            arrow.setTicksFrozen(40);
            arrow.getPersistentData().putBoolean("isSoulArrow",true);
            arrow.setOwner(player);
            arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
            if (!player.isCreative()) {
                SoulData.removeSoul(player, 1);
            }
            player.level().addFreshEntity(arrow);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}
