package net.royling.lovelysparklepieces.datagenerator;

import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurioWorkRecipeBuilder {
    private final RecipeSerializer<?> serializer;
    private final ItemStack result;
    private final Ingredient primary;
    private final NonNullList<Ingredient> secondary;

    // 私有构造方法
    private CurioWorkRecipeBuilder(RecipeSerializer<?> serializer, Ingredient primary, List<Ingredient> secondary, ItemStack result) {
        this.serializer = serializer;
        this.primary = primary;
        this.secondary = NonNullList.of(Ingredient.EMPTY, secondary.toArray(new Ingredient[0]));
        this.result = result;
    }

    // 静态工厂方法：使用Ingredient对象
    public static CurioWorkRecipeBuilder create(
            RecipeSerializer<?> serializer,
            Ingredient primary,
            List<Ingredient> secondary,
            Item result,
            int count
    ) {
        return new CurioWorkRecipeBuilder(serializer, primary, secondary, new ItemStack(result,count));
    }

    // 静态工厂方法：使用物品
    public static CurioWorkRecipeBuilder create(
            RecipeSerializer<?> serializer,
            Item primary,
            List<Item> secondary,
            Item result,
            int count
    ) {
        return new CurioWorkRecipeBuilder(
                serializer,
                Ingredient.of(primary),
                secondary.stream().map(Ingredient::of).toList(),
                new ItemStack(result, count)
        );
    }

    // 静态工厂方法：使用物品数组
    public static CurioWorkRecipeBuilder create(
            RecipeSerializer<?> serializer,
            Item primary,
            Item result,
            int count,
            Item... secondaryItems
    ) {
        List<Ingredient> secondary = new ArrayList<>();
        for (Item item : secondaryItems) {
            secondary.add(Ingredient.of(item));
        }
        return new CurioWorkRecipeBuilder(
                serializer,
                Ingredient.of(primary),
                secondary,
                new ItemStack(result, count)
        );
    }

    // 提交配方
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(
                id,
                new CurioWorkRecipe(primary, secondary, result),
                null
        );
    }
    // 提交配方（自动生成ID）
    public void save(RecipeOutput output, String recipeName) {
        save(output, ResourceLocation.fromNamespaceAndPath("lovely_sparkle_pieces", "curio_work/" + recipeName));
    }
}
