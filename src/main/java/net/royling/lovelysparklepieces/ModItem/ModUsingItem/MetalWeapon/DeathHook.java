package net.royling.lovelysparklepieces.ModItem.ModUsingItem.MetalWeapon;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

import java.util.List;

public class DeathHook extends SwordItem {
    public static final Tier GRAVEDIGGER = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL,
            1689,4.5F,0f,22,()-> Ingredient.of(Items.IRON_INGOT));
    public DeathHook() {
        super(GRAVEDIGGER, new Properties().attributes(SwordItem.createAttributes(GRAVEDIGGER,7,-2.6F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof Player player){
            if(ModCurios.hasCurio(player,ModCurios.BLASPHEMOUS_CONTRACT.get())){
                System.out.println("玩家攻击！");
                if (player.getAttackStrengthScale(0.5f) > 0.9f) {
                    target.invulnerableTime=0;
                    DamageSource source =target.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE,
                            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"silver_damage")), attacker);
                    target.hurt(source,8f);
                }
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.space").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.death_hook.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.death_hook.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.death_hook.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.death_hook.des3").withColor(ColorUtil.getRainbow()));
    }
}
