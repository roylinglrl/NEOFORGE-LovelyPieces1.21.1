package net.royling.lovelysparklepieces.ModEvents.bracelet;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class MoonlightBraceletEvent {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        if (event.getEntity().level().isClientSide)return;
        if (!(event.getEntity() instanceof Monster)) return;
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if(!ModCurios.hasCurio(player,ModCurios.MOONLIGHT_BRACELET.get()))return;
        if(player.getRandom().nextFloat()<0.15f){
            Level level = event.getEntity().level();
            ItemStack stack = new ItemStack(ModItems.SPARKLE_SHARD.get(), 1);
            event.getEntity().spawnAtLocation(stack);
        }
    }
}
