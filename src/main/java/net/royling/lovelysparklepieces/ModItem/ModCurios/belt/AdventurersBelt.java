package net.royling.lovelysparklepieces.ModItem.ModCurios.belt;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Objects;

public class AdventurersBelt extends UniversalCurio {
    private static final ResourceLocation DAMAGE_MODIFY = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"adventurer");

    public AdventurersBelt(Properties properties) {
        super(properties.stacksTo(1));
    }
    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>,AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ModAttribute.DAMAGE_MODIFIER, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"adventure_base_attack"),+0.05, AttributeModifier.Operation.ADD_VALUE));
        return modifiers;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(!(slotContext.entity() instanceof Player player))return;
        if(player.level().isClientSide)return;
        BlockPos pos = player.blockPosition();
        int distance = Math.abs(pos.getX())+Math.abs(pos.getZ());
        double bonus = Math.min(distance/10000.0,1)*0.25;
        System.out.print(bonus);
        AttributeModifier newModifier = new AttributeModifier(DAMAGE_MODIFY,
                bonus, AttributeModifier.Operation.ADD_VALUE);
        Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER))
                .addOrReplacePermanentModifier(newModifier);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(!(slotContext.entity() instanceof Player player))return;
        if(player.level().isClientSide)return;
        if(player.tickCount%200!=0)return;
        BlockPos pos = player.blockPosition();
        int distance = Math.abs(pos.getX())+Math.abs(pos.getZ());
        double bonus = Math.min(distance/10000.0,1)*0.25;
        System.out.print(bonus);
        AttributeModifier newModifier = new AttributeModifier(DAMAGE_MODIFY,
                bonus, AttributeModifier.Operation.ADD_VALUE);
        Objects.requireNonNull(player.getAttribute(ModAttribute.DAMAGE_MODIFIER))
                .addOrReplacePermanentModifier(newModifier);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof ServerPlayer player) {
            AttributeInstance attr = player.getAttribute(ModAttribute.DAMAGE_MODIFIER);
            if (attr != null) attr.removeModifier(DAMAGE_MODIFY);
        }
    }


    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        // 添加空检查防止NullPointerException
        if (context.level() == null) {
            // 当level为null时添加默认提示
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer.des").withColor(ColorUtil.getRainbow()));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer.des2").withColor(ColorUtil.getRainbow()));
            tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer_add", 0.0f).withColor(ColorUtil.getRainbow()));
            return;
        }
        Player player = Minecraft.getInstance().player;
        BlockPos pos = null;
        int distance = 0;
        if (player != null) {
            pos = player.blockPosition();
            if (pos != null) {
                distance = Math.abs(pos.getX()) + Math.abs(pos.getZ());
            }
        }
        double bonus = Math.min(distance / 10000.0, 1) * 0.25;
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.adventurer_add", (float)bonus * 100).withColor(ColorUtil.getRainbow()));
    }
}

