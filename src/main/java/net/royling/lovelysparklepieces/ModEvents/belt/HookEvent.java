package net.royling.lovelysparklepieces.ModEvents.belt;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.GAME)
public class HookEvent {
    @SubscribeEvent
    public static void onFishingCatch(ItemFishedEvent event){
        Player player = event.getEntity();
        Level level= player.level();
        if(level.isClientSide)return;
        if(ModCurios.hasCurio(player,ModCurios.GOLDEN_HOOK.get())||ModCurios.hasCurio(player,ModCurios.FISHERMAN_TOOLBOX.get())) {
            if (player.getRandom().nextDouble() < 0.05 + player.getAttributeValue(Attributes.LUCK) * 0.01) {
                FishingHook fishingHook = event.getHookEntity();
                ItemStack treasure = new ItemStack(ModItems.FISHING_TREASURE.get());
                ItemEntity itemEntity = new ItemEntity(level, fishingHook.getX(), fishingHook.getY(), fishingHook.getZ(), treasure);
                double deltaX = player.getX() - fishingHook.getX();
                double deltaY = player.getY() - fishingHook.getY();
                double deltaZ = player.getZ() - fishingHook.getZ();
                double distance = Mth.sqrt((float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
                double velocityMultiplier = 0.8;
                double upwardBias = 0.25;
                itemEntity.setDeltaMovement(
                        deltaX / distance * velocityMultiplier,
                        deltaY / distance * velocityMultiplier + upwardBias,
                        deltaZ / distance * velocityMultiplier
                );
                level.addFreshEntity(itemEntity);
            }
        }
        if(ModCurios.hasCurio(player,ModCurios.DOUBLE_HOOK.get())||ModCurios.hasCurio(player,ModCurios.FISHERMAN_TOOLBOX.get())){
            if(player.getRandom().nextDouble()<0.25){
                if(!event.getDrops().isEmpty()) {
                    ItemEntity itemEntity = getItemEntity(event, level, player);
                    level.addFreshEntity(itemEntity);
                }

            }
        }
    }

    private static @NotNull ItemEntity getItemEntity(ItemFishedEvent event, Level level, Player player) {
        ItemStack ori = event.getDrops().getFirst();
        FishingHook fishingHook = event.getHookEntity();
        ItemEntity itemEntity = new ItemEntity(level, fishingHook.getX(), fishingHook.getY(), fishingHook.getZ(), ori);
        double deltaX = player.getX() - fishingHook.getX();
        double deltaY = player.getY() - fishingHook.getY();
        double deltaZ = player.getZ() - fishingHook.getZ();
        double distance = Mth.sqrt((float) (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ));
        double velocityMultiplier = 0.8;
        double upwardBias = 0.25;
        itemEntity.setDeltaMovement(
                deltaX / distance * velocityMultiplier,
                deltaY / distance * velocityMultiplier + upwardBias,
                deltaZ / distance * velocityMultiplier
        );
        return itemEntity;
    }
}
