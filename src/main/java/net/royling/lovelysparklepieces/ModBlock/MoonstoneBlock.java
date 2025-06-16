package net.royling.lovelysparklepieces.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;

public class MoonstoneBlock extends Block {
    public MoonstoneBlock() {
        super(Properties.ofFullCopy(Blocks.STONE).strength(2.0f,15.0f));
    }
}
