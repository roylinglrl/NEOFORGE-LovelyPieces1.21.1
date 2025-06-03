package net.royling.lovelysparklepieces.datagenerator;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Stream;

import static net.minecraft.world.flag.FeatureFlags.REGISTRY;

public class ModBlockLoottableProvider extends BlockLootSubProvider {
    protected ModBlockLoottableProvider(HolderLookup.Provider registries) {
        super(Set.of(), REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BLAST_ABSORBER.get());
        dropSelf(ModBlocks.MOLTEN_DIRT.get());
        dropSelf(ModBlocks.MOLTEN_STONE.get());
        dropSelf(ModBlocks.CURIO_WORKBENCH.get());
        dropOther(ModBlocks.SOUL_LIGHT.get(), Items.AIR);
        dropOther(ModBlocks.FLAT_ICE.get(), Items.AIR);
        dropOther(ModBlocks.SOLID_ICE.get(), Items.AIR);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return Stream.concat(ModBlocks.BLOCKS.getEntries().stream(),ModBlocks.BLOCKS.getEntries().stream()).map(Holder::value)::iterator;
    }
    protected void dropWildCrop(Block block,Item seed){
        add(block,LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(seed))));
    }
    protected void dropWildCrops(Block block,Object... drops){
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        for(int i =0;i<drops.length;i+=2){
            if(drops[i]instanceof Item item&&drops[i+1]instanceof Integer weight){
                pool.add(LootItem.lootTableItem(item).setWeight(weight));
            }
        }
        add(block,LootTable.lootTable().withPool(pool));
    };
    protected LootTable.Builder newCreateCropDrops(Block pCropBlock, Item pGrowenCropItem,Item pSeedItem,LootItemCondition.Builder pDropGrowenCropCondition){
        Holder<Enchantment> fortune = registries.holderOrThrow(Enchantments.FORTUNE);
        LootPool.Builder cropDropPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(pGrowenCropItem).when(pDropGrowenCropCondition)
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune,0.5714286F,3))
                        .otherwise(LootItem.lootTableItem(pSeedItem)));

        LootPool.Builder seedDropPool = LootPool.lootPool()
                .add(LootItem.lootTableItem(pSeedItem).when(pDropGrowenCropCondition)
                        .apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune,0.5714286f,3)));
        return this.applyExplosionDecay(pCropBlock,LootTable.lootTable().withPool(cropDropPool).withPool(seedDropPool));
    }

    private void dropWithSilkTouchOrItem(Block targetBlock, Item dropItem) {
        this.add(targetBlock, createSingleItemTableWithSilkTouch(targetBlock, dropItem));
    }


}
