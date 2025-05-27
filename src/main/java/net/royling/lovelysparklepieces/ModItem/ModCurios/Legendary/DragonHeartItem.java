package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModEvents.Legendarys.BCEvents;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class DragonHeartItem extends UniversalCurio {
    public DragonHeartItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>,AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"dh_health"),8, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"dh_attack"),4, AttributeModifier.Operation.ADD_VALUE));
        modifiers.put(ModAttribute.CRIT_CHANCE, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"dh_crit"),0.15, AttributeModifier.Operation.ADD_VALUE));
        return modifiers;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            if(!BCEvents.hasBlasphemousContract(player)){
                CuriosApi.getCuriosInventory(player).ifPresent(curios->{
                    curios.getStacksHandler(slotContext.identifier()).ifPresent(handler->{
                        handler.getStacks().setStackInSlot(slotContext.index(),ItemStack.EMPTY)
                        ;        player.level().addFreshEntity(new ItemEntity(
                                player.level(),
                                player.getX(),
                                player.getY() + 0.5, // 避免卡在方块内
                                player.getZ(),
                                stack.copy()));
                    });
                });
            }
        }
    }
}
