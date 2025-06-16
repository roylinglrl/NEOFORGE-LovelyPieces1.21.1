package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class MirrorAndWater extends UniversalCurio {
    public MirrorAndWater(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>,AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"bc_speed"),0.000000000001, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(ModAttribute.DAMAGE_MODIFIER, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"bc_attack"),-0.000000000001, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ARMOR, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"bc_armor"),-0.000000000001, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        return modifiers;
    }
    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level12"));
        if(!Screen.hasShiftDown()){
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.desx").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.des2x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.press shift").withStyle(ChatFormatting.DARK_PURPLE));
        }
        else {
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.rewardx").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward1x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward2x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward3x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward4x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward5x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward6x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.reward7x").withStyle(ChatFormatting.DARK_GREEN));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punishx").withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish1x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish2x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish3x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish4x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish5x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish6x").withStyle(ChatFormatting.DARK_RED));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bs.punish7x").withStyle(ChatFormatting.DARK_RED));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return true;
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
