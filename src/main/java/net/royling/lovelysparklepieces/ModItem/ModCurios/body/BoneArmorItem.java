package net.royling.lovelysparklepieces.ModItem.ModCurios.body;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.BoneArmorComponent;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ModDataComponents;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BoneArmorItem extends UniversalCurio {
    public BoneArmorItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        BoneArmorComponent component = stack.get(ModDataComponents.BONE_ARMOR.get());
        if (component == null) {
            // 初始化组件
            component = new BoneArmorComponent(0, 0);
            stack.set(ModDataComponents.BONE_ARMOR.get(), component);
        }
        int times = component.chargeTimes();
        if (times < 3) {
            int count = component.chargeCount();
            if (count >= 20*30) {
                stack.set(ModDataComponents.BONE_ARMOR.get(), new BoneArmorComponent(times + 1, 0));
            } else {
                // 更新计数
                stack.set(ModDataComponents.BONE_ARMOR.get(), new BoneArmorComponent(times, count + 1));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level8").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bone_armor.tip").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bone_armor.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bone_armor.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bone_armor.des2").withColor(ColorUtil.getRainbow()));
    }
}
