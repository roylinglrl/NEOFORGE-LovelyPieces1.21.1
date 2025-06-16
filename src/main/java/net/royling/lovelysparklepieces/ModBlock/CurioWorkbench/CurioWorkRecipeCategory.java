package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CurioWorkRecipeCategory implements IRecipeCategory<CurioWorkRecipe> {
    public static final RecipeType<CurioWorkRecipe> RECIPE_TYPE =
            RecipeType.create(LovelySparklePieces.MODID,"teapot",CurioWorkRecipe.class);

    public static final int WIDTH = 120;
    public static final int HEIGHT = 100;
    private static final int CENTER_X = 30; // 向左移动
    private static final int CENTER_Y = 50;
    private static final int OUTPUT_X = 90; // 向左移动
    private static final int OUTPUT_Y = 50;
    private static final int RADIUS = 24;
    private final IDrawable background;
    private final IDrawable icon;
    public CurioWorkRecipeCategory(IGuiHelper guiHelper) {
        // 创建背景 - 使用JEI内置的空白背景
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        // 使用工作台方块作为图标
        this.icon = guiHelper.createDrawableIngredient(
                VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.CURIO_WORKBENCH.get())
        );
    }

    @Override
    public RecipeType<CurioWorkRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.lsp.curio_work.title");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }
    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CurioWorkRecipe recipe, @NotNull IFocusGroup focuses) {
        // 主要输入槽位 - 中心位置
        builder.addSlot(RecipeIngredientRole.INPUT, CENTER_X - 8, CENTER_Y - 8)
                .addIngredients(recipe.getPrimaryInput());
        // 次要输入槽位 - 圆形排列
        List<Ingredient> secondaryInputs = recipe.getSecondaryInputs();
        int slotCount = secondaryInputs.size();
        for (int i = 0; i < slotCount; i++) {
            // 从顶部开始（-π/2），顺时针排列
            double angle = 2 * Math.PI * i / slotCount - Math.PI / 2;
            int x = (int) (CENTER_X + RADIUS * Math.cos(angle)) - 8;
            int y = (int) (CENTER_Y + RADIUS * Math.sin(angle)) - 8;
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredients(secondaryInputs.get(i));
        }
        // 输出槽位 - 右侧
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X - 8, OUTPUT_Y - 8)
                .addItemStack(recipe.getResult());
    }
}
