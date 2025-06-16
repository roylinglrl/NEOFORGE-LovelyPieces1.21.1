package net.royling.lovelysparklepieces.ModRecipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkRecipeSerializer;

import java.util.function.Supplier;

public class ModSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, LovelySparklePieces.MODID);

    public static final Supplier<RecipeSerializer<?>> CURIO_WORK =
            RECIPE_SERIALIZERS.register("curio_work", CurioWorkRecipeSerializer::new);
}
