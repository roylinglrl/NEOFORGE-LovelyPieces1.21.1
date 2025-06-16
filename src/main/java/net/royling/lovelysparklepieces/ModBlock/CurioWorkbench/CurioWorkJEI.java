package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModBlock.ModMenuType;
import net.royling.lovelysparklepieces.ModRecipe.ModRecipe;

import java.util.ArrayList;
import java.util.List;


@JeiPlugin
public class CurioWorkJEI implements IModPlugin {
    public static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"curio_work_plugin");
    private static IJeiRuntime jeiRuntime;
    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if(level==null)return;
        List<RecipeHolder<CurioWorkRecipe>> recipeHolders = level.getRecipeManager().getAllRecipesFor(ModRecipe.CURIO_WORK.get());
        List<CurioWorkRecipe> curioWorkRecipes = new ArrayList<>();
        for(RecipeHolder<CurioWorkRecipe> recipeRecipeHolder:recipeHolders){
            curioWorkRecipes.add(recipeRecipeHolder.value());
        }
        registration.addRecipes(CurioWorkRecipeCategory.RECIPE_TYPE,curioWorkRecipes);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CurioWorkRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURIO_WORKBENCH.get()),CurioWorkRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
                CurioWorkScreen.class,
                129,64,24,16,
                CurioWorkRecipeCategory.RECIPE_TYPE
        );
    }
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(
                CurioWorkMenu.class,
                ModMenuType.CURIO_WORK_MENU.get(),
                CurioWorkRecipeCategory.RECIPE_TYPE,
                0,9,6,36
        );
    }
}
