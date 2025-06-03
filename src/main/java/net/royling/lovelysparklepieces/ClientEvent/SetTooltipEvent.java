package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.SetBonus;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.SetBonusRegistry;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.SetBonusStage;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 14795
 */
@EventBusSubscriber(modid = LovelySparklePieces.MODID,value = Dist.CLIENT)
public class SetTooltipEvent {
    @SubscribeEvent
    public static void setTooltip(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List<Component> tooltip = event.getToolTip();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        for (SetBonus bonus : SetBonusRegistry.ALL){
            if (bonus.matches(item)){
                tooltip.add(Component.literal("-------------------").withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable("tooltip.set_bonus.title").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
                tooltip.add(Component.translatable("tooltip.set_bonus.items")
                        .withStyle(ChatFormatting.YELLOW));
                bonus.getItems().forEach(setItem -> {
                    // 检查玩家是否穿戴了该物品
                    boolean isEquipped = CuriosApi.getCuriosInventory(player)
                            .map(handler ->
                                    !handler.findCurios(testStack -> testStack.getItem() == setItem).isEmpty()
                            )
                            .orElse(false);
                    // 根据穿戴状态设置颜色
                    ChatFormatting color = isEquipped ? ChatFormatting.GREEN : ChatFormatting.GRAY;
                    tooltip.add(Component.translatable("tooltip.set_bonus.item_entry",
                                    setItem.getDescription())
                            .withStyle(color));
                });                        tooltip.add(Component.translatable("tooltip.set_bonus.effects")
                        .withStyle(ChatFormatting.YELLOW));

                bonus.getStages().forEach(stage ->
                        tooltip.add(Component.translatable("tooltip.set_bonus.stage_entry",
                                        stage.requiredCount(),
                                        Component.translatable(getStageTranslationKey(stage)))
                                .withStyle(ChatFormatting.BLUE))
                );
                break;
            }
        }
    }
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        for (int i = 0; i < event.getToolTip().size(); i++) {
            Component component = event.getToolTip().get(i);
            String rawText = component.getString();
            // 获取原始文本

            // 获取当前语言的属性名称（自动适配中英文）
            String damageKey = Component.translatable("attribute.lsp.damage_modifier").getString();

            // 构建动态正则表达式
            Pattern pattern = Pattern.compile(
                    "([+-])(\\d+\\.?\\d*)\\s*" + Pattern.quote(damageKey)
            );

            Matcher matcher = pattern.matcher(rawText);
            if (matcher.find()) {
                // 转换数值到百分比
                double value = Double.parseDouble(matcher.group(2));
                int percentage = (int) Math.round(value * 100);

                // 构建新文本（保留符号和样式）
                MutableComponent newText = Component.literal(matcher.group(1) + percentage + "% ")
                        .append(Component.translatable("attribute.lsp.damage_modifier"))
                        .withStyle(component.getStyle());

                event.getToolTip().set(i, newText);
            }
        }
    }

    private static String getStageTranslationKey(SetBonusStage stage) {
        return stage.tooltip();
    }
}
