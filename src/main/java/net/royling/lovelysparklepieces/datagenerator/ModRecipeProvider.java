package net.royling.lovelysparklepieces.datagenerator;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import net.royling.lovelysparklepieces.ModRecipe.ModRecipe;
import net.royling.lovelysparklepieces.ModRecipe.ModSerializers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.royling.lovelysparklepieces.LovelySparklePieces.MODID;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        RecipeSerializer<?> serializer = ModSerializers.CURIO_WORK.get();
        CurioWorkRecipeBuilder.create(
            serializer,
                Items.APPLE,
                List.of(Items.APPLE,Items.APPLE,Items.APPLE),
                Items.CARROT,1
        ).save(output,"apple_to_carrot");
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.SPARKLE_SHARD.get()), // 使用get()获取物品实例
                        RecipeCategory.MISC,
                        ModItems.MOON_INGOT.get(),
                        0.7f, // 经验值
                        200)   // 烧炼时间(ticks)
                .unlockedBy("has_shard", has(ModItems.SPARKLE_SHARD.get()))
                .save(output, MODID + ":moonlight_ingot_smelting");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOONSTONE_ITEM.get())
                .pattern("SS")
                .pattern("SS")
                .define('S', ModItems.SPARKLE_SHARD.get())
                .unlockedBy("has_shard", has(ModItems.SPARKLE_SHARD.get()))
                .save(output, MODID + ":moonstone_block_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.MULTI_PURPOSE_TOOL.get())
                .pattern(" I ")
                .pattern(" M ")
                .pattern(" S ")
                .define('I', Items.IRON_INGOT)          // 铁锭（基础材料）
                .define('M', ModItems.MOON_INGOT.get())  // 月石锭（核心材料）
                .define('S', Items.STICK)               // 木棍（手柄）
                .unlockedBy("has_moon_ingot", has(ModItems.MOON_INGOT.get()))
                .save(output, MODID + ":multi_tool_crafting");
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CURIO_WORKBENCH.get())
                .pattern(" W ")
                .pattern("WMW")
                .pattern(" W ")
                .define('W', Blocks.OAK_PLANKS)           // 橡木木板（基础结构）
                .define('M', ModItems.MULTI_PURPOSE_TOOL.get())   // 多功能工具（核心部件）
                .unlockedBy("has_multi_tool", has(ModItems.MULTI_PURPOSE_TOOL.get()))
                .save(output, MODID + ":curio_workbench_crafting");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.POLYMERIZATION.get())
                .pattern("III")
                .pattern("IEI")
                .pattern("III")
                .define('I', Items.IRON_INGOT)           // 铁锭（外壳）
                .define('E', Items.ENDER_PEARL)          // 末影珍珠（核心能量）
                .unlockedBy("has_ender_pearl", has(Items.ENDER_PEARL))
                .save(output); // Simply save with the default name
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SUPERPOLYMERIZATION.get())
                .pattern("DDD")
                .pattern("DCD")
                .pattern("DDD")
                .define('D', Items.DIAMOND)              // 钻石（更强的外壳）
                .define('C', ModItems.POLYMERIZATION.get())      // 融合（核心升级部件）
                .unlockedBy("has_fusion", has(ModItems.POLYMERIZATION.get()))
                .save(output);


        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.SPARKLE_SHARD.get()),
                        RecipeCategory.MISC,
                        ModItems.MOON_INGOT.get(),
                        0.7f,
                        100)
                .unlockedBy("has_shard", has(ModItems.SPARKLE_SHARD.get()))
                .save(output, MODID + "moonlight_ingot_blasting");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.APPLE,
                List.of(Items.DIAMOND,Items.DIAMOND,Items.DIAMOND,Items.DIAMOND,Items.DIAMOND,Items.DIAMOND,Items.DIAMOND,Items.DIAMOND),
                Items.CARROT,1
        ).save(output,"diamond_to_carrot");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(),
                List.of(Items.IRON_INGOT,Items.IRON_INGOT,Items.IRON_INGOT),
                ModCurios.MAGNETIC_RING.get(),1
        ).save(output,"magnetic_ring");
        CurioWorkRecipeBuilder.create(
                serializer, // 假设 serializer 是您的自定义配方序列化器实例
                ModCurios.MAGNETIC_RING.get(), // 主要输入：普通磁力戒指
                List.of( // 次要输入（最多8个）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.REDSTONE, // 红石
                        Items.REDSTONE, // 红石
                        Items.GOLD_INGOT // 金锭
                ),
                ModCurios.SUPER_MAGNETIC_RING.get(), // 输出：强力磁力戒指
                1 // 输出数量
        ).save(output, "advanced_magnetic_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ENDER_EYE, // 主要输入：末影之眼
                List.of( // 次要输入（7个材料）
                        Items.ENDER_PEARL,      // 末影珍珠
                        Items.ENDER_PEARL,      // 末影珍珠
                        Items.ENDER_PEARL,      // 末影珍珠
                        Items.OBSIDIAN,         // 黑曜石
                        Items.OBSIDIAN,         // 黑曜石
                        Items.OBSIDIAN,         // 黑曜石
                        Items.DIAMOND,          // 钻石
                        ModItems.MOON_INGOT.get() // 月光锭

                ),
                ModCurios.ENDER_RING.get(), // 输出：末影戒指
                1
        ).save(output,   "ender_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(),
                List.of( // 次要输入（7个材料）
                        Items.DIAMOND,
                        Items.DIAMOND,
                        Items.FLINT,
                        Items.FLINT,
                        Items.REDSTONE,
                        ModItems.MOON_INGOT.get(),
                        Items.GOLD_INGOT
                ),
                ModCurios.CRIT_RING.get(), // 输出：暴击戒指
                1
        ).save(output,  "critical_ring");

        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(),
                List.of( // 次要输入（8个材料）
                        Items.REDSTONE,         // 红石
                        Items.REDSTONE,         // 红石
                        Items.REDSTONE,         // 红石
                        Items.GOLD_INGOT,         // 红石
                        Items.LAPIS_LAZULI,     // 青金石
                        Items.LAPIS_LAZULI,     // 青金石
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.QUARTZ     // 下界石英
                ),
                ModCurios.MEMORY_RING.get(), // 输出：内存戒指
                1
        ).save(output, "memory_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月光锭（与夜晚相关）
                List.of( // 次要输入（8个材料）
                        Items.PHANTOM_MEMBRANE, // 幻翼膜（夜晚生物）
                        Items.PHANTOM_MEMBRANE,
                        Items.GLOW_INK_SAC,     // 荧光墨囊（夜晚发光）
                        Items.GLOW_INK_SAC,
                        Items.CLOCK,            // 时钟（代表昼夜循环）
                        Items.GOLD_INGOT,       // 金锭（贵重材料）
                        Items.DIAMOND,
                        Items.SOUL_TORCH        // 灵魂火把（夜晚光源）
                ),
                ModCurios.NIGHT_OWL_RING.get(), // 输出：夜莺戒指
                1
        ).save(output, "nightingale_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_PICKAXE, // 主要输入：铁镐（代表挖掘工具）
                List.of( // 次要输入（7个材料）
                        Items.REDSTONE,         // 红石（代表速度）
                        Items.REDSTONE,
                        Items.REDSTONE,
                        Items.COAL,             // 煤炭（代表能量）
                        Items.COAL,
                        Items.IRON_INGOT,       // 铁锭（加固材料）
                        ModItems.MOON_INGOT.get() // 月光锭（特殊强化）
                ),
                ModCurios.CRUSH_STONE_RING.get(), // 输出：碎石者戒指
                1
        ).save(output,  "breaker_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLAZE_ROD, // 主要输入：烈焰棒（代表火焰）
                List.of( // 次要输入（8个材料）
                        Items.MAGMA_CREAM,       // 岩浆膏（高温材料）
                        Items.MAGMA_CREAM,
                        Items.IRON_INGOT,        // 铁锭（基础材料）
                        Items.IRON_INGOT,
                        Items.COAL,              // 煤炭（燃料）
                        Items.COAL,
                        Items.FIRE_CHARGE,       // 火焰弹（火焰能量）
                        ModItems.MOON_INGOT.get()  // 月光锭（特殊强化）
                ),
                ModCurios.INFERNO_RING.get(), // 输出：业炎戒指
                1
        ).save(output, "inferno_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.WITHER_SKELETON_SKULL, // 主要输入：凋零骷髅头颅（代表目标生物）
                List.of( // 次要输入（8个材料）
                        Items.COAL,              // 煤炭（代表掉落物）
                        Items.COAL,
                        Items.SOUL_SAND,         // 灵魂沙（下界环境）
                        Items.SOUL_SAND,
                        Items.BONE,              // 骨头（骷髅关联）
                        Items.BONE,
                        Items.GOLD_INGOT,        // 金锭（平衡材料）
                        ModItems.MOON_INGOT.get()  // 月光锭（特殊能量）
                ),
                ModCurios.ECO_RING.get(), // 输出：生态戒指
                1
        ).save(output,  "ecology_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT, // 主要输入：金锭（猪灵喜爱的材料）
                List.of( // 次要输入（8个材料）
                        Items.GOLD_NUGGET,      // 金粒
                        Items.GOLD_NUGGET,
                        Items.GOLD_NUGGET,
                        Items.GOLD_NUGGET,
                        Items.GOLD_NUGGET,
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.CRYING_OBSIDIAN,  // 哭泣的黑曜石（下界材料）
                        Items.PORKCHOP          // 生猪排（猪灵食物）
                ),
                ModCurios.PUREGOLD_RING.get(), // 输出：纯金戒指
                1
        ).save(output, "golden_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.PUREGOLD_RING.get(), // 主要输入：纯金戒指（基础功能）
                List.of( // 次要输入（8个材料）
                        Items.GOLD_BLOCK,        // 金块（强化金质特性）
                        Items.BLACKSTONE,        // 黑石（下界堡垒材料）
                        Items.BLACKSTONE,
                        Items.GILDED_BLACKSTONE, // 镶金黑石（堡垒特有方块）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.COOKED_PORKCHOP,   // 熟猪排（猪灵蛮兵关联）
                        Items.CRYING_OBSIDIAN    // 哭泣的黑曜石（下界材料）
                ),
                ModCurios.BASTION_RING.get(), // 输出：堡垒戒指
                1
        ).save(output, "bastion_ring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT,
                List.of(
                        Items.IRON_INGOT,
                        Items.IRON_INGOT,
                        Items.REDSTONE,
                        Items.REDSTONE,
                        Items.LAPIS_LAZULI,
                        Items.GLOWSTONE_DUST,
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.DIAMOND
                ),
                ModCurios.ROFAP.get(), // 输出：宠爱庇佑戒指
                1
        ).save(output, "blessed_ring_economy");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ENDER_EYE,
                List.of(
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.REDSTONE,
                        Items.REDSTONE,
                        Items.REDSTONE,
                        Items.CLOCK,
                        Items.AMETHYST_SHARD,
                        Items.GLASS_PANE,
                        Items.LAPIS_LAZULI
                ),
                ModCurios.FPS_EYE.get(),
                1
        ).save(output, "fps_eye");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GILDED_BLACKSTONE, // 主要输入：镶金黑石（代表黑石核心）
                List.of( // 次要输入（8个材料）
                        Items.BLACKSTONE,       // 黑石（基础材料）
                        Items.BLACKSTONE,
                        Items.MAGMA_CREAM,     // 岩浆膏（防火材料）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.OBSIDIAN,        // 黑曜石（耐热材料）
                        Items.OBSIDIAN,
                        Items.SOUL_SAND,       // 灵魂沙（下界环境）
                        Items.FIRE_CHARGE       // 火焰弹（控制火焰）
                ),
                ModCurios.BLACKSTONE_HEART.get(), // 输出：黑石之心戒指
                1
        ).save(output, "blackstone_heart");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.SPYGLASS, // 主要输入：望远镜（单筒基础）
                List.of( // 次要输入（7个材料）
                        Items.GLOWSTONE_DUST,   // 萤石粉（光源增强）
                        Items.GLOWSTONE_DUST,
                        Items.REDSTONE,         // 红石（电路）
                        Items.REDSTONE,
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.IRON_NUGGET,      // 铁粒（小型结构）
                        Items.COPPER_INGOT,     // 铜锭（电子元件）
                        Items.BLACK_DYE         // 黑色染料（遮光涂层）
                ),
                ModCurios.NIGHT_VISION.get(), // 输出：PVS14夜视仪
                1
        ).save(output, "pvs14_night_vision");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.NIGHT_VISION.get(), // 主要输入：PVS14夜视仪（基础）
                List.of( // 次要输入（8个升级材料）
                        Items.GLASS_PANE,        // 玻璃板（广角镜头）
                        Items.GLASS_PANE,
                        Items.COPPER_BLOCK,      // 铜块（增强电子元件）
                        Items.AMETHYST_SHARD,    // 紫水晶碎片（光学镀膜）
                        Items.REDSTONE_BLOCK,     // 红石块（增强电路）
                        Items.GOLD_INGOT,         // 金锭（信号增强）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.QUARTZ       // 下界石英（精密光学元件）
                ),
                ModCurios.DOUBLE_NIGHT_VISION.get(), // 输出：ATNPS15夜视仪
                1
        ).save(output, "atnps15_night_vision");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.DOUBLE_NIGHT_VISION.get(), // 主要输入：ATNPS15夜视仪（基础）
                List.of( // 次要输入（8个高级材料）
                        Items.SPYGLASS,          // 望远镜（额外镜筒）
                        Items.SPYGLASS,          // 望远镜（额外镜筒）
                        Items.GLASS_PANE,          // 望远镜（额外镜筒）
                        Items.DIAMOND,       // 下界之星（终极能量源）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.GLOWSTONE // 萤石块（强力光源）
                ),
                ModCurios.QUARTER_NIGHT_VISION.get(), // 输出：GPNVG18四筒夜视仪
                1
        ).save(output, "gp_nvg18_night_vision");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.CONDUIT, // 主要输入：潮涌核心（水下呼吸能力）
                List.of( // 次要输入（8个水下主题材料）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.PRISMARINE_SHARD,   // 海晶碎片（海洋材料）
                        Items.PRISMARINE_SHARD,
                        Items.PRISMARINE_SHARD,
                        Items.PRISMARINE_SHARD,   // 共4个海晶碎片
                        Items.GLOW_INK_SAC,       // 荧光墨囊（发光水母）
                        Items.GLOW_INK_SAC,
                        Items.NAUTILUS_SHELL      // 鹦鹉螺壳（水下呼吸）
                ),
                ModCurios.JELLYFISH_HELMET.get(), // 输出：水母帽帽
                1
        ).save(output, "jellyfish_hat");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLACK_WOOL, // 主要输入：黑色羊毛（帽子主体）
                List.of( // 次要输入（8个材料）
                        Items.BLACK_WOOL,       // 黑色羊毛（增加厚度）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.LEATHER,          // 皮革（帽檐）
                        Items.LEATHER,
                        Items.GOLD_INGOT,       // 金锭（装饰）
                        Items.GOLD_INGOT,
                        Items.BLACK_DYE,        // 黑色染料（加深颜色）
                        Items.FEATHER           // 羽毛（装饰）
                ),
                ModCurios.CAPITALIST_HEAT.get(), // 输出：资本家帽子
                1
        ).save(output, "capitalist_heat");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.PRISMARINE_SHARD, // 主要输入：海晶碎片（海洋材料）
                List.of( // 次要输入（8个海洋主题材料）
                        Items.TURTLE_SCUTE,             // 鳞甲（海龟鳞片）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.COD,               // 鳕鱼（代表鱼尾）
                        Items.SALMON,            // 鲑鱼（代表鱼尾）
                        Items.HEART_OF_THE_SEA,  // 海洋之心（海洋力量）
                        Items.KELP,              // 海带（海洋植物）
                        Items.NAUTILUS_SHELL,    // 鹦鹉螺壳（水下推进）
                        Items.PHANTOM_MEMBRANE   // 幻翼膜（流线型设计）
                ),
                ModCurios.MERMAID_TAIL.get(), // 输出：鲛人的尾巴
                1
        ).save(output, "mermaid_tail");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.JELLYFISH_HELMET.get(), // 主要输入：水母帽帽（提供水下呼吸）
                List.of( // 次要输入（8个海神主题材料）
                        ModCurios.MERMAID_TAIL.get(), // 鲛人的尾巴（游泳加速）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.TRIDENT,                // 三叉戟（海神武器）
                        Items.HEART_OF_THE_SEA,       // 海洋之心（海洋核心）
                        Items.PRISMARINE_CRYSTALS,    // 海晶砂粒（海洋能量）
                        Items.GOLD_BLOCK,             // 金块（神圣材料）
                        Items.DIAMOND,
                        Items.DIAMOND
                ),
                ModCurios.POSEIDON_RESPIRATOR.get(), // 输出：海神呼吸器
                1
        ).save(output, "poseidon_respirator");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.SPYGLASS, // 主要输入：望远镜（瞄准基础）
                List.of( // 次要输入（8个射击主题材料）
                        Items.ARROW,             // 箭（弹射物代表）
                        Items.ARROW,
                        Items.STRING,            // 线（弓弦）
                        Items.STRING,
                        Items.FLINT,             // 燧石（发射机制）
                        Items.FLINT,
                        Items.GOLD_NUGGET,       // 金粒（精密部件）
                        ModItems.MOON_INGOT.get() // 月光锭
                ),
                ModCurios.MARKSMAN_GOGGLES.get(), // 输出：神射手护目镜
                1
        ).save(output, "sniper_goggles");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLACK_WOOL, // 主要输入：黑色羊毛（帽子基础）
                List.of( // 次要输入（8个魔法主题材料）
                        Items.BLAZE_POWDER,     // 烈焰粉（火魔法）
                        Items.GHAST_TEAR,       // 恶魂之泪（治疗魔法）
                        Items.PHANTOM_MEMBRANE, // 幻翼膜（飞行魔法）
                        Items.REDSTONE,         // 红石（能量传导）
                        Items.GLOWSTONE_DUST,   // 萤石粉（光明魔法）
                        Items.SPIDER_EYE,       // 蜘蛛眼（毒魔法）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.AMETHYST_SHARD    // 紫水晶碎片（奥术能量）
                ),
                ModCurios.WITCH_HAT.get(), // 输出：魔女的帽子
                1
        ).save(output, "witch_hat");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.YELLOW_WOOL, // 主要输入：黄色羊毛（头巾基础）
                List.of( // 次要输入（8个雷电主题材料）
                        Items.COPPER_INGOT,      // 铜锭（导电材料）
                        Items.COPPER_INGOT,
                        Items.REDSTONE,          // 红石（能量传导）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.GLOWSTONE_DUST,    // 萤石粉（闪电光亮）
                        Items.IRON_INGOT,        // 铁锭（引雷特性）
                        Items.GUNPOWDER,         // 火药（雷声象征）
                        Items.LIGHTNING_ROD      // 避雷针（雷电控制）
                ),
                ModCurios.YELLOW_HEADSCARF.get(), // 输出：黄色头巾
                1
        ).save(output, "yellow_headband");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_HELMET, // 主要输入：碗（盛放食物）
                List.of( // 次要输入（8个婴儿/食物主题材料）
                        Items.MILK_BUCKET,      // 牛奶桶（婴儿食品）
                        Items.APPLE,            // 苹果（健康食物）
                        Items.CARROT,           // 胡萝卜（营养食物）
                        Items.POTATO,           // 马铃薯（基础食物）
                        Items.REDSTONE,         // 红石（自动化）
                        Items.GOLD_NUGGET,      // 金粒（精密喂食机制）
                        Items.STRING,           // 线（连接装置）
                        ModItems.MOON_INGOT.get()// 月光锭

                ),
                ModCurios.BABY_FEEDER.get(), // 输出：宝宝辅食器
                1
        ).save(output, "baby_feeder");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT, // 主要输入：金锭（鎏金材质）
                List.of( // 次要输入（8个时间/效果主题材料）
                        Items.CLOCK,           // 时钟（时间延长）
                        Items.REDSTONE,        // 红石（效果增强）
                        Items.REDSTONE,
                        Items.PHANTOM_MEMBRANE, // 幻翼膜（夜间效果）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.SUGAR,           // 糖（加速代谢）
                        Items.GLOWSTONE_DUST,  // 萤石粉（光亮持久）
                        Items.DRIED_KELP       // 干海带（烟雾象征）
                ),
                ModCurios.GILDED_PIPE.get(), // 输出：鎏金烟斗
                1
        ).save(output, "golden_pipe");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ANVIL, // 主要输入：铁砧（代表重量）
                List.of( // 次要输入（8个重量/坠落主题材料）
                        Items.OBSIDIAN,         // 黑曜石（沉重材料）
                        Items.OBSIDIAN,
                        Items.IRON_BLOCK,       // 铁块（高密度）
                        Items.STONE,            // 石头（基础重量）
                        Items.STONE,
                        Items.GRAVEL,           // 沙砾（坠落效果）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.COBBLESTONE       // 岩浆膏（增加坠落速度）
                ),
                ModCurios.HEAVY_BIGROCK.get(), // 输出：沉重大石头
                1
        ).save(output, "heavy_bigrock");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.CLOCK, // 主要输入：时钟（时间显示基础）
                List.of( // 次要输入（8个时间/精密主题材料）
                        Items.GOLD_INGOT,       // 金锭（精密部件）
                        Items.COPPER_INGOT,     // 铜锭（表壳材料）
                        Items.REDSTONE,         // 红石（机械装置）
                        Items.GLASS_PANE,       // 玻璃板（表盖）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.IRON_NUGGET       // 铁粒（指针）
                ),
                ModCurios.POCKET_WATCH.get(), // 输出：怀表
                1
        ).save(output, "pocket_watch");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.CLOCK, // 主要输入：时钟（时间测量基础）
                List.of( // 次要输入（8个速度/测量主题材料）
                        Items.COMPASS,          // 指南针（方向指示）
                        Items.REDSTONE,         // 红石（电子测量）
                        Items.IRON_INGOT,       // 铁锭（机械结构）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.COPPER_INGOT     // 铜锭（导电材料）
                ),
                ModCurios.SPEEDOMETER.get(), // 输出：测速仪
                1
        ).save(output, "speedometer");
        // 在 buildRecipes 方法中添加定位仪配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.COMPASS, // 主要输入：指南针（定位基础）
                List.of( // 次要输入（8个定位/导航主题材料）
                        Items.PAPER,            // 纸（坐标记录）
                        Items.REDSTONE,         // 红石（信号传输）
                        Items.COPPER_INGOT,     // 铜锭（导电材料）
                        Items.GLOWSTONE_DUST,   // 萤石粉（位置标记）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.IRON_INGOT      // 铁锭（机械结构）
                ),
                ModCurios.POSITION_TRACKER.get(), // 输出：定位仪
                1
        ).save(output, "position_tracker");
        // 在 buildRecipes 方法中添加GPS配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.POSITION_TRACKER.get(), // 主要输入：定位仪（位置显示基础）
                List.of( // 次要输入（8个高级导航主题材料）
                        ModCurios.POCKET_WATCH.get(),  // 怀表（时间显示功能）
                        ModCurios.SPEEDOMETER.get(),   // 测速仪（速度显示功能）
                        Items.REDSTONE_BLOCK,          // 红石块（增强信号处理）
                        Items.COPPER_BLOCK,            // 铜块（增强导电性能）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        ModItems.POLYMERIZATION.get()           // 下界石英（精密计时）

                ),
                ModCurios.GPS.get(), // 输出：GPS设备
                1
        ).save(output, "gps_device");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.GPS.get(), // 主要输入：GPS设备（基础功能）
                List.of( // 次要输入（8个信息处理主题材料）
                        Items.GOLD_INGOT,                 // 书（数据存储）
                        Items.GOLD_INGOT,
                        Items.WRITABLE_BOOK,
                        Items.DIAMOND,
                        Items.ECHO_SHARD,
                        Items.AMETHYST_SHARD,
                        Items.LIGHT_WEIGHTED_PRESSURE_PLATE,
                        ModItems.SUPERPOLYMERIZATION.get()
                ),
                ModCurios.PDA.get(), // 输出：PDA设备
                1
        ).save(output, "pda");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_SWORD, // 主要输入：铁剑（攻击基础）
                List.of( // 次要输入（8个减速/冰冻主题材料）
                        Items.SNOWBALL,          // 雪球（寒冷减速）
                        Items.SNOWBALL,
                        Items.BLUE_ICE,          // 蓝冰（强力减速）
                        Items.SOUL_SAND,         // 灵魂沙（减速效果）
                        Items.SLIME_BALL,        // 粘液球（粘性减速）
                        Items.FERMENTED_SPIDER_EYE, // 发酵蛛眼（负面效果）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.PRISMARINE_SHARD   // 海晶碎片（寒冷材质）
                ),
                ModCurios.UFFFD.get(), // 输出：锟斤拷
                1
        ).save(output, "ufffd");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BONE, // 主要输入：骷髅头颅（骸骨核心）
                List.of( // 次要输入（8个防护/骸骨主题材料）
                        Items.BONE,              // 骨头（骸骨材料）
                        Items.BONE,
                        Items.BONE,
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.OBSIDIAN,          // 黑曜石（坚固防护）
                        Items.SOUL_SAND,         // 灵魂沙（亡灵能量）
                        Items.IRON_INGOT,        // 铁锭（强化结构）
                        Items.GOLD_INGOT         // 金锭（能量传导）
                ),
                ModCurios.BONE_ARMOR.get(), // 输出：骸骨之护
                1
        ).save(output, "bone_armor");
// 在 buildRecipes 方法中添加虚空触手配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ENDER_EYE, // 主要输入：末影之眼（虚空核心）
                List.of( // 次要输入（8个虚空/空间主题材料）
                        Items.ENDER_PEARL,      // 末影珍珠（空间传送）
                        Items.ENDER_PEARL,
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.CHORUS_FRUIT,     // 紫颂果（维度穿梭）
                        Items.SHULKER_SHELL,    // 潜影壳（空间容器）
                        Items.OBSIDIAN,         // 黑曜石（空间稳定）
                        Items.AMETHYST_SHARD    // 紫水晶碎片（空间共振）

                ),
                ModCurios.VOID_TENTACLES.get(), // 输出：虚空触手
                1
        ).save(output, "void_tentacles");
        // 在 buildRecipes 方法中添加皮革箭袋配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.LEATHER, // 主要输入：皮革（箭袋基础）
                List.of( // 次要输入（8个弓箭主题材料）
                        Items.ARROW,             // 箭（核心材料）
                        Items.ARROW,
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.STRING,            // 线（缝合固定）
                        Items.FEATHER,           // 羽毛（箭羽）
                        Items.FLINT,             // 燧石（箭头）
                        Items.LEATHER,       // 兔子皮（增强皮革）
                        Items.LEATHER           // 红石（强化效果）
                ),
                ModCurios.LEATHER_QUIVER.get(), // 输出：皮革箭袋
                1
        ).save(output, "leather_quiver");
        // 在 buildRecipes 方法中添加木纹箭袋配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(ModCurios.LEATHER_QUIVER.get()), // 主要输入：皮革箭袋（基础）
                List.of( // 次要输入（8个升级材料）
                        Ingredient.of(ItemTags.LOGS), // 原木标签
                        Ingredient.of(ItemTags.LOGS), // 原木标签
                        Ingredient.of(Items.FLETCHING_TABLE),
                        Ingredient.of(Items.SPECTRAL_ARROW),
                        Ingredient.of(Items.COPPER_INGOT),
                        Ingredient.of(Items.STRING),
                        Ingredient.of(Items.FEATHER),
                        Ingredient.of(Items.AMETHYST_SHARD)
                ),
                ModCurios.WOOD_GRAIN_QUIVER.get(), // 输出：木纹箭袋
                1
        ).save(output, "woodgrain_quiver");
        // 在 buildRecipes 方法中添加乾坤大挪移配方
// 在 buildRecipes 方法中添加乾坤大挪移配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ENDER_PEARL, // 主要输入：末影珍珠（空间转移）
                List.of( // 次要输入（8个物品）
                        Items.TNT,               // TNT
                        Items.REDSTONE_BLOCK,    // 红石块
                        Items.OBSIDIAN,          // 黑曜石
                        Items.SOUL_SAND,         // 灵魂沙
                        Items.GUNPOWDER,          // 火药
                        Items.GUNPOWDER,          // 火药
                        Items.GUNPOWDER          // 火药
                ),
                ModCurios.GREAT_SHIFT.get(), // 输出物品
                1                            // 输出数量
        ).save(output, "great_shift");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT, // 主要输入：金锭（黄金材质）
                List.of( // 次要输入（8个钓鱼/宝藏主题材料）
                        Items.FISHING_ROD,       // 钓鱼竿（基础工具）
                        Items.NAUTILUS_SHELL,    // 鹦鹉螺壳（海洋生物）
                        Items.PRISMARINE_CRYSTALS, // 海晶砂粒（海洋材料）
                        Items.GOLD_NUGGET,       // 金粒（黄金细节）
                        Items.GOLD_NUGGET,       // 金粒（黄金细节）
                        Items.GOLD_NUGGET,       // 金粒（黄金细节）
                        ModItems.MOON_INGOT.get(), // 月光锭

                        Items.LAPIS_LAZULI       // 青金石（幸运象征）
                ),
                ModCurios.GOLDEN_HOOK.get(), // 输出：黄金鱼钩
                1
        ).save(output, "golden__hook");
        // 在 buildRecipes 方法中添加冒险家腰带配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.LEATHER, // 主要输入：皮革（腰带基础）
                List.of( // 次要输入（8个旅行/探险主题材料）
                        Items.PAPER,              // 地图（记录旅程）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.COMPASS,          // 指南针（指引方向）
                        Items.IRON_BOOTS,       // 铁靴（行走工具）
                        Items.SPYGLASS,         // 望远镜（远眺）
                        Items.TORCH            // 火把（照亮前路）
                ),
                ModCurios.ADVENTURER_BELT.get(), // 输出：冒险家腰带
                1
        ).save(output, "adventurer_belt");
        // 在 buildRecipes 方法中添加高品质钓鱼线配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.STRING, // 主要输入：线（钓鱼线基础）
                List.of( // 次要输入（8个钓鱼/精密主题材料）
                        Items.STRING,             // 栓绳（强化线材）
                        Items.STRING,             // 栓绳（强化线材）
                        Items.STRING,             // 栓绳（强化线材）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.COPPER_INGOT,     // 铜锭（防腐蚀处理）
                        Items.SLIME_BALL,       // 粘液球（防水涂层）
                        Items.GOLD_NUGGET     // 金粒（精密连接）
                ),
                ModCurios.HIGH_QUALITY_FISHING_LINE.get(), // 输出：高品质钓鱼线
                1
        ).save(output, "high_quality_fishing_line");
        // 在 buildRecipes 方法中添加护符皮袋配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.LEATHER, // 主要输入：皮革（皮袋基础）
                List.of( // 次要输入（4个空间/容器主题材料）
                        Items.SHULKER_SHELL,    // 潜影壳（空间扩展）
                        Items.LEATHER,
                        Items.LEATHER,
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.GOLD_INGOT,       // 金锭（魔法增强）
                        Items.AMETHYST_SHARD    // 紫水晶碎片（能量稳定）
                ),
                ModCurios.AMULET_POUCH.get(), // 输出：护符皮袋
                1
        ).save(output, "amulet_pouch");
        // 在 buildRecipes 方法中添加双钩配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.FISHING_ROD, // 主要输入：钓鱼竿（基础工具）
                List.of( // 次要输入（3条钓鱼/双倍主题材料）
                        Items.IRON_INGOT,     // 铁粒（第二个钩子）
                        Items.IRON_INGOT,     // 铁粒（第二个钩子）
                        Items.IRON_INGOT,     // 铁粒（第二个钩子）
                        Items.IRON_INGOT,     // 铁粒（第二个钩子）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.LEAD,            // 栓绳（双线连接）
                        Items.RABBIT_FOOT      // 兔子脚（幸运加成）
                ),
                ModCurios.DOUBLE_HOOK.get(), // 输出：双钩
                1
        ).save(output, "double_hook");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.CHEST, // 主要输入：箱子（工具箱基础）
                List.of( // 次要输入（5个核心材料）
                        ModCurios.GOLDEN_HOOK.get(),      // 金鱼钩（宝藏加成）
                        ModCurios.DOUBLE_HOOK.get(),           // 双钩（双倍战利品）
                        ModCurios.HIGH_QUALITY_FISHING_LINE.get(), // 高品质钓鱼线（快速钓鱼）
                        Items.HEART_OF_THE_SEA,                // 海洋之心（终极钓鱼加成）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        ModItems.POLYMERIZATION.get()
                ),
                ModCurios.FISHERMAN_TOOLBOX.get(), // 输出：钓鱼佬的工具箱
                1
        ).save(output, "fisherman_toolbox");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.MAGMA_CREAM, // 主要输入：岩浆膏（岩浆防护）
                List.of( // 次要输入（5个岩浆/防护主题材料）
                        Items.OBSIDIAN,          // 黑曜石（耐热防护）
                        Items.NETHER_BRICK,      // 下界砖（下界材料）
                        Items.GOLD_INGOT,        // 金锭（护身符材质）
                        Items.GOLD_INGOT,        // 金锭（护身符材质）
                        Items.GOLD_INGOT,        // 金锭（护身符材质）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.BLAZE_POWDER,      // 烈焰粉（火焰控制）
                        Items.PHANTOM_MEMBRANE   // 幻翼膜（短暂防护）
                ),
                ModCurios.MAGMA_AMULET.get(), // 输出：熔火护身符
                1
        ).save(output, "magma_amulet");
// 在 buildRecipes 方法中添加皎月护身符配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月石锭（核心材料）
                List.of( // 次要输入（5个月光/夜晚主题材料）
                        Items.GLOWSTONE_DUST,   // 萤石粉（月光光芒）
                        Items.PHANTOM_MEMBRANE, // 幻翼膜（夜间生物）
                        Items.ENDER_PEARL,      // 末影珍珠（夜空神秘）
                        Items.CLOCK,            // 时钟（月相追踪）
                        Items.QUARTZ            // 石英（纯净月光）
                ),
                ModCurios.MOON_AMULET.get(), // 输出：皎月护身符
                1
        ).save(output, "lunar_amulet");
        // 在 buildRecipes 方法中添加熔火核心配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLAZE_ROD, // 主要输入：烈焰棒（火焰核心）
                List.of( // 次要输入（4个火焰/伤害主题材料）
                        Items.MAGMA_CREAM,     // 岩浆膏（熔岩伤害）
                        Items.FIRE_CHARGE,     // 火焰弹（爆炸伤害）
                        Items.NETHER_BRICK,    // 下界砖（下界环境）
                        ModItems.MOON_INGOT.get(), // 月光锭
                        Items.REDSTONE_BLOCK   // 红石块（能量增幅）
                ),
                ModCurios.BLAZE_CORE.get(), // 输出：熔火核心
                1
        ).save(output, "blaze_core");
        // 在 buildRecipes 方法中添加动力护符配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.POWERED_RAIL, // 主要输入：充能铁轨（动力象征）
                List.of( // 次要输入（5个速度/动能主题材料）
                        Items.REDSTONE_BLOCK,   // 红石块（能量核心）
                        Items.FEATHER,          // 羽毛（轻盈加速）
                        Items.SUGAR,            // 糖（速度加成）
                        Items.LIGHTNING_ROD,    // 避雷针（闪电速度）
                        Items.CLOCK             // 时钟（速度测量）
                ),
                ModCurios.POWER_AMULET.get(), // 输出：动力护符
                1
        ).save(output, "power_amulet");
        // 在 buildRecipes 方法中添加神秘凝胶配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月石锭（神秘能量）
                List.of( // 次要输入（5个凝胶/友好主题材料）
                        Items.SLIME_BALL,       // 史莱姆球（核心材料）
                        Items.SLIME_BALL,
                        Items.GOLDEN_CARROT,    // 金胡萝卜（驯服材料）
                        Items.EMERALD,          // 绿宝石（友好象征）
                        Items.LIME_DYE          // 黄绿色染料（史莱姆颜色）
                ),
                ModCurios.MYSTERIOUS_GEL.get(), // 输出：神秘凝胶
                1
        ).save(output, "mystical_gel");
        // 在 buildRecipes 方法中添加食史者配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.MYSTERIOUS_GEL.get(), // 主要输入：神秘凝胶（基础）
                List.of( // 次要输入（5个食用/凝胶主题材料）
                        Items.SLIME_BLOCK,       // 史莱姆块（大量凝胶）
                        ModItems.MOON_INGOT.get(), // 月石锭（能量转化）
                        Items.BOWL,              // 碗（食用容器）
                        Items.HONEY_BOTTLE,      // 蜂蜜瓶（调味剂）
                        Items.REDSTONE           // 红石（能量激活）
                ),
                ModCurios.SLIME_EATER.get(), // 输出：食史者
                1
        ).save(output, "slime_eater");
        // 在 buildRecipes 方法中添加暴食护符配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLDEN_CARROT, // 主要输入：金胡萝卜（高效食物）
                List.of( // 次要输入（5个食用/速度主题材料）
                        ModItems.MOON_INGOT.get(), // 月石锭（能量加速）
                        Items.SUGAR,             // 糖（加速代谢）
                        Items.SUGAR,             // 糖（加速代谢）
                        Items.SUGAR,             // 糖（加速代谢）
                        Items.REDSTONE_BLOCK     // 红石块（能量增强）
                ),
                ModCurios.GLUTTONY_AMULET.get(), // 输出：暴食护符
                1
        ).save(output, "gluttony_amulet");
        // 在 buildRecipes 方法中添加苦力怕护符配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.OBSIDIAN,   // 主要输入：苦力怕头颅（核心材料）
                List.of( // 次要输入（5个爆炸抑制主题材料）
                        ModItems.MOON_INGOT.get(), // 月石锭（平静能量）
                        Items.GUNPOWDER,         // 火药（爆炸材料）
                        Items.OBSIDIAN,          // 黑曜石（爆炸防护）
                        Items.OBSIDIAN,          // 黑曜石（爆炸防护）
                        Items.SHIELD,            // 盾牌（保护象征）
                        Items.WATER_BUCKET       // 水桶（冷却爆炸）
                ),
                ModCurios.CREEPFEAR_AMULET.get(), // 输出：苦力怕护符
                1
        ).save(output, "creeper_amulet");
        // 在 buildRecipes 方法中添加再生手环配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLDEN_APPLE, // 主要输入：金苹果（再生象征）
                List.of( // 次要输入（5个再生/生命主题材料）
                        ModItems.MOON_INGOT.get(), // 月石锭（周期性能量）
                        Items.GHAST_TEAR,        // 恶魂之泪（生命恢复）
                        Items.APPLE,             // 苹果（生命活力）
                        Items.REDSTONE,          // 红石（能量脉冲）
                        Items.ROSE_BUSH          // 玫瑰丛（生命象征）
                ),
                ModCurios.RESUSCITATOR.get(), // 输出：再生手环
                1
        ).save(output, "regeneration_bracelet");
        // 在 buildRecipes 方法中添加逢魔之刻配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月石锭（核心魔法材料）
                List.of( // 次要输入（8个魔法/黑暗主题材料）
                        Items.DIAMOND_BLOCK,        // 下界之星（强大魔力）
                        Items.ECHO_SHARD,         // 回响碎片（法术回响）
                        Items.AMETHYST_SHARD,   // 紫水晶簇（魔法增幅）
                        Items.BLAZE_ROD,          // 烈焰棒（火焰魔法）
                        Items.PHANTOM_MEMBRANE,   // 幻翼膜（虚空魔法）
                        Items.OBSIDIAN,     // 幽匿催发体（黑暗能量）
                        Items.CRYING_OBSIDIAN,    // 哭泣的黑曜石（黑暗魔法）
                        Items.GHAST_TEAR          // 恶魂之泪（痛苦能量）
                ),
                ModCurios.TWILIGHT_MOMENT.get(), // 输出：逢魔之刻
                1
        ).save(output, "twilight_hour");
        // 在 buildRecipes 方法中添加月光手环配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月石锭（核心材料）
                List.of( // 次要输入（5个月光/收集主题材料）
                        ModItems.MOON_INGOT.get(),
                        ModItems.MOON_INGOT.get(),
                        Items.GLOWSTONE_DUST,   // 萤石粉（月光精华）
                        Items.BONE,             // 骨头（生物遗骸）
                        Items.ENDER_PEARL,      // 末影珍珠（空间收集）
                        Items.GOLD_NUGGET       // 金粒（幸运象征）
                ),
                ModCurios.MOONLIGHT_BRACELET.get(), // 输出：月光手环
                1
        ).save(output, "moonlight_bracelet");
        // 在 buildRecipes 方法中添加多面手配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(), // 主要输入：月石锭（核心材料）
                List.of( // 次要输入（7个代表不同属性的材料）
                        Items.DIAMOND,          // 钻石（耐久与力量）
                        Items.EMERALD,          // 绿宝石（幸运与财富）
                        Items.REDSTONE_BLOCK,   // 红石块（速度与能量）
                        Items.IRON_BLOCK,       // 铁块（防御与坚韧）
                        Items.LAPIS_LAZULI,     // 青金石（魔法与经验）
                        Items.PHANTOM_MEMBRANE, // 幻翼膜（敏捷与灵活）
                        Items.HEART_OF_THE_SEA  // 海洋之心（生命与恢复）
                ),
                ModCurios.VERSATILE_PERSON.get(), // 输出：多面手
                1
        ).save(output, "versatile_hand");
        // 在 buildRecipes 方法中添加草鞋配方
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.LEATHER, // 主要输入：小麦（草材料）
                List.of( // 次要输入（2个基础材料）
                        Items.LEATHER,
                        Items.LEATHER,
                        Items.STRING, // 线（缝合固定）
                        Items.STRING  // 线
                ),
                ModCurios.STRAW_SANDALS.get(), // 输出：草鞋
                1
        ).save(output, "straw_shoes");
        // 在 buildRecipes 方法中添加羊蹄鞋配方
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 主要输入：草鞋（基础）
                List.of( // 次要输入（5个缓冲/保护主题材料）
                        Items.WHITE_WOOL,        // 白色羊毛（羊蹄材料）
                        Items.WHITE_WOOL,
                        ModItems.MOON_INGOT.get(), // 月石锭（特殊保护）
                        Items.SLIME_BALL,         // 粘液球（弹性缓冲）
                        Items.FEATHER             // 羽毛（轻盈特性）
                ),
                ModCurios.GOAT_BOOT.get(), // 输出：羊蹄鞋
                1
        ).save(output, "hoof_shoes");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.RABBIT_FOOT,         // 兔子脚（跳跃材料）
                        Items.COD,                 // 鳕鱼（猫喜欢）
                        ModItems.MOON_INGOT.get()  // 月石锭
                ),
                ModCurios.CAT_BOOT.get(), // 输出：猫猫鞋
                1
        ).save(output, "cat_shoes");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(),  // 月石锭
                List.of(
                        ModCurios.GOAT_BOOT.get(), // 羊蹄鞋（免疫摔落伤害）
                        ModCurios.CAT_BOOT.get(),  // 猫猫鞋（二段跳）
                        ModItems.POLYMERIZATION.get(),
                        ModItems.MOON_INGOT.get()  // 月石锭
                ),
                ModCurios.JUMPING_FOOTWEAR.get(), // 输出：灵跃靴
                1
        ).save(output, "agile_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.ORANGE_DYE,          // 橙色染料（狐狸颜色）
                        Items.SWEET_BERRIES,       // 甜浆果（狐狸食物）
                        Items.EMERALD,       // 甜浆果（狐狸食物）
                        ModItems.MOON_INGOT.get()  // 月石锭
                ),
                ModCurios.FOX_BOOT.get(), // 输出：狐狸鞋
                1
        ).save(output, "fox_shoes");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.RABBIT_HIDE,        // 兔子皮（速度材料）
                        Items.RABBIT_FOOT,        // 兔子脚（速度象征）
                        Items.CARROT,             // 胡萝卜（兔子食物）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.RABBIT_BOOT.get(), // 输出：兔兔鞋
                1
        ).save(output, "rabbit_shoes");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.MOON_INGOT.get(),  // 月石锭
                List.of(
                        ModCurios.RABBIT_BOOT.get(), // 兔兔鞋（速度）
                        ModCurios.FOX_BOOT.get(),    // 狐狸鞋（跳跃）
                        ModItems.MOON_INGOT.get(),    // 月石锭
                        ModItems.POLYMERIZATION.get()    // 月石锭
                ),
                ModCurios.WIND_LEAP_BOOTS.get(), // 输出：疾风鞋
                1
        ).save(output, "wind_leap_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.SUPERPOLYMERIZATION.get(),
                List.of(
                        ModCurios.WIND_LEAP_BOOTS.get(), // 疾风鞋（速度+跳跃高度）
                        ModCurios.JUMPING_FOOTWEAR.get(), // 灵跃靴（免疫摔落+二段跳）
                        ModItems.MOON_INGOT.get()   // 月石锭
                ),
                ModCurios.SKY_BEAST_SHOES.get(), // 输出：兽靴
                1
        ).save(output, "beast_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.AMETHYST_SHARD,     // 紫水晶碎片（生命能量）
                        Items.AMETHYST_SHARD,
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.CRYSTAL_BOOT.get(), // 输出：水晶靴
                1
        ).save(output, "crystal_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.IRON_SWORD,        // 铁剑（伤害象征）
                        Items.FLINT,             // 燧石（锋利材料）
                        Items.FLINT,             // 燧石（锋利材料）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.BLADE_BOOT.get(), // 输出：刀片靴
                1
        ).save(output, "blade_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.STRAW_SANDALS.get(), // 草鞋（基础）
                List.of(
                        Items.SHIELD,            // 盾牌（防御象征）
                        Items.IRON_INGOT,        // 铁锭（坚固材料）
                        Items.IRON_INGOT,        // 铁锭（坚固材料）
                        Items.OBSIDIAN,          // 黑曜石（坚固防御）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.GUARD_BOOT.get(), // 输出：守卫鞋
                1
        ).save(output, "guardian_shoes");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModItems.POLYMERIZATION.get(),
                List.of(
                        ModCurios.BLADE_BOOT.get(),     // 刀片靴（增加伤害）
                        ModCurios.GUARD_BOOT.get(),  // 守卫鞋（增加防御）
                        ModCurios.CRYSTAL_BOOT.get(),  // 守卫鞋（增加防御）
                        ModItems.MOON_INGOT.get()       // 月石锭
                ),
                ModCurios.WARRIOR_GREAVES.get(), // 输出：战士径甲
                1
        ).save(output, "warrior_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(ModCurios.STRAW_SANDALS.get()), // 草鞋基础
                List.of(
                        Ingredient.of(ItemTags.SMALL_FLOWERS), // 所有小型花标签
                        Ingredient.of(ItemTags.SMALL_FLOWERS), // 所有小型花标签
                        Ingredient.of(ModItems.MOON_INGOT.get()) // 月石锭
                ),
                ModCurios.FLOWER_BOOT.get(), 1 // 输出
        ).save(output, "flower_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(ModCurios.STRAW_SANDALS.get()), // 草鞋基础
                List.of(
                        Ingredient.of(Items.ICE),             // 所有冰类标签
                        Ingredient.of(ItemTags.LEAVES),          // 所有树叶标签
                        Ingredient.of(ModItems.MOON_INGOT.get()) // 月石锭
                ),
                ModCurios.WATERWALK_BOOT.get(), 1 // 输出
        ).save(output, "water_walking_boots");
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(ModCurios.STRAW_SANDALS.get()), // 草鞋基础
                List.of(
                        Ingredient.of(Items.ICE),             // 所有冰类标签
                        Ingredient.of(Items.IRON_INGOT),         // 铁锭（鞋底结构）
                        Ingredient.of(Items.LEATHER),        // 兔子皮（内衬材料）
                        Ingredient.of(ModItems.MOON_INGOT.get()) // 月石锭
                ),
                ModCurios.ROLLER_SKATES.get(), 1 // 输出
        ).save(output, "roller_skates");
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(Items.GOLD_INGOT),
                List.of(
                        Ingredient.of(Items.GOLD_NUGGET),       // 金粒（赌注象征）
                        Ingredient.of(Items.EMERALD),           // 绿宝石（高额赌注）
                        Ingredient.of(Items.RABBIT_FOOT),       // 兔子脚（幸运象征）
                        Ingredient.of(Items.GUNPOWDER),         // 火药（风险爆炸）
                        Ingredient.of(ModItems.MOON_INGOT.get()) // 月石锭
                ),
                ModCurios.GAMBLERS_CORSAGE.get(), 1 // 输出
        ).save(output, "gambler_corsage");
        CurioWorkRecipeBuilder.create(
                serializer,
                Ingredient.of(Items.BONE_BLOCK), // 主要输入：骨块（骰子材料）
                List.of(
                        Ingredient.of(Items.GOLD_INGOT),       // 金锭（筹码价值）
                        Ingredient.of(Items.DIAMOND),          // 钻石（高价值筹码）
                        Ingredient.of(Items.RABBIT_FOOT),      // 兔子脚（幸运象征）
                        Ingredient.of(Items.INK_SAC),          // 墨囊（骰子点数）
                        Ingredient.of(ModItems.MOON_INGOT.get()) // 月石锭
                ),
                ModCurios.GAMBLERS_DICE.get(), 1 // 输出
        ).save(output, "gambler_dice");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_NUGGET, // 主要输入：金粒（赌注象征）
                List.of(
                        Items.GHAST_TEAR,        // 恶魂之泪（生命恢复）
                        Items.SPIDER_EYE,        // 蜘蛛眼（虚弱象征）
                        Items.RABBIT_FOOT,       // 兔子脚（幸运象征）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.GAMBLERS_EARRINGS.get(), // 输出物品
                1                             // 输出数量
        ).save(output, "gambler_earring");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT, // 主要输入：金锭（金币象征）
                List.of(
                        Items.RABBIT_FOOT,       // 兔子脚（幸运象征）
                        Items.EMERALD,           // 绿宝石（财富象征）
                        Items.LAPIS_LAZULI,      // 青金石（幸运象征）
                        Items.CLOCK,             // 时钟（时机把握）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.GAMBLERS_GOLD_COIN.get(), // 输出物品
                1                           // 输出数量
        ).save(output, "gambler_coin");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.PAPER, // 主要输入：纸（扑克基础）
                List.of(
                        Items.INK_SAC,           // 墨囊（绘制图案）
                        Items.RED_DYE,           // 红色染料（红心/方块）
                        Items.BLACK_DYE,         // 黑色染料（黑桃/梅花）
                        Items.GOLD_NUGGET,       // 金粒（赌注象征）
                        Items.GOLD_NUGGET,       // 金粒（赌注象征）
                        Items.CLOCK,             // 时钟（30秒周期）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.GAMBLERS_POKER.get(), // 输出物品
                1                            // 输出数量
        ).save(output, "gambler_poker");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BOOK, // 主要输入：附魔书（核心知识）
                List.of(
                        Items.LAPIS_LAZULI,      // 青金石（附魔材料）
                        Items.LAPIS_LAZULI,
                        Items.LAPIS_LAZULI,
                        Items.AMETHYST_SHARD,  // 紫水晶簇（附魔增幅）
                        Items.ENDER_EYE,         // 末影之眼（神秘知识）
                        Items.DIAMOND_BLOCK,       // 下界之星（强大能量）
                        ModItems.MOON_INGOT.get(), // 月石锭
                        Items.EXPERIENCE_BOTTLE  // 经验瓶（知识精华）
                ),
                ModCurios.ENCHANT_EYE.get(), // 输出物品
                1                           // 输出数量
        ).save(output, "enchanted_eye");
        CurioWorkRecipeBuilder.create(
                serializer,
                ModCurios.WOOD_GRAIN_QUIVER.get(), // 主要输入：光灵箭（灵魂箭基础）
                List.of(
                        Items.SOUL_SAND,         // 灵魂沙（灵魂来源）
                        Items.SOUL_SAND,
                        Items.SOUL_TORCH,        // 灵魂火把（灵魂能量）
                        Items.SOUL_TORCH,
                        Items.GHAST_TEAR,        // 恶魂之泪（灵魂精华）
                        Items.ECHO_SHARD,        // 回响碎片（灵魂共鸣）
                        Items.DIAMOND_BLOCK,       // 下界之星（传奇能量）
                        ModItems.MOON_INGOT.get() // 月石锭
                ),
                ModCurios.SOUL_QUIVER.get(), // 输出物品
                1                           // 输出数量
        ).save(output, "soul_quiver");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.SHIELD,  // 主要输入：盾牌（保护象征）
                List.of(
                        Items.GOLD_INGOT,  // 不死图腾（防止死亡）
                        Items.ENDER_PEARL,        // 末影珍珠（瞬移保命）
                        Items.OBSIDIAN,           // 黑曜石（坚固防护）
                        Items.FEATHER,            // 羽毛（轻盈防摔落）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModCurios.NEWBIE_UMBRELLA.get(), // 输出：菜鸟保护伞
                1                              // 输出数量
        ).save(output, "noob_umbrella");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.NETHERITE_INGOT,  // 主要输入：下界合金锭（基础强度）
                List.of(
                        Items.NETHER_STAR,         // 下界之星（伤害提升）
                        Items.DIAMOND_SWORD,        // 钻石剑（暴击几率）
                        Items.NETHERITE_CHESTPLATE, // 下界合金胸甲（护甲提升）
                        Items.GOLDEN_APPLE,         // 金苹果（治疗效果）
                        Items.AMETHYST_SHARD,       // 紫水晶碎片（能量增幅）
                        Items.ECHO_SHARD,           // 回响碎片（多重效果）
                        ModItems.MOON_INGOT.get()   // 月石锭（必须材料）
                ),
                ModCurios.ERODED_FACE.get(), // 输出：净蚀假面
                1                             // 输出数量
        ).save(output, "erosion_mask");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.TOTEM_OF_UNDYING,  // 主要输入：不死图腾（免疫致命伤害基础）
                List.of(
                        Items.GOLDEN_APPLE,        // 金苹果（生命恢复）
                        Items.TURTLE_HELMET,       // 海龟壳（伤害吸收）
                        Items.SOUL_SAND,           // 灵魂沙（灵魂守护）
                        Items.ECHO_SHARD,          // 回响碎片（死亡回响）
                        Items.AMETHYST_SHARD,    // 紫水晶簇（能量增幅）
                        ModItems.MOON_INGOT.get()  // 月石锭（必须材料）
                ),
                ModCurios.SOUL_MARK.get(), // 输出：魂守烙印
                1                                // 输出数量
        ).save(output, "soul_guard_brand");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLAZE_ROD,  // 主要输入：烈焰棒（火焰能量核心）
                List.of(
                        Items.MAGMA_CREAM,        // 岩浆膏（冲击波基础）
                        Items.FIRE_CHARGE,        // 火焰弹（冲击波原型）
                        Items.GUNPOWDER,          // 火药（爆炸能量）
                        Items.AMETHYST_SHARD,     // 紫水晶碎片（概率增幅）
                        Items.SOUL_SAND,          // 灵魂沙（范围影响）
                        Items.NETHER_BRICK,       // 下界砖（地狱关联）
                        Items.REDSTONE_BLOCK,      // 红石块（触发机制）
                        ModItems.MOON_INGOT.get()  // 月石锭（必须材料）
                ),
                ModCurios.EMBER_CORE.get(), // 输出：余烬核心
                1                           // 输出数量
        ).save(output, "ember_core");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.WHITE_BED,  // 主要输入：床（睡眠核心）
                List.of(
                        Items.GOLDEN_APPLE,       // 金苹果（属性加成）
                        Items.AMETHYST_SHARD,   // 紫水晶簇（梦境能量）
                        Items.FEATHER,            // 羽毛（轻盈醒来）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModCurios.DREAM_HEART.get(), // 输出：美梦之心
                1                           // 输出数量
        ).save(output, "dream_heart");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GOLD_INGOT,  // 主要输入：下界合金碎片（危险材料）
                List.of(
                        Items.TNT,               // TNT（爆炸伤害象征）
                        Items.GOLD_INGOT, // 凋灵骷髅头（灾厄源头）
                        ModItems.MOON_INGOT.get()  // 月石锭（必须材料）
                ),
                ModCurios.DISASTER_EMBLEM.get(), // 输出：灾厄纹章
                1                           // 输出数量
        ).save(output, "disaster_emblem");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.DIRT,  // 主要输入：凋零玫瑰（致命伤害象征）
                List.of(
                        Items.DIRT, // 凋灵骷髅头（死亡源头）
                        Items.DIRT,         // 龙息（即死效果）
                        ModItems.MOON_INGOT.get()     // 月石锭（必须材料）
                ),
                ModCurios.THE_EVIL_CURSE.get(), // 输出：凶竖恶咒
                1                                // 输出数量
        ).save(output, "the_evil_curse");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLAZE_ROD,  // 主要输入：烈焰棒（火焰法杖基础）
                List.of(
                        Items.SOUL_TORCH,         // 灵魂火把（灵魂火焰来源）
                        Items.SOUL_CAMPFIRE,      // 灵魂营火（持续燃烧）
                        Items.BLAZE_POWDER,       // 烈焰粉（火焰增强）
                        Items.AMETHYST_SHARD,   // 紫水晶簇（效果增幅）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.FLAME_STAFF.get(), // 输出：火焰灵魂法杖
                1                              // 输出数量
        ).save(output, "flame_soul_staff");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ECHO_SHARD,  // 主要输入：回响碎片（灵魂核心）
                List.of(
                        Items.PHANTOM_MEMBRANE,  // 幻翼膜（蝶翼基础）
                        Items.SOUL_TORCH,         // 灵魂火把（灵魂能量）
                        Items.AMETHYST_SHARD,     // 紫水晶碎片（召唤增幅）
                        Items.FEATHER,            // 羽毛（轻盈特性）
                        Items.SPIDER_EYE,         // 蜘蛛眼（攻击性）
                        Items.GLOWSTONE_DUST,     // 荧石粉（光芒效果）
                        Items.SWEET_BERRIES,     //甜浆果
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.NECROPSYCHE_PAPILLON.get(), // 输出：冥心蝶语
                1                              // 输出数量
        ).save(output, "necropsyche_papillon");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.BLAZE_ROD,  // 主要输入：烈焰棒（法杖基础）
                List.of(
                        Items.BLAZE_POWDER,     // 烈焰粉（火焰能量）
                        Items.FIRE_CHARGE,      // 火焰弹（火球原型）
                        Items.COAL,             // 煤炭（燃料来源）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.FIREBALL_STAFF.get(), // 输出：火球法杖
                1                           // 输出数量
        ).save(output, "fireball_staff");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.SPYGLASS,  // 主要输入：望远镜（基础光学设备）
                List.of(
                        Items.AMETHYST_SHARD,   // 紫水晶碎片（光学增强）
                        Items.EMERALD,           // 绿宝石（清晰度提升）
                        Items.GLOWSTONE_DUST,    // 荧石粉（低光增强）
                        Items.ENDER_PEARL,       // 末影珍珠（远距聚焦）
                        Items.GOLD_INGOT,        // 金锭（精密部件）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.BINOCULARS.get(), // 输出：双筒望远镜
                1                           // 输出数量
        ).save(output, "binoculars");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.ENDER_EYE,  // 主要输入：末影之眼（传送核心）
                List.of(
                        Items.ENDER_PEARL,         // 末影珍珠（空间定位）
                        Items.OBSIDIAN,            // 黑曜石（空间稳定）
                        Items.AMETHYST_SHARD,    // 紫水晶簇（能量聚焦）
                        Items.REDSTONE_BLOCK,      // 红石块（蓄力机制）
                        Items.DIAMOND,             // 钻石（能量容器）
                        ModItems.MOON_INGOT.get()  // 月石锭（跨维度支持）
                ),
                ModItems.DOMAIN_STONE.get(), // 输出：界域之石
                1                           // 输出数量
        ).save(output, "domain_stone");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.APPLE,
                List.of(
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE,
                        Items.APPLE
                ),
                ModItems.INFINITE_APPLE.get(),1
        ).save(output,"infinite_apple");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_INGOT,  // 主要输入：铁锭（基础枪身材料）
                List.of(
                        Items.GUNPOWDER,        // 火药（推进剂）
                        Items.AMETHYST_SHARD,   // 紫水晶碎片（魔法子弹）
                        Items.IRON_NUGGET,      // 铁粒（银子弹替代）
                        Items.REDSTONE,          // 红石（切换机制）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.DEMON_BREAKING_SILVER_HUNTER.get(), // 输出：破魔银狩
                1                           // 输出数量
        ).save(output, "demon_breaking_silver_hunter");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_BLOCK,  // 主要输入：铁块（重型枪身）
                List.of(
                        Items.DISPENSER,         // 发射器（速射机制）
                        Items.GUNPOWDER,         // 火药（推进剂）
                        Items.IRON_BARS,         // 铁栏杆（多枪管）
                        Items.REDSTONE_BLOCK,    // 红石块（高速电路）
                        Items.GOLD_INGOT,             // 箭（基础弹药）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.MINIGUN.get(), // 输出：minigun
                1                      // 输出数量
        ).save(output, "minigun");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.NETHERITE_INGOT,  // 主要输入：下界合金锭（高级枪身）
                List.of(
                        Items.REDSTONE_BLOCK,    // 红石块（高速电路）
                        Items.DISPENSER,         // 发射器（速射机制）
                        Items.GUNPOWDER,         // 火药（推进剂）
                        Items.FIRE_CHARGE,       // 火焰弹（高爆伤害）
                        Items.IRON_BARS,         // 铁栏杆（紧凑枪管）
                        Items.DIAMOND,           // 钻石（精密部件）
                        Items.AMETHYST_SHARD,    // 紫水晶碎片（能量聚焦）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.HKMP7.get(), // 输出：HKMP7
                1                     // 输出数量
        ).save(output, "hkmp7");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.GUNPOWDER,  // 主要输入：火药（基础推进剂）
                List.of(
                        Items.IRON_NUGGET,      // 铁粒（弹头材料）x1
                        Items.IRON_NUGGET,      // 铁粒（弹头材料）x2
                        Items.IRON_NUGGET,      // 铁粒（弹头材料）x3
                        Items.IRON_NUGGET,      // 铁粒（弹头材料）x4
                        Items.COPPER_INGOT
                ), // 铜锭（弹壳材料）
                ModItems.FLINTLOCK_BULLET.get(), // 输出：火枪子弹
                30                          // 输出数量：30发
        ).save(output, "musket_bullet");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.STONE,  // 主要输入：蜂蜜块（吸收冲击）
                List.of(
                        Items.REDSTONE_BLOCK,     // 红石块（能量转换）
                        Items.IRON_BLOCK,         // 铁块（坚固外壳）
                        Items.GLASS_PANE,         // 玻璃板（观察窗）
                        Items.LIGHTNING_ROD,      // 避雷针（能量引导）
                        Items.COPPER_BLOCK,       // 铜块（能量传导）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModBlocks.BLAST_ABSORBER_ITEM.get(), // 输出：爆炸吸收器
                1                               // 输出数量
        ).save(output, "explosion_absorber");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.DIAMOND_AXE,  // 主要输入：下界合金斧（顶级斧头基础）
                List.of(
                        Items.DIAMOND,           // 钻石（增强锋利度）
                        Items.DIAMOND,           // 绿宝石（提升效率）
                        Items.GOLD_INGOT,        // 金锭（提升附魔能力）
                        Items.REDSTONE_BLOCK,    // 红石块（加速砍伐）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.LUCY_AXE.get(), // 输出：路西斧
                1                       // 输出数量
        ).save(output, "lucy_axe");
        CurioWorkRecipeBuilder.create(
                serializer,
                Items.DIAMOND_SHOVEL,  // 主要输入：下界合金铲（顶级基础）
                List.of(
                        Items.DIAMOND,           // 钻石（增强耐用性）
                        Items.IRON_BLOCK,        // 铁块（加重铲头）
                        Items.GOLD_INGOT,        // 金锭（提升效率）
                        Items.REDSTONE_BLOCK,    // 红石块（挖掘加速）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.GRAVEDIGGER.get(), // 输出：掘墓铲
                1                           // 输出数量
        ).save(output, "gravedigger_shovel");

        CurioWorkRecipeBuilder.create(
                serializer,
                Items.IRON_SWORD,  // 主要输入：下界合金剑（基础武器）
                List.of(
                        Items.IRON_BARS,        // 铁栏杆（镰刀造型）
                        Items.SUNFLOWER,        // 向日葵（亡灵克星）
                        Items.BONE_BLOCK,       // 骨块（亡灵能量）
                        Items.SOUL_TORCH,       // 灵魂火把（灵魂伤害）
                        Items.BONE,       // 恶魂之泪（幽灵伤害）
                        Items.ROTTEN_FLESH,       // 恶魂之泪（幽灵伤害）
                        ModItems.MOON_INGOT.get() // 月石锭（必须材料）
                ),
                ModItems.DEATH_HOOK.get(), // 输出：死亡钩镰
                1                           // 输出数量
        ).save(output, "death_hook");







        super.buildRecipes(output);
    }
    protected void addShapelessRecipe(RecipeOutput consumer, String name, ItemLike output, int count, ItemLike... inputs){
        {
            ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, count);
            for (ItemLike input : inputs) {
                builder.requires(input);
            }
            builder.unlockedBy("has_" + inputs[0].asItem().toString(), has(inputs[0]))
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(MODID,name));
        }
    }

}
