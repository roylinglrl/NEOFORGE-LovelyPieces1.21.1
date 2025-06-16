package net.royling.lovelysparklepieces.ModBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkbench;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            LovelySparklePieces.MODID
    );
    public static final Supplier<Block> SOUL_LIGHT = BLOCKS.register(
            "soul_light",()->new Block(BlockBehaviour.Properties.of()
                    .destroyTime(0.05f).explosionResistance(0.05f).sound(SoundType.SAND)
                    .lightLevel(state->15).noCollission().noOcclusion()){
            private static final VoxelShape SOULIGHT_SHAPE = Shapes.box(
                    0.375,0.375,0.375,0.625,0.625,0.625);
                @Override
                protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                    return SOULIGHT_SHAPE;
                }
            }
    );

    public static final Supplier<Block> MOLTEN_STONE = BLOCKS.register("molten_stone",
            () -> new MoltenStone(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final Supplier<Block> MOLTEN_DIRT = BLOCKS.register("molten_dirt",
            () -> new MoltenDirt(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)));

    public static final Supplier<Block> SOLID_ICE = BLOCKS.register(
            "solid_ice",
            () -> new HardIce(Block.Properties.ofFullCopy(Blocks.ICE)
                    .friction(0.6f) // 与普通方块相同的摩擦系数
                    .hasPostProcess((bs, level, pos) -> true) // 保持冰的透明外观
            )
    );
    public static final Supplier<Block> FLAT_ICE = BLOCKS.register(
            "flat_ice",
            () -> new FlatIce(Block.Properties.ofFullCopy(Blocks.ICE)
                    .friction(0.6f) // 与普通方块相同的摩擦系数
                    .hasPostProcess((bs, level, pos) -> true) // 保持冰的透明外观
            )
    );

    public static final DeferredRegister<Item> BLOCK_ITEMS=DeferredRegister.create(
            BuiltInRegistries.ITEM,LovelySparklePieces.MODID
    );
    public static final Supplier<Item> SOUL_LIGHT_ITEM = BLOCK_ITEMS.register(
            "soul_light",()->new BlockItem(SOUL_LIGHT.get(),new Item.Properties())
    );
    public static final Supplier<Item> MOLTEN_STONE_ITEM = BLOCK_ITEMS.register(
            "molten_stone",()->new BlockItem(MOLTEN_STONE.get(),new Item.Properties())
    );
    public static final Supplier<Item> MOLTEN_DIRT_ITEM = BLOCK_ITEMS.register(
            "molten_dirt",()->new BlockItem(MOLTEN_DIRT.get(),new Item.Properties())
    );
    public static final Supplier<Block> BLAST_ABSORBER = BLOCKS.register(
            "blast_absorber",
            ()->new BlastAbsorber(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))
    );
    public static final Supplier<Item> BLAST_ABSORBER_ITEM = BLOCK_ITEMS.register(
            "blast_absorber",()->new BlockItem(BLAST_ABSORBER.get(),new Item.Properties())
    );
    public static final Supplier<Block> CURIO_WORKBENCH = BLOCKS.register(
            "curio_workbench",
            ()->new CurioWorkbench(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))
    );
    public static final Supplier<Item> CURIO_WORKBENCH_ITEM = BLOCK_ITEMS.register(
            "curio_workbench",()->new BlockItem(CURIO_WORKBENCH.get(),new Item.Properties())
    );
    public static final Supplier<Block> MOONSTONE = BLOCKS.register(
            "moonstone", MoonstoneBlock::new
    );
    public static final Supplier<Item> MOONSTONE_ITEM = BLOCK_ITEMS.register(
            "moonstone",()->new BlockItem(MOONSTONE.get(),new Item.Properties())
    );
    public static final Supplier<Block> WILDFIRE_PORTAL = BLOCKS.register("wildfire_portal",
            () -> new WildfirePortalBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Item> WILDFIRE_PORTAL_ITEM = BLOCK_ITEMS.register(
            "wildfire_portal",()->new BlockItem(WILDFIRE_PORTAL.get(),new Item.Properties()));
    public static final Supplier<Block> LAB_RUSTY_PLATE = BLOCKS.register("labblock_rusty_plate",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.5f).explosionResistance(15)));
    public static final Supplier<Item> LAB_RUSTY_PLATE_ITEM = BLOCK_ITEMS.register(
            "labblock_rusty_plate",()->new BlockItem(LAB_RUSTY_PLATE.get(),new Item.Properties()));
    public static final Supplier<Block> LAB_RUSTY_PLATE2 = BLOCKS.register("labblock_rusty_plate2",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.5f).explosionResistance(15)));
    public static final Supplier<Item> LAB_RUSTY_PLATE_ITEM2 = BLOCK_ITEMS.register(
            "labblock_rusty_plate2",()->new BlockItem(LAB_RUSTY_PLATE2.get(),new Item.Properties()));
    public static final Supplier<Block> LAB_BAW_FLOOR = BLOCKS.register("labblock_baw_floor",
            () -> new Block(BlockBehaviour.Properties.of().strength(3.5f).explosionResistance(15)));
    public static final Supplier<Item> LAB_BAW_FLOOR_ITEM = BLOCK_ITEMS.register(
            "labblock_baw_floor",()->new BlockItem(LAB_BAW_FLOOR.get(),new Item.Properties()));
}
