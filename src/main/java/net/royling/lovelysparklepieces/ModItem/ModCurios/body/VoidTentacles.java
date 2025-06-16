package net.royling.lovelysparklepieces.ModItem.ModCurios.body;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class VoidTentacles extends UniversalCurio {
    public VoidTentacles(Properties properties) {
        super(properties.stacksTo(1));
    }
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        if(!slotContext.entity().level().isClientSide){
            LivingEntity player = slotContext.entity();
            CuriosApi.getCuriosInventory(player).ifPresent(inv->{
                Multimap<String, AttributeModifier> modifiers =
                        HashMultimap.create();
                modifiers.put("ring", new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_ring"),
                        4, AttributeModifier.Operation.ADD_VALUE
                ));
                modifiers.put("bracelet",new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_bracelet"),
                        1, AttributeModifier.Operation.ADD_VALUE
                ));
                inv.addPermanentSlotModifiers(modifiers);
            });
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(!slotContext.entity().level().isClientSide){
            LivingEntity player = slotContext.entity();
            CuriosApi.getCuriosInventory(player).ifPresent(inv->{
                Multimap<String, AttributeModifier> modifiers =
                        HashMultimap.create();
                modifiers.put("ring",new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_ring"),
                        1, AttributeModifier.Operation.ADD_VALUE
                ));
                modifiers.put("bracelet",new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_bracelet"),
                        4, AttributeModifier.Operation.ADD_VALUE
                ));
                inv.removeSlotModifiers(modifiers);
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level6").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.void_tentacles.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.void_tentacles.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.void_tentacles.des2").withColor(ColorUtil.getRainbow()));
    }
}
