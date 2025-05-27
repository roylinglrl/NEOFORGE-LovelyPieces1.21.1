package net.royling.lovelysparklepieces.ModItem.ModCurios.necklace;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Objects;

public class MoonAmuletItem extends UniversalCurio {
    public MoonAmuletItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    ResourceLocation attack =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"moon_amulet_attack");
    ResourceLocation attack_speed =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"moon_amulet_attack_speed");
    ResourceLocation armor =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"moon_amulet_attack_armor");
    ResourceLocation speed =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"moon_amulet_speed");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            if(slotContext.entity().level().isClientSide)return;
            if(slotContext.entity().level().isNight()){
                Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).addOrReplacePermanentModifier(new AttributeModifier(attack  ,0.08, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_SPEED)).addOrReplacePermanentModifier(new AttributeModifier(attack_speed  ,0.08, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).addOrReplacePermanentModifier(new AttributeModifier(speed  ,0.02, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).addOrReplacePermanentModifier(new AttributeModifier(armor  ,2, AttributeModifier.Operation.ADD_VALUE));
            }
            else {
                Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(attack);
                Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_SPEED)).removeModifier(attack_speed);
                Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(speed);
                Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).removeModifier(armor);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player) {
            Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(attack);
            Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_SPEED)).removeModifier(attack_speed);
            Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(speed);
            Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).removeModifier(armor);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.moon.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.moon.des2").withColor(ColorUtil.getRainbow()));
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
