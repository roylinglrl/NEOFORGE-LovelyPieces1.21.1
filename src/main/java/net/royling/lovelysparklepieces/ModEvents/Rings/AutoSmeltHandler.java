package net.royling.lovelysparklepieces.ModEvents.Rings;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

@EventBusSubscriber(modid = LovelySparklePieces.MODID)
public class AutoSmeltHandler {
    private static final Map<Item, Item> SMELTING_CACHE = new HashMap<>();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().level().isClientSide() && !event.isCanceled()) {
            Level level = event.getPlayer().level();
            Player player = event.getPlayer();
            BlockPos pos = event.getPos();
            BlockState state = event.getState();
            if (!player.isCreative()) {
                boolean hasRing = ModCurios.hasCurio(player, ModCurios.INFERNO_RING.get());
                if (hasRing) {
                    List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level, pos, null, player, player.getMainHandItem());
                    List<ItemStack> smeltedDrops = new ArrayList<>();
                    float totalExp = 0;
                    boolean smeltedAnything = false;
                    int smeltCount = 0;
                    for (ItemStack drop : drops) {
                        Optional<RecipeHolder<SmeltingRecipe>> recipeHolder = level.getRecipeManager()
                                .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(drop), level);
                        if (recipeHolder.isPresent()) {
                            ItemStack smelted = recipeHolder.get().value().assemble(new SingleRecipeInput(drop), level.registryAccess());
                            totalExp += recipeHolder.get().value().getExperience();
                            if (!smelted.isEmpty()) {
                                smelted.setCount(drop.getCount());
                                smeltedDrops.add(smelted);
                                smeltCount += drop.getCount();
                            }
                            smeltedAnything = true;
                        } else {
                            smeltedDrops.add(drop);
                        }
                    }
                    if (smeltedAnything) {
                        event.setCanceled(true);
                        level.removeBlock(pos, false); // 移除方块
                        // 生成冶炼后的掉落物
                        for (ItemStack smeltedDrop : smeltedDrops) {
                            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, smeltedDrop);
                            itemEntity.setDefaultPickUpDelay();
                            level.addFreshEntity(itemEntity);
                        }
                        // 生成经验
                        if (totalExp > 0) {
                            ExperienceOrb expOrb = new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, (int) totalExp);
                            level.addFreshEntity(expOrb);
                        }

                        // 如果主手持有工具，则消耗耐久
                        ItemStack heldItem = player.getMainHandItem();
                        if (!heldItem.isEmpty() && heldItem.isDamageableItem()) {
                            heldItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                        }

                        // 消耗饰品耐久
                        int finalSmeltCount = smeltCount;
                        CuriosApi.getCuriosInventory(player).flatMap(inv ->
                                inv.findFirstCurio(ModCurios.INFERNO_RING.get())
                        ).ifPresent(curio ->
                                curio.stack().hurtAndBreak(finalSmeltCount, player, EquipmentSlot.MAINHAND)
                        );
                    }
                }
            }
        }
    }
}
