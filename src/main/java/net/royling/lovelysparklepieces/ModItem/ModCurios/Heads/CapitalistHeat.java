package net.royling.lovelysparklepieces.ModItem.ModCurios.Heads;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CapitalistHeat extends UniversalCurio {
    private static final int INTERVAL = 80; // 4秒 = 80 tick
    private static final double RADIUS = 8.0; // 作用半径8格
    public CapitalistHeat(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level level = entity.level();
        if (!level.isClientSide && entity.tickCount % INTERVAL == 0) {
            // 获取范围内所有铁傀儡
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            List<IronGolem> golems = level.getEntitiesOfClass(
                    IronGolem.class,
                    new AABB(x - RADIUS, y - RADIUS, z - RADIUS,
                            x + RADIUS, y + RADIUS, z + RADIUS)
            );
            for (IronGolem golem : golems) {
                // 创建无来源的魔法伤害
                DamageSource magicDamage = golem.damageSources().magic();
                // 施加足以致死的伤害（当前生命值 + 1确保致死）
                golem.hurt(magicDamage, 25f);
                // 检查是否因这次伤害死亡
                if (golem.isDeadOrDying()) {
                    // 确保只在服务端生成掉落物
                    if (!golem.level().isClientSide()) {
                        // 创建蛋糕掉落物
                        ItemEntity cakeDrop = new ItemEntity(
                                golem.level(),
                                golem.getX(),
                                golem.getY(),
                                golem.getZ(),
                                new ItemStack(ModItems.CAPITALIST_CAKE.get())
                        );
                        // 添加轻微弹跳效果
                        cakeDrop.setDeltaMovement(
                                (golem.getRandom().nextDouble() - 0.5) * 0.1,
                                0.2,
                                (golem.getRandom().nextDouble() - 0.5) * 0.1
                        );
                        // 将掉落物加入世界
                        golem.level().addFreshEntity(cakeDrop);
                    }
                }
                // 强制掉落铁锭（每次必掉）
                level.addFreshEntity(new ItemEntity(
                        level,
                        golem.getX(),
                        golem.getY() + 0.5,
                        golem.getZ(),
                        new ItemStack(Items.IRON_INGOT)
                )
                );
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_heat.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_heat.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_heat.des3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.capitalist_heat.des4").withColor(ColorUtil.getRainbow()));
    }
}
