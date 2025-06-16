package net.royling.lovelysparklepieces.ModItem.ModCurios.belt;

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
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.List;

public class AmuletPouchItem extends UniversalCurio {
    public AmuletPouchItem(Properties properties) {
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
                modifiers.put("charm", new AttributeModifier(
                       ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_amulet"),
                        4, AttributeModifier.Operation.ADD_VALUE
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
                modifiers.put("charm",new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"new_amulet"),
                        4, AttributeModifier.Operation.ADD_VALUE
                ));
                inv.removeSlotModifiers(modifiers);
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.amulet_pouch.des").withColor(ColorUtil.getRainbow(2f)));
    }
}
