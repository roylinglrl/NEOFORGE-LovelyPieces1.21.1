package net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Gamblers;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.SetBonusStage;

import java.util.Arrays;
import java.util.List;


public class Gamblers3Effect implements SetBonusStage {
    public int requiredCount() { return 3; }

    public void onActivated(Player player) {
        player.sendSystemMessage(Component.translatable("set.lsp.gamblers.3effect"));
        player.getAttribute(Attributes.LUCK).addOrReplacePermanentModifier(
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"gambler3"),1, AttributeModifier.Operation.ADD_VALUE)
        );
    }

    public void onTick(Player player) {
        if(player.tickCount%20==0)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 0, true, false));
    }

    public void onDeactivated(Player player) {
        player.sendSystemMessage(Component.literal("3件套失效！"));
        player.getAttribute(Attributes.LUCK).removeModifier(
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"gambler3"),1, AttributeModifier.Operation.ADD_VALUE)
        );
    }
    public List<String> tooltip(){
        return Arrays.asList(
                "lsp.effect.gamblers.3"
        );
    }
}
