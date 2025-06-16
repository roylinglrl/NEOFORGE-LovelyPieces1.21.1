package net.royling.lovelysparklepieces.ModItem.ModCurios.charm;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class PowerAmulet extends UniversalCurio {
    private static final ResourceLocation POWER_BOOST_ID =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "power_boost");
    public PowerAmulet(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }
        if (player.level().isClientSide) {
            return;
        }
        double speed = calculateHorizontalSpeed(player)*0.05;
        AttributeInstance damageAttr = player.getAttribute(ModAttribute.DAMAGE_MODIFIER);
        if (damageAttr == null) {
            return;
        }
        // 更新或移除修改器
        if (speed > 0.001) {
            damageAttr.addOrReplacePermanentModifier(
                    new AttributeModifier(
                            POWER_BOOST_ID,
                            speed,
                            AttributeModifier.Operation.ADD_VALUE
                    )
            );
        } else {
            damageAttr.removeModifier(POWER_BOOST_ID);
        }
    }
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }
        if (player.level().isClientSide) {
            return;
        }
        AttributeInstance damageAttr = player.getAttribute(ModAttribute.DAMAGE_MODIFIER);
        if (damageAttr != null) {
            damageAttr.removeModifier(POWER_BOOST_ID);
        }
    }
    private static double calculateHorizontalSpeed(Player player){
        Vec3 motion = player.getDeltaMovement();
        double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z)*20;
        return Math.round(speed*10.0)/10.0;
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level3"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.power_amulet.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.power_amulet.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.power_amulet.des2").withColor(ColorUtil.getRainbow(2f)));
    }
}
