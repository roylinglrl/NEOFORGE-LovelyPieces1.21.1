package net.royling.lovelysparklepieces.mixin;


import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary.SoulQuiverItem;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.PlayerData.SoulData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {
    @Inject(        method = "getHeldProjectile(Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Predicate;)Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true)
    private static void injectAmmo(LivingEntity shooter, Predicate<ItemStack> isAmmo, CallbackInfoReturnable<ItemStack> cir){
        if(shooter instanceof Player player && SoulQuiverItem.hasSoulQuiver(player)&&SoulData.getSouls(player)>0){
            cir.setReturnValue(new ItemStack(Items.ARROW));
            cir.cancel();
        }
    }
    @Redirect(
            method = "useAmmo",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;processAmmoUse(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;I)I")
    )
    private static int redirectAmmo(ServerLevel level, ItemStack weapon, ItemStack ammo, int count, @Coerce ItemStack actualWeapon,@Coerce ItemStack actualAmmo,LivingEntity shooter,boolean intangible){
        int modifiedCount = count;
        if(shooter instanceof Player player&&
        !player.level().isClientSide ){
            if(ModCurios.hasCurio(player,ModCurios.LEATHER_QUIVER.get())){
                if(player.getRandom().nextFloat()<0.25f){
                    modifiedCount = 0;
                }
            }
            if(ModCurios.hasCurio(player,ModCurios.WOOD_GRAIN_QUIVER.get())){
                if(player.getRandom().nextFloat()<0.4){
                    modifiedCount = 0;
                }
            }
            if(ModCurios.hasCurio(player,ModCurios.SOUL_QUIVER.get())){
                if(player.getRandom().nextFloat()<0.5){
                    modifiedCount = 0;
                }
            }
        }
        return EnchantmentHelper.processAmmoUse(level,weapon,ammo,modifiedCount);
    }
}
