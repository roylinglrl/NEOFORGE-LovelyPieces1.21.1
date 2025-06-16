package net.royling.lovelysparklepieces.ModItem.ModCurios.bracelet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModConfigs.LSPConfig;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class VersatilePersonItem extends UniversalCurio {
    public VersatilePersonItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>,AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ModAttribute.CRIT_CHANCE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),0.05, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(ModAttribute.DAMAGE_MODIFIER, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),0.05, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),1, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ARMOR, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),1, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),1, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),2, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        modifiers.put(Attributes.BLOCK_BREAK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"versatile_crit"),0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return modifiers;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level9").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.versatile.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.versatile.des1").withColor(ColorUtil.getRainbow()));
    }
}
