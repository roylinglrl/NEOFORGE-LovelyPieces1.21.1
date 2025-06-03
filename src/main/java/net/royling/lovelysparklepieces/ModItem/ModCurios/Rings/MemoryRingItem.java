package net.royling.lovelysparklepieces.ModItem.ModCurios.Rings;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Objects;

public class MemoryRingItem extends Item implements ICurioItem{
    private static final ResourceLocation ATTID = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lsp_memory_attack");

    public MemoryRingItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    public float getMemory(Player player){
        double lsp_memory = player.getPersistentData().getDouble("lsp_memory");
        return calculateMemoryBonus(lsp_memory);
    }
    public float calculateMemoryBonus(double lsp_memory){
        // 内存越低伤害加成越高，内存越高伤害加成越低
        if (lsp_memory <= 4.0) {
            return 0.3f; // 4GB及以下：+30%
        } else if (lsp_memory <= 16.0) {
            // 4GB-16GB线性变化：从+30%到-30%
            return (float) (0.3 - (lsp_memory - 4.0) * (0.6 / 12.0));
        } else {
            return -0.3f; // 16GB以上：-30%
        }
    }
    public float curMem(){
        Runtime runtime = Runtime.getRuntime();
        return (float) runtime.maxMemory() /1024/1024;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity()instanceof Player player) {
            if (player.level().isClientSide) return;
            AttributeModifier newModifier = new AttributeModifier(ATTID,
                    getMemory(player), AttributeModifier.Operation.ADD_VALUE);
            Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER)).removeModifier(newModifier);
            Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER)).addOrReplacePermanentModifier(newModifier);
        }
    }
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            long fps = player.getPersistentData().getLong("lsp_fpsvalue");
            if(Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER)).getModifier(ATTID)==null){
                Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER)).addOrReplacePermanentModifier(new AttributeModifier(
                        ATTID,getMemory(player), AttributeModifier.Operation.ADD_VALUE
                ));
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER)).removeModifier(ATTID);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level6"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.lsp_memory_ring.basic").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.lsp_memory_ring.basic2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.lsp_memory_ring.basic3",curMem()).withColor(ColorUtil.getRainbow()));
        double maxMemoryGB = Runtime.getRuntime().maxMemory() / 1024.0 / 1024 / 1024;
        float bonus = calculateMemoryBonus(maxMemoryGB);
        tooltipComponents.add(Component.translatable(
                "tooltip.lovely_sparkle_pieces.lsp_memory_ring.bonus",
                String.format("%.2f%%", bonus * 100)
        ).withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable(
                "tooltip.lovely_sparkle_pieces.lsp_memory_ring.des"
        ).withColor(ColorUtil.getRainbow()));
        super.appendHoverText(stack,context,tooltipComponents,tooltipFlag);
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }

}
