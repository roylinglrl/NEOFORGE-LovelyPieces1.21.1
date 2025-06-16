package net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Gamblers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.PlayerData.ChipsData;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class GamblersEarringsItem extends UniversalCurio {
    public GamblersEarringsItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        if (!(slotContext.entity() instanceof Player player)) return;
        if(player.level().isClientSide)return;
        if (player.getCooldowns().isOnCooldown(ModCurios.GAMBLERS_EARRINGS.get())) return;
        if (player.getHealth() / player.getMaxHealth() >= 0.25) return;
        float chance = 0.5f; // 默认值
        if (player.getPersistentData().contains("lsp_chip_count", Tag.TAG_INT)) {
            int chipCount = ChipsData.getChips(player);
            chance = chipCount < 200 ? 0.5f : 1.0f;
        }
        if (player.level().random.nextFloat() < chance) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 800, 1, true, true));
            if (chance >= 0.75f&&!player.getPersistentData().getBoolean("gambler_5effect")) ChipsData.removeChip(player, 10);
        } else {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 300, 0, true, true));
        }

        player.getCooldowns().addCooldown(ModCurios.GAMBLERS_EARRINGS.get(), 2400);
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>,AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"gamblers_health"),2, AttributeModifier.Operation.ADD_VALUE));
        return modifiers;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level2"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gamblers_earrings.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gamblers_earrings.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gamblers_earrings.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gamblers_earrings.des3").withColor(ColorUtil.getRainbow()));
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
