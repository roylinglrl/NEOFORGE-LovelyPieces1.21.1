package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.BoneArmorComponent;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ModDataComponents;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Optional;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class PlayerGetDamageEvent {
    @SubscribeEvent
    public static void playerIncomingDamage(LivingIncomingDamageEvent event){
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;
        Optional<ItemStack> boneArmorStack = CuriosApi.getCuriosInventory(player)
                .flatMap(inv -> inv.findFirstCurio(ModCurios.BONE_ARMOR.get()))
                .flatMap(result -> Optional.of(result.stack()));
        if (boneArmorStack.isEmpty()) return;
        ItemStack stack = boneArmorStack.get();
        BoneArmorComponent component = stack.get(ModDataComponents.BONE_ARMOR.get());
        if (component == null) {
            component = new BoneArmorComponent(0, 0);
            stack.set(ModDataComponents.BONE_ARMOR.get(), component);
        }
        int currentCharges = component.chargeTimes();
        if (currentCharges > 0) {
            stack.set(ModDataComponents.BONE_ARMOR.get(),
                    new BoneArmorComponent(currentCharges - 1, component.chargeCount()));
            player.inventoryMenu.broadcastChanges();
            event.setCanceled(true);
        }

    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerGetDamage(LivingDamageEvent.Pre event){
        if(event.getEntity()instanceof Player player){
            int modifier = 1;
            if(ModCurios.hasCurio(player,ModCurios.BLASPHEMOUS_CONTRACT.get())){
                modifier += 0.35;
            }

            event.setNewDamage(event.getNewDamage()*modifier);
            if(ModCurios.hasCurio(player,ModCurios.DISASTER_EMBLEM.get())){
                event.setNewDamage(event.getNewDamage()*2);
            }
            if(ModCurios.hasCurio(player,ModCurios.THE_EVIL_CURSE.get())){
                player.setHealth(0);
                return;
            }
        }
    }
}
