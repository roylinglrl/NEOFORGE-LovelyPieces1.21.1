package net.royling.lovelysparklepieces.ModEvents.Legendarys;

import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class ValorantEvent {
    private static final int MAX_MOMMIES_PER_PLAYER = 4;

    @SubscribeEvent
    public static void onPlayerInteEntity(PlayerInteractEvent.EntityInteract event){
        if (event.getLevel().isClientSide) {
            return;
        }
        Player player = event.getEntity();
        if(!player.isShiftKeyDown())return;
        if(!ModCurios.hasCurio(player,ModCurios.VALORANT.get()))return;
        Entity target = event.getTarget();
        if(!(target instanceof LivingEntity targetEntity))return;
        if(targetEntity.isBaby())return;
        if(targetEntity.getType().is(Tags.EntityTypes.BOSSES))return;
        if(targetEntity instanceof Player)return;
        int momCount = player.getPersistentData().getInt("lsp_momCount");
        if(momCount>=4)return;
        if(targetEntity.getPersistentData().getBoolean("lsp_isMom")){
            if(targetEntity.getPersistentData().getUUID("lsp_son").equals(player.getUUID()))
                player.sendSystemMessage(Component.literal("这个实体已经是你的妈妈了！"));
            else player.sendSystemMessage(Component.literal("这个实体已经是别人的妈妈了！"));
            return;
        }
        targetEntity.setCustomName(Component.literal("妈妈！"));
        targetEntity.setCustomNameVisible(true);
        targetEntity.getPersistentData().putBoolean("lsp_isMom",true);
        targetEntity.getPersistentData().putUUID("lsp_son",player.getUUID());
        player.getPersistentData().putInt("lsp_momCount",momCount+1);
        player.sendSystemMessage(Component.literal("你现在有一个新妈妈！你现在有 " + (momCount + 1) + " 个妈妈了。"));
    }
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event){
        LivingEntity deceased = event.getEntity();
        CompoundTag compoundTag = deceased.getPersistentData();
        if(compoundTag.getBoolean("lsp_isMom")){
            UUID sonUUID = compoundTag.getUUID("lsp_son");
            if(sonUUID !=null){
                if(deceased.level().isClientSide)return;
                Level level = deceased.level();
                Player son = level.getPlayerByUUID(sonUUID);
                if(son!= null){
                    int momCount = son.getPersistentData().getInt("lsp_momCount");
                    if(momCount>0){
                        son.getPersistentData().putInt("lsp_momCount",momCount-1);
                        son.sendSystemMessage(net.minecraft.network.chat.Component.literal("你的一个妈妈去世了。你现在有 " + (momCount - 1) + " 个妈妈了。"));
                    }
                }

            }
        }
    }
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide) {
            return;
        }
        if (entity instanceof LivingEntity livingEntity) {
            CompoundTag persistentData = livingEntity.getPersistentData();

            if (persistentData.getBoolean("lsp_isMom")) {
                UUID sonUUID = persistentData.getUUID("lsp_son");
                if (sonUUID != null) {
                    Level level = entity.level();
                    Player son = level.getPlayerByUUID(sonUUID);
                    if (son != null) {
                        int momCount = son.getPersistentData().getInt("lsp_momCount");
                        // 减少计数前，检查这个实体是否真的已经从世界中移除，而不是仅仅在区块边界移动
                        // 通常 LivingLeaveLevelEvent 意味着它确实要离开了
                        if (momCount > 0) {
                            son.getPersistentData().putInt("lsp_momCount", momCount - 1);
                            son.sendSystemMessage(Component.literal("你的一个妈妈消失了。你现在有 " + (momCount - 1) + " 个妈妈了。"));
                        }
                        // 移除妈妈的标记，防止它再次被计入
                        persistentData.remove("lsp_isMom");
                        persistentData.remove("lsp_son");
                    }
                }
            }
        }
    }
    private static final double TELEPORT_DISTANCE_SQ = 64 * 64;
    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event){
        if (!(event.getLevel() instanceof ServerLevel serverLevel))return;
        if(serverLevel.getGameTime()%80!=0)return;
        for (Entity entity : serverLevel.getAllEntities()){
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.getPersistentData().getBoolean("lsp_isMom")) {
                    UUID sonUUID = livingEntity.getPersistentData().getUUID("lsp_son");
                    if (sonUUID != null) {
                        Player son = serverLevel.getPlayerByUUID(sonUUID);
                        if (son != null) {
                            double distanceSq = livingEntity.distanceToSqr(son);

                            // 如果距离过远，传送“妈妈”到玩家身边
                            if (distanceSq > TELEPORT_DISTANCE_SQ) {
                                // 尝试传送到玩家周围的安全位置
                                BlockPos teleportPos = findSafeTeleportLocation(serverLevel, son.blockPosition());
                                if (teleportPos != null) {
                                    livingEntity.teleportTo(teleportPos.getX() + 0.5, teleportPos.getY(), teleportPos.getZ() + 0.5);
                                    // 可以给“妈妈”加上一些效果，例如粒子效果，表示传送
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // 辅助方法：找到玩家周围一个安全的传送位置
    private static BlockPos findSafeTeleportLocation(Level level, BlockPos playerPos) {
        for (int i = 0; i < 10; i++) {
            // 尝试几次
            int xOffset = (int) (Math.random() * 6) - 3;
            // -3 到 +2
            int zOffset = (int) (Math.random() * 6) - 3;
            BlockPos checkPos = playerPos.offset(xOffset, 0, zOffset);
            // 尝试与玩家同高
            // 检查上方是否有足够空间
            if (level.getBlockState(checkPos).isSolid() && !level.getBlockState(checkPos.above()).isSolid() && !level.getBlockState(checkPos.above(2)).isSolid()) {
                return checkPos.above();
                // 传送到方块上方
            }
            // 尝试在玩家脚下或更高一点的位置
            if (!level.getBlockState(checkPos).isSolid() && level.getBlockState(checkPos.below()).isSolid()) {
                return checkPos;
            }
        }
        return playerPos;
        // 如果找不到安全位置，就传送到玩家当前位置（可能不安全）
    }
    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {
        // 尝试获取正在改变目标的生物
        LivingEntity mob = event.getEntity();
        LivingEntity newTarget = event.getNewAboutToBeSetTarget();
        // 新的目标
        // 检查这个生物是否是“妈妈”
        if (mob.getPersistentData().getBoolean("lsp_isMom")) {
            UUID sonUUID = mob.getPersistentData().getUUID("lsp_son");
            // 检查“妈妈”的“儿子”UUID是否存在且有效
            if (newTarget instanceof Player) {
                // 如果新的目标是玩家，并且这个玩家是“妈妈”的“儿子”
                if (newTarget.getUUID().equals(sonUUID)) {
                    event.setNewAboutToBeSetTarget(null);
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event){
        LivingEntity damagedEntity = event.getEntity();
        LivingEntity directAttacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;
        if (directAttacker instanceof Player) {
            // 如果攻击者是玩家
            Player sonCandidate = (Player) directAttacker;
            if (damagedEntity.getPersistentData().getBoolean("lsp_isMom")) {
                // 如果受伤害的是妈妈
                UUID momSonUUID = damagedEntity.getPersistentData().getUUID("lsp_son");
                if (momSonUUID != null && momSonUUID.equals(sonCandidate.getUUID())) {
                    // 并且这个妈妈的儿子是当前攻击者
                    event.setCanceled(true);
                    // 取消伤害
                    return;
                    // 已经处理，直接返回
                }
            }
        }
        if (damagedEntity instanceof Player) {
            // 如果受伤害的是玩家
            Player sonCandidate = (Player) damagedEntity;
            if (directAttacker != null && directAttacker.getPersistentData().getBoolean("lsp_isMom")) {
                // 如果攻击者是妈妈
                UUID momSonUUID = directAttacker.getPersistentData().getUUID("lsp_son");
                if (momSonUUID != null && momSonUUID.equals(sonCandidate.getUUID())) {
                    // 并且这个妈妈的儿子是当前受伤害的玩家
                    event.setCanceled(true);
                    // 取消伤害
                    return; // 已经处理，直接返回
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingPlayerDeath(LivingDeathEvent event) {
        LivingEntity deceasedEntity = event.getEntity();

        // 检查死亡的实体是否是玩家
        if (deceasedEntity instanceof Player player) {
            // 确保只在服务器端执行
            if (!player.level().isClientSide) {
                ServerLevel serverLevel = (ServerLevel) player.level(); // 向下转型为 ServerLevel
                UUID playerUUID = player.getUUID();

                // 遍历世界中的所有实体
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (entity instanceof LivingEntity livingEntity) {
                        CompoundTag persistentData = livingEntity.getPersistentData();

                        // 检查实体是否是“妈妈”并且是当前死亡玩家的“妈妈”
                        if (persistentData.getBoolean("lsp_isMom")) {
                            UUID momSonUUID = persistentData.getUUID("lsp_son");
                            if (momSonUUID != null && momSonUUID.equals(playerUUID)) {
                                livingEntity.discard();
                                player.getPersistentData().putInt("lsp_momCount", 0);
                                player.sendSystemMessage(Component.literal("你失去了所有的妈妈~。"));
                            }
                        }
                    }
                }
            }
        }
    }
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        LivingEntity damagedEntity = event.getEntity(); // The entity that took damage
        LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null; // The direct attacker

        // Ensure it's a player taking damage, and there's a valid attacker that isn't the player themselves
        if (damagedEntity instanceof Player player && attacker != null && attacker != player) {
            // If the attacker is a mommy, the damage should have been cancelled by MommyCombatHandler.
            // So, no need for mommies to counter-attack other mommies or themselves.
            if (attacker.getPersistentData().getBoolean("lsp_isMom")) {
                return;
            }

            // Define the search radius for mommies
            double searchRadius = 32.0; // E.g., search within 32 blocks of the player

            // Ensure we are on the server side and get ServerLevel
            if (!player.level().isClientSide && player.level() instanceof ServerLevel serverLevel) {
                AABB searchBox = player.getBoundingBox().inflate(searchRadius); // Expand player's bounding box as search area
                List<Entity> nearbyEntities = serverLevel.getEntities(
                        player, // Exclude the player from the search results
                        searchBox,
                        entity -> entity instanceof Mob && // Ensure it's a mob that can be set as target
                                entity.getPersistentData().getBoolean("lsp_isMom") && // It's a mommy
                                entity.getPersistentData().getUUID("lsp_son") != null && // It has a son UUID
                                entity.getPersistentData().getUUID("lsp_son").equals(player.getUUID()) // It's this player's mommy
                );
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof Mob mommyMob) {
                        // Check if the mommy currently has no target, or its current target is not the attacker
                        if (mommyMob.getTarget() == null || mommyMob.getTarget() != attacker) {
                            // Set the attacker as the mommy's new target
                            mommyMob.setTarget(attacker);
                            // The mommy's AI will then try to attack this new target
                        }
                    }
                }
            }
        }
    }

}
