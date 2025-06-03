package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEvents.Legendarys.BCEvents;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class EmberCoreItem extends UniversalCurio {
    public EmberCoreItem(Properties properties) {
        super(properties.stacksTo(1));
    }
    public static final float DAMAGE_RADIUS = 3.0f;
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.need.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.ember.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.ember.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.ember.des2").withColor(ColorUtil.getRainbow(2f)));
    }
    public static void tryTriggerEffect(LivingEntity wearer){
        if(!wearer.level().isClientSide){
            Level level = wearer.level();
            level.playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.6F,
                    0.8F + wearer.getRandom().nextFloat() * 0.4F);
            AABB area = new AABB(
                    wearer.getX() - DAMAGE_RADIUS,
                    wearer.getY() - DAMAGE_RADIUS,
                    wearer.getZ() - DAMAGE_RADIUS,
                    wearer.getX() + DAMAGE_RADIUS,
                    wearer.getY() + DAMAGE_RADIUS,
                    wearer.getZ() + DAMAGE_RADIUS
            );
            List<LivingEntity> entities = level.getEntitiesOfClass(
                    LivingEntity.class,
                    area,
                    entity -> entity != wearer && entity.isAttackable()
            );
            DamageSource source = level.damageSources().source(ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"pure_fire_damage")),wearer);
            for (LivingEntity target : entities) {
                // 火焰伤害 + 点燃目标
                target.hurt(source,6);
            }
            createFlameRingEffect((ServerLevel) level,wearer.position());
        }
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this) && ModCurios.hasCurio(player,ModCurios.BLASPHEMOUS_CONTRACT.get());
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

    public static void createFlameRingEffect(ServerLevel level, Vec3 center) {
        // 粒子参数
        final int PARTICLE_COUNT = 80;
        // 粒子数量（更密集）
        final float START_RADIUS = 1.0f;
        // 起始半径
        final float EXPANSION_SPEED = 0.2f;
        // 扩散速度
        final float PARTICLE_SPREAD = 0.1f;
        // 粒子位置随机扩散范围
        final float HEIGHT_VARIATION = 0.5f;
        // 高度变化范围
        double centerX = center.x;
        double centerY = center.y + 0.5;
        // 玩家腰部高度
        double centerZ = center.z;
        // 创建一圈密集的火焰粒子
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            // 计算粒子角度
            double angle = 2 * Math.PI * i / PARTICLE_COUNT;
            // 计算粒子位置（在圆周上）
            double x = centerX + START_RADIUS * Math.cos(angle);
            double z = centerZ + START_RADIUS * Math.sin(angle);
            // 添加随机位置偏移
            double offsetX = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD;
            double offsetY = (level.random.nextDouble() - 0.5) * HEIGHT_VARIATION;
            double offsetZ = (level.random.nextDouble() - 0.5) * PARTICLE_SPREAD;
            // 计算向外扩散的速度方向
            double velX = Math.cos(angle) * EXPANSION_SPEED;
            double velY = 0.1 + level.random.nextDouble() * 0.1;
            double velZ = Math.sin(angle) * EXPANSION_SPEED;

            // 添加随机速度变化
            velX += (level.random.nextDouble() - 0.5) * 0.05;
            velY += (level.random.nextDouble() - 0.5) * 0.05;
            velZ += (level.random.nextDouble() - 0.5) * 0.05;
            // 发送粒子到客户端
            level.sendParticles(
                    ParticleTypes.FLAME,
                    x + offsetX, centerY + offsetY, z + offsetZ,
                    1,
                    velX, velY, velZ,
                    0.1
            );
        }
        // 添加中心爆发效果（可选）
        for (int i = 0; i < 10; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.5;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

            double velX = (level.random.nextDouble() - 0.5) * 0.1;
            double velY = 0.05 + level.random.nextDouble() * 0.1;
            double velZ = (level.random.nextDouble() - 0.5) * 0.1;

            level.sendParticles(
                    ParticleTypes.FLAME,
                    centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                    1,
                    velX, velY, velZ,
                    0.15
            );
        }
    }
}
