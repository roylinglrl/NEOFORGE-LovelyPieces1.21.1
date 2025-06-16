package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Valorant extends UniversalCurio {
    public Valorant(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level12").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.valorant.des5").withColor(ColorUtil.getRainbow()));
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        Player player = (Player) slotContext.entity();
        // 获取取下饰品的玩家
        Level level = player.level();
        // 获取玩家所在的世界
        if (!level.isClientSide) {
            // 确保只在服务器端执行
            // 确保我们是在服务器世界中，才能遍历所有实体
            if (level instanceof ServerLevel serverLevel) {
                // 向下转型为 ServerLevel
                UUID playerUUID = player.getUUID();
                // 遍历世界中的所有实体
                for (Entity entity : serverLevel.getAllEntities()) {
                    // 正确使用 getAllEntities()
                    if (entity instanceof LivingEntity livingEntity) {
                        CompoundTag persistentData = livingEntity.getPersistentData();

                        // 检查实体是否是“妈妈”并且是当前玩家的“妈妈”
                        if (persistentData.getBoolean("lsp_isMom")) {
                            UUID momSonUUID = persistentData.getUUID("lsp_son");
                            if (momSonUUID != null && momSonUUID.equals(playerUUID)) {
                                // 清除“妈妈”的标记数据
                                persistentData.remove("lsp_isMom");
                                persistentData.remove("lsp_son");

                                // 将生物名称恢复为默认
                                livingEntity.setCustomName(null);
                                livingEntity.setCustomNameVisible(false);

                                // 恢复生物的AI行为
                                if (livingEntity instanceof Mob mob) {
                                    mob.setTarget(null);
                                    // 清除当前目标，让AI重新评估
                                    // **重要：如果之前你在设置为妈妈时移除了特定的攻击玩家Goal，
                                    // 你需要在这里重新添加它们。否则，AI会自动恢复默认行为。**
                                    // 示例 (如果之前移除了，请取消注释并根据实际情况调整优先级和目标类型)：
                                    // mob.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(mob, Player.class, true));
                                }
                                // 如果需要，给玩家发送消息
                                player.sendSystemMessage(Component.literal(livingEntity.getDisplayName().getString() + " 变回了普通的生物。"));
                            }
                        }
                    }
                }
            }
            // 清除玩家自己的妈妈计数
            player.getPersistentData().putInt("lsp_momCount", 0);
            player.sendSystemMessage(Component.literal("你所有的妈妈都变回了普通生物。"));
        }
    }
}
