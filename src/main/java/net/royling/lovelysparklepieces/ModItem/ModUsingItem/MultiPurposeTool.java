package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MultiPurposeTool extends Item {
    public MultiPurposeTool(Properties properties) {
        super(properties.stacksTo(1).durability(512));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        int damage = itemStack.getDamageValue();
        if(itemStack.getDamageValue()+1>=itemStack.getMaxDamage()){
            return ItemStack.EMPTY;
        }
        ItemStack newStack = new ItemStack(itemStack.getItem());
        newStack.setDamageValue(itemStack.getDamageValue()+1);
        return newStack;
    }
}
