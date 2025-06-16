package net.royling.lovelysparklepieces.ModBlock;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkbenchBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, LovelySparklePieces.MODID);

    public static final Supplier<BlockEntityType<CurioWorkbenchBlockEntity>> CURIO_WORKBENCH =
            BLOCK_ENTITY_TYPES.register("teapot",()->BlockEntityType.Builder.of(
                    CurioWorkbenchBlockEntity ::new,ModBlocks.CURIO_WORKBENCH.get()
            ).build(null));

}
