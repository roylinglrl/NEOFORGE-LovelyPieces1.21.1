package net.royling.lovelysparklepieces.ModRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkRecipe;

import java.util.List;
import java.util.function.Supplier;

public class ModRecipe {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES=
            DeferredRegister.create(Registries.RECIPE_TYPE, LovelySparklePieces.MODID);

    public static final Supplier<RecipeType<CurioWorkRecipe>> CURIO_WORK=
            RECIPE_TYPES.register("curio_work",()->RecipeType.register("curio_work"));

    public static List<RecipeHolder<CurioWorkRecipe>> getRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(CURIO_WORK.get());
    }
}
