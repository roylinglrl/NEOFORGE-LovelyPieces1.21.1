package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.List;

public class CurioWorkRecipeSerializer implements RecipeSerializer<CurioWorkRecipe> {
    @Override
    public MapCodec<CurioWorkRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CurioWorkRecipe> streamCodec() {
        return STREAM_CODEC;
    }
    public static final MapCodec<CurioWorkRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("primary").forGetter(CurioWorkRecipe::getPrimaryInput),
            Ingredient.CODEC.listOf().fieldOf("secondary").forGetter(CurioWorkRecipe::getSecondaryInputs),
            ItemStack.CODEC.fieldOf("result").forGetter(CurioWorkRecipe::getResult)
    ).apply(instance, CurioWorkRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CurioWorkRecipe> STREAM_CODEC =
            StreamCodec.of(CurioWorkRecipeSerializer::toNetwork, CurioWorkRecipeSerializer::fromNetwork);

    private static void toNetwork(RegistryFriendlyByteBuf buf, CurioWorkRecipe recipe) {
        // 写入主要输入
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getPrimaryInput());

        // 写入次要输入数量及内容
        List<Ingredient> secondaries = recipe.getSecondaryInputs();
        buf.writeVarInt(secondaries.size());
        for (Ingredient ingredient : secondaries) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
        }
        ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
    }
    private static CurioWorkRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
        // 读取主要输入
        Ingredient primary = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        // 读取次要输入
        int secondaryCount = buf.readVarInt();
        Ingredient[] secondaries = new Ingredient[secondaryCount];
        for (int i = 0; i < secondaryCount; i++) {
            secondaries[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        }
        // 读取输出
        ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
        return new CurioWorkRecipe(primary, List.of(secondaries), output);
    }
}
