package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;

@EventBusSubscriber(modid = "lovely_sparkle_pieces", bus = EventBusSubscriber.Bus.MOD)
public class createDefaultAttribute {
    @SubscribeEvent
    public static void createDA(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER, ModAttribute.CRIT_CHANCE,0d);
        event.add(EntityType.PLAYER, ModAttribute.DAMAGE_MODIFIER,1d);
    }
}
