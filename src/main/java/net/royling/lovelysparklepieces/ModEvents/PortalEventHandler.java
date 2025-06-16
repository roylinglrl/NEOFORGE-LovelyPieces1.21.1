package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.GAME)
public class PortalEventHandler {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos clickedPos = event.getPos();
        BlockState clickedState = level.getBlockState(clickedPos);
        Player player = event.getEntity();
        if(!player.getMainHandItem().isEmpty())return;
        if(!player.getOffhandItem().isEmpty())return;
        // 仅处理岩浆块右键
        if (clickedState.getBlock() != Blocks.MAGMA_BLOCK || level.isClientSide())
            return;

        // 检查结构完整性
        BlockPos basePos = findBasePosition(level, clickedPos);
        if (basePos == null) return;

        if (checkGoldFrame(level, basePos)) {
            createPortal(level, basePos);
            event.setCanceled(true); // 阻止岩浆被取走
        }
    }
    private static BlockPos findBasePosition(Level level, BlockPos clicked) {
        int y = clicked.getY();
        for (int dx = 0; dx >= -1; dx--) {
            for (int dz = 0; dz >= -1; dz--) {
                BlockPos candidate = new BlockPos(clicked.getX() + dx, y, clicked.getZ() + dz);
                if (isValidMagmaBase(level, candidate)) {
                    return candidate;
                }
            }
        }
        return null;
    }
    private static boolean isValidMagmaBase(Level level, BlockPos base) {
        for (int dx = 0; dx < 2; dx++) {
            for (int dz = 0; dz < 2; dz++) {
                BlockPos pos = base.offset(dx, 0, dz);
                if (level.getBlockState(pos).getBlock() != Blocks.MAGMA_BLOCK) {
                    return false;
                }
            }
        }
        return true;
    }
    // 检查12个金块框架
    private static boolean checkGoldFrame(Level level, BlockPos base) {
        int x = base.getX();
        int y = base.getY();
        int z = base.getZ();

        // 检查4个边的金块（避开岩浆区域）
        for (int i = -1; i <= 2; i++) {
            // 顶部行 (z-1)
            if (!isGold(level, x + i, y, z - 1)) return false;
            // 底部行 (z+2)
            if (!isGold(level, x + i, y, z + 2)) return false;
            // 左侧列 (x-1)
            if (i >= 0 && i <= 1 && !isGold(level, x - 1, y, z + i)) return false;
            // 右侧列 (x+2)
            if (i >= 0 && i <= 1 && !isGold(level, x + 2, y, z + i)) return false;
        }
        return true;
    }
    private static boolean isGold(Level level, int x, int y, int z) {
        return level.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.GOLD_BLOCK;
    }
    private static void createPortal(Level level, BlockPos base) {
        // 替换岩浆块为传送门方块
        for (int dx = 0; dx < 2; dx++) {
            for (int dz = 0; dz < 2; dz++) {
                BlockPos pos = base.offset(dx, 0, dz);
                level.setBlock(pos, ModBlocks.WILDFIRE_PORTAL.get().defaultBlockState(), 3);
            }
        }
        // 播放音效
        level.playSound(null, base, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}
