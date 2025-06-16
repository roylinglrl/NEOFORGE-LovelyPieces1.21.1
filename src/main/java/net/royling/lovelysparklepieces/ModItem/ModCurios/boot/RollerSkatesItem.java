package net.royling.lovelysparklepieces.ModItem.ModCurios.boot;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class RollerSkatesItem extends UniversalCurio {
    private static final int RADIUS = 3;
    private final Queue<BlockPos> workQueue = new ArrayDeque<>();

    public RollerSkatesItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player && !player.level().isClientSide) {
            Level level = player.level();
            BlockPos below = player.blockPosition().below();
            if (workQueue.isEmpty()) {
                for (int x = -RADIUS; x <= RADIUS; x++) {
                    for (int z = -RADIUS; z <= RADIUS; z++) {
                        workQueue.offer(below.offset(x, 0, z));
                    }
                }
            }
            for (int i = 0; i < 10 && !workQueue.isEmpty(); i++) {
                BlockPos pos = workQueue.poll();
                if (level.getBlockState(pos).is(Blocks.ICE)) {
                    level.setBlock(pos, ModBlocks.SOLID_ICE.get().defaultBlockState(), 3);
                    level.scheduleTick(pos, ModBlocks.SOLID_ICE.get(), 100); // 5秒恢复
                }
            }
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level1"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.icewalk.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.icewalk.des1").withColor(ColorUtil.getRainbow()));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
