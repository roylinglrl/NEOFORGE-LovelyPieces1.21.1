package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import icyllis.modernui.annotation.Nullable;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.royling.lovelysparklepieces.ModEntity.Abigail.AbigailEntity;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;

import java.util.List;

public class AbigailsFlower extends Item {
    public AbigailsFlower(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        AbigailEntity existingAbigail = findPlayerAbigail(level, player);
        if (player.isShiftKeyDown()) {
            // Shift+右键取消召唤
            if (existingAbigail != null) {
                despawnAbigail(existingAbigail, level);
                return InteractionResultHolder.success(stack);
            }
        }else if(existingAbigail!=null){
            return InteractionResultHolder.success(stack);
        }else {
            if (!level.isClientSide) {
                spawnAbigail((ServerLevel) level, player);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
    @Nullable
    private AbigailEntity findPlayerAbigail(Level level, Player player) {
        List<AbigailEntity> abigails = level.getEntitiesOfClass(
                AbigailEntity.class,
                player.getBoundingBox().inflate(64)
        );

        for (AbigailEntity abigail : abigails) {
            if (abigail.isOwnedBy(player)) {
                return abigail;
            }
        }
        return null;
    }
    private void spawnAbigail(ServerLevel level, Player player) {
        AbigailEntity abigail = new AbigailEntity(ModEntities.ABIGAIL.get(), level);
        abigail.setOwner(player);
        abigail.setPos(player.getX(), player.getY() + 1.0, player.getZ());
        // 添加召唤特效
        level.sendParticles(
                ParticleTypes.ENCHANT,
                player.getX(),
                player.getY() + 1.0,
                player.getZ(),
                30,
                0.5, 0.5, 0.5,
                0.5
        );
        level.addFreshEntity(abigail);
        System.out.println(abigail);
    }
    private void despawnAbigail(AbigailEntity abigail, Level level) {
        // 添加消失特效
        if (!level.isClientSide) {
            ((ServerLevel) level).sendParticles(
                    ParticleTypes.POOF,
                    abigail.getX(),
                    abigail.getY() + 0.5,
                    abigail.getZ(),
                    20,
                    0.3, 0.3, 0.3,
                    0.1
            );
        }
        abigail.discard();
    }
}
