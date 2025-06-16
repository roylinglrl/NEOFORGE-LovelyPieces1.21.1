package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.royling.lovelysparklepieces.ModRecipe.ModRecipe;
import net.royling.lovelysparklepieces.ModRecipe.ModSerializers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurioWorkRecipe implements Recipe<RecipeWrapper> {
    private final Ingredient primaryInput;
    private final NonNullList<Ingredient> secondaryInputs;
    private final ItemStack result;
    public CurioWorkRecipe(Ingredient primaryInput,List<Ingredient> secondaryInputs,ItemStack result){
        this.primaryInput = primaryInput;
        this.secondaryInputs = NonNullList.of(Ingredient.EMPTY, secondaryInputs.toArray(new Ingredient[0]));
        this.result = result;
    }


    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> grid = NonNullList.withSize(9, Ingredient.EMPTY);
        grid.set(4, primaryInput); // 中心位置
        int pos = 0;
        for (Ingredient ing : secondaryInputs) {
            while (pos < 9) {
                if (pos != 4 && grid.get(pos).isEmpty()) {
                    grid.set(pos, ing);
                    break;
                }
                pos++;
            }
        }
        return grid;
    }

    @Override
    public boolean matches(RecipeWrapper container, Level level) {
        if (!primaryInput.test(container.getItem(4))) {
            return false;
        }

        List<ItemStack> secondaryItems = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (i == 4) continue;
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) secondaryItems.add(stack);
        }

        if (secondaryItems.size() < secondaryInputs.size()) {
            return false;
        }

        List<Ingredient> remainingIngredients = new ArrayList<>(secondaryInputs);
        for (ItemStack item : secondaryItems) {
            boolean found = false;
            Iterator<Ingredient> it = remainingIngredients.iterator();
            while (it.hasNext()) {
                if (it.next().test(item)) {
                    it.remove();
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper, HolderLookup.Provider provider) {
        return result.copy();
    }
    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }
    public ItemStack getResult(){
        return result.copy();
    }
    public NonNullList<Ingredient> getSecondaryInputs() {
        return secondaryInputs;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModSerializers.CURIO_WORK.get();
    }
    @Override
    public RecipeType<?> getType() {
        return ModRecipe.CURIO_WORK.get();
    }

    public Ingredient getPrimaryInput(){
        return primaryInput;
    }
    public Ingredient getInput(int index) {
        if (index < 0 || index >= secondaryInputs.size()) {
            return Ingredient.EMPTY;
        }
        return secondaryInputs.get(index);
    }
}
