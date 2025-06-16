package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.royling.lovelysparklepieces.ModBlock.ModBlockEntities;
import net.royling.lovelysparklepieces.ModRecipe.ModRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class CurioWorkbenchBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(10) { // 9 input slots + 1 output slot
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                if (slot >= 0 && slot < 9) { // Only update if an input slot changes (slots 0-8)
                    updateRecipeOutput();
                }
            }
        }
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (slot == 9) {
                return stack;
            }
            return super.insertItem(slot, stack, simulate);
        }
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            // 允许所有槽位提取物品
            return super.extractItem(slot, amount, simulate);
        }
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 9) { // Output slot
                return false; // Cannot place anything into the output slot
            }
            return super.isItemValid(slot, stack);
        }
    };
    public CurioWorkbenchBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CURIO_WORKBENCH.get(), pos, blockState);
    }
    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        pTag.put("inventory", itemHandler.serializeNBT(pRegistries));
        super.saveAdditional(pTag, pRegistries);
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
        itemHandler.deserializeNBT(pRegistries, pTag.getCompound("inventory"));
        if (level != null && !level.isClientSide) {
            updateRecipeOutput();
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.curio_workbench");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CurioWorkMenu(pContainerId, pPlayerInventory, this);
    }

    public void consumeIngredients() {
        Level level = this.getLevel();
        if (level == null || level.isClientSide) return;

        // Ensure updateRecipeOutput has been called to get the correct recipe
        RecipeWrapper recipeWrapper = new RecipeWrapper(itemHandler);
        Optional<RecipeHolder<CurioWorkRecipe>> recipeOptional = level.getRecipeManager().getRecipeFor(ModRecipe.CURIO_WORK.get(), recipeWrapper, level);

        if (recipeOptional.isPresent()) {
            CurioWorkRecipe recipe = recipeOptional.get().value();

            // Consume primary input (slot 4)
            itemHandler.extractItem(4, 1, false);

            // Consume secondary inputs (slots 0-3, 5-8)
            List<Ingredient> remainingIngredients = new ArrayList<>(recipe.getSecondaryInputs());
            for (int i = 0; i < 9; i++) { // Iterate through all 9 input slots
                if (i == 4) continue; // Skip primary input slot

                ItemStack slotStack = itemHandler.getStackInSlot(i);
                if (slotStack.isEmpty()) continue;
                Iterator<Ingredient> it = remainingIngredients.iterator();
                while (it.hasNext()) {
                    Ingredient requiredIngredient = it.next();
                    if (requiredIngredient.test(slotStack)) {
                        itemHandler.extractItem(i, 1, false);
                        it.remove();
                        break;
                    }
                }
            }
            // Clear output slot after consumption (so player can't take infinite items)
            itemHandler.setStackInSlot(9, ItemStack.EMPTY);
            setChanged();
        }
    }

    private void updateRecipeOutput() {
        Level level = this.getLevel();
        if (level == null || level.isClientSide) return;

        RecipeWrapper recipeWrapper = new RecipeWrapper(itemHandler);

        Optional<RecipeHolder<CurioWorkRecipe>> recipeOptional = level.getRecipeManager().getRecipeFor(ModRecipe.CURIO_WORK.get(), recipeWrapper, level);

        if (recipeOptional.isPresent()) {
            ItemStack result = recipeOptional.get().value().assemble(recipeWrapper, level.registryAccess());
            itemHandler.setStackInSlot(9, result);
        } else {
            itemHandler.setStackInSlot(9, ItemStack.EMPTY);
        }
        setChanged();
    }

    @Override
    public void setRemoved() {
        // 仅在服务端且区块卸载时清空物品（避免掉落）
        if (level != null && !level.isClientSide) {
            // 清空所有槽位但不掉落物品
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        super.setRemoved();
    }
    @Override
    public void onChunkUnloaded() {
        // 区块卸载时不掉落物品，只保存NBT
        setRemoved();
    }
}
