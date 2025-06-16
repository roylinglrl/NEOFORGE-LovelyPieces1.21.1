package net.royling.lovelysparklepieces.ModFeature;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;

public class MoonstoneCraterFeature extends Feature<MoonstoneCraterConfig> {
    // 矿石类型及其概率权重
    private static final OreType[] ORE_TYPES = {
            new OreType(Blocks.COAL_ORE.defaultBlockState(), 50),  // 煤矿最常见 (50%权重)
            new OreType(Blocks.COPPER_ORE.defaultBlockState(), 30), // 铜矿次之 (30%权重)
            new OreType(Blocks.IRON_ORE.defaultBlockState(), 15),  // 铁矿较少 (15%权重)
            new OreType(Blocks.GOLD_ORE.defaultBlockState(), 5)    // 金矿最稀有 (5%权重)
    };

    // 矿石放置密度 (每立方格的概率)
    private static final float ORE_DENSITY = 0.08f; // 3%的坑洞方块会变成矿石
    public MoonstoneCraterFeature() {
        super(MoonstoneCraterConfig.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<MoonstoneCraterConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        MoonstoneCraterConfig config = context.config();
        int radius = config.radius();
        int maxDepth = config.depth();

        // 获取实际地表高度
        BlockPos surfacePos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, origin);

        // 确保是地表方块
        if (!level.getBlockState(surfacePos.below()).isSolid()) {
            return false;
        }

        // 生成完美的圆形坑洞
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // 计算当前点到中心的距离
                double distance = Math.sqrt(x * x + z * z);

                // 如果点在圆内
                if (distance <= radius) {
                    // 计算该点的深度（抛物线形状）
                    double depthFactor = 1 - Math.pow(distance / radius, 2);
                    int depth = (int) Math.round(maxDepth * depthFactor);

                    // 从地表向下挖掘到计算出的深度
                    for (int y = 0; y >= -depth; y--) {
                        BlockPos pos = surfacePos.offset(x, y, z);

                        // 移除方块创建坑洞
                        if (level.getBlockState(pos).isSolid()) {
                            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }

                    // 移除坑洞上方的所有方块（从地表到天空）
                    for (int y = 1; y <= level.getMaxBuildHeight() - surfacePos.getY(); y++) {
                        BlockPos abovePos = surfacePos.offset(x, y, z);
                        if (!level.getBlockState(abovePos).isAir()) {
                            level.setBlock(abovePos, Blocks.AIR.defaultBlockState(), 3);
                        }
                    }

                    // 在坑底放置特殊方块（如沙子）
                    if (depth > 0) {
                        BlockPos bottomPos = surfacePos.offset(x, -depth, z);
                        if (level.getBlockState(bottomPos).isAir()) {
                            // 根据深度选择不同材质
                            if (depth > maxDepth * 0.7) {
                                level.setBlock(bottomPos.below(), Blocks.SAND.defaultBlockState(), 3);
                            } else {
                                level.setBlock(bottomPos.below(), Blocks.GRAVEL.defaultBlockState(), 3);
                            }
                        }
                    }

                    // 在坑壁和坑底随机放置矿石
                    for (int y = -depth; y <= 0; y++) {
                        BlockPos orePos = surfacePos.offset(x, y, z);

                        // 确保位置是坑壁或坑底
                        if (level.getBlockState(orePos).isAir() &&
                                level.getBlockState(orePos.below()).isSolid() &&
                                random.nextFloat() < ORE_DENSITY) {

                            // 根据概率权重选择矿石类型
                            placeRandomOre(level, orePos.below(), random);
                        }
                    }
                }
            }
        }

        // 在坑底生成月石
        int stoneCount = config.minStones() + random.nextInt(config.maxStones() - config.minStones() + 1);
        for (int i = 0; i < stoneCount; i++) {
            // 随机角度和半径（更靠近中心）
            double angle = random.nextDouble() * 2 * Math.PI;
            double stoneDistance = random.nextDouble() * radius * 0.7; // 70%半径范围内

            int offsetX = (int) Math.round(stoneDistance * Math.cos(angle));
            int offsetZ = (int) Math.round(stoneDistance * Math.sin(angle));

            // 计算深度
            double depthFactor = 1 - Math.pow(stoneDistance / radius, 2);
            int depth = (int) Math.round(maxDepth * depthFactor);

            BlockPos stonePos = surfacePos.offset(offsetX, -depth, offsetZ);

            // 确保位置有效
            if (level.isEmptyBlock(stonePos.above())) {
                level.setBlock(stonePos, ModBlocks.MOONSTONE.get().defaultBlockState(), 3);
            }
        }

        return true;
    }

    // 随机放置矿石的方法
    private void placeRandomOre(WorldGenLevel level, BlockPos pos, RandomSource random) {
        int totalWeight = 0;
        for (OreType ore : ORE_TYPES) {
            totalWeight += ore.weight;
        }

        int selected = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (OreType ore : ORE_TYPES) {
            cumulativeWeight += ore.weight;
            if (selected < cumulativeWeight) {
                level.setBlock(pos, ore.block, 3);
                return;
            }
        }

        // 默认放置煤矿
        level.setBlock(pos, Blocks.COAL_ORE.defaultBlockState(), 3);
    }

    // 矿石类型辅助类
    private static class OreType {
        public final BlockState block;
        public final int weight;

        public OreType(BlockState block, int weight) {
            this.block = block;
            this.weight = weight;
        }
    }
}
