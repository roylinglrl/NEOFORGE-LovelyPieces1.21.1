package net.royling.lovelysparklepieces.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import org.jetbrains.annotations.NotNull;

public class WildfirePortalBlock extends Block {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);
    public WildfirePortalBlock(Properties properties) {
        super(properties.mapColor(MapColor.COLOR_BLACK)
                .lightLevel(state->15)
                .strength(-1.0f,360000000f)
                .noLootTable()
                .noCollission()
                .noOcclusion()
        );
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            if (entity instanceof ServerPlayer player) {
                if (!player.isOnPortalCooldown()) {
                    player.setPortalCooldown(); 
                    teleportPlayer(player);
                }
            }
        }
    }
    private void teleportPlayer(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {return;}
        ResourceKey<Level> burningWastelandDimension = ResourceKey.create(
                ResourceKey.createRegistryKey( ResourceLocation.parse("minecraft:dimension")),
                 ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "the_wildfireland")
        );
        ResourceKey<Level> currentDimension = player.level().dimension();

        ServerLevel destinationWorld; // 初始化为null
        Vec3 targetPosition;         // 初始化为null
        BlockPos spawnBlockPos = null;
        if (currentDimension.equals(burningWastelandDimension)) {
            ResourceKey<Level> respawnDim = player.getRespawnDimension();
            BlockPos respawnPos = player.getRespawnPosition();
            float respawnAngle = player.getRespawnAngle();
            if (respawnPos != null) {
                ServerLevel respawnLevel = server.getLevel(respawnDim);
                if (respawnLevel != null) {
                    destinationWorld = respawnLevel;
                    targetPosition = new Vec3(respawnPos.getX() + 0.5, respawnPos.getY() + 1.0, respawnPos.getZ() + 0.5);
                    spawnBlockPos = respawnPos;
                } else {
                    destinationWorld = server.getLevel(Level.OVERWORLD);
                    if (destinationWorld != null) {
                        spawnBlockPos = destinationWorld.getSharedSpawnPos();
                        targetPosition = new Vec3(
                                destinationWorld.getSharedSpawnPos().getX() + 0.5,
                                destinationWorld.getSharedSpawnPos().getY() + 1.0,
                                destinationWorld.getSharedSpawnPos().getZ() + 0.5
                        );
                    } else {
                        return; // 无法返回，中止传送
                    }
                }
            } else {
                // 没有设定重生点，返回主世界默认出生点
                destinationWorld = server.getLevel(Level.OVERWORLD);
                if (destinationWorld != null) {
                    spawnBlockPos = destinationWorld.getSharedSpawnPos();
                    targetPosition = new Vec3(
                            destinationWorld.getSharedSpawnPos().getX() + 0.5,
                            destinationWorld.getSharedSpawnPos().getY() + 1.0,
                            destinationWorld.getSharedSpawnPos().getZ() + 0.5
                    );
                } else {
                    return; // 无法返回，中止传送
                }
            }
        } else if (currentDimension.equals(Level.OVERWORLD)) {
            // 如果玩家当前在主世界，则传送到荒火地出生点
            destinationWorld = server.getLevel(burningWastelandDimension);
            if (destinationWorld == null) {
                return; // 目标维度不存在，中止传送
            }
            // 传送到荒火地的共享出生点
            spawnBlockPos = destinationWorld.getSharedSpawnPos();
            targetPosition = new Vec3(
                    destinationWorld.getSharedSpawnPos().getX() + 0.5,
                    destinationWorld.getSharedSpawnPos().getY() + 1.0,
                    destinationWorld.getSharedSpawnPos().getZ() + 0.5
            );

        } else {
            return;
        }
        DimensionTransition transition = getDimensionTransition(player, destinationWorld, targetPosition,spawnBlockPos);
        player.changeDimension(transition);
    }
    private static @NotNull DimensionTransition getDimensionTransition(ServerPlayer player, ServerLevel destinationWorld, Vec3 targetPosition,BlockPos spawnBlockPos) {
        DimensionTransition.PostDimensionTransition postTransitionBehavior = (transferredEntity) -> {
            if (transferredEntity instanceof ServerPlayer transferredPlayer && transferredPlayer.level() == destinationWorld) {
                transferredPlayer.teleportTo(
                        destinationWorld,
                        targetPosition.x(),
                        targetPosition.y(),
                        targetPosition.z(),
                        transferredPlayer.getYRot(),
                        transferredPlayer.getXRot()
                );
                ResourceLocation wildFirelandID = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "the_wildfireland");
                if (destinationWorld.dimension().location().equals(wildFirelandID)) {
                    // 生成5x5黑曜石平台
                    generateObsidianPlatform(destinationWorld, spawnBlockPos);
                }
            }
        };
        Vec3 zeroVelocity = Vec3.ZERO;
        float currentYRot = player.getYRot();
        float currentXRot = player.getXRot();
        return new DimensionTransition(
                destinationWorld,
                targetPosition,
                zeroVelocity,
                currentYRot,
                currentXRot,
                postTransitionBehavior
        );
    }
    private static void generateObsidianPlatform(ServerLevel world, BlockPos center) {
        int platformSize = 5; // 5x5平台
        int halfSize = platformSize / 2;
        int centerX = center.getX();
        int centerY = center.getY();
        int centerZ = center.getZ();

        for (int x = centerX - halfSize; x <= centerX + halfSize; x++) {
            for (int z = centerZ - halfSize; z <= centerZ + halfSize; z++) {
                BlockPos pos = new BlockPos(x, centerY, z);
                world.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3); // 3 = 更新和通知
            }
        }
    }
}
