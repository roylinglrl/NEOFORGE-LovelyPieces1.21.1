package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CurioWorkbench extends Block implements EntityBlock{
    public CurioWorkbench(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CurioWorkbenchBlockEntity(blockPos,blockState);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CurioWorkbenchBlockEntity curioWorkbenchBlockEntity) {
                pPlayer.openMenu(curioWorkbenchBlockEntity);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CurioWorkbenchBlockEntity curioWorkbenchBlockEntity) {
                // 创建临时容器并掉落所有物品
                SimpleContainer inventory = new SimpleContainer(curioWorkbenchBlockEntity.getItemHandler().getSlots());
                for (int i = 0; i < curioWorkbenchBlockEntity.getItemHandler().getSlots(); i++) {
                    inventory.setItem(i, curioWorkbenchBlockEntity.getItemHandler().getStackInSlot(i));
                }
                Containers.dropContents(pLevel, pPos, inventory);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

}
