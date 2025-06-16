package net.royling.lovelysparklepieces.ModCreative;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;

public class ModCreative {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LovelySparklePieces.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.lovely_sparkle_pieces"))
            .icon(() -> ModCurios.BLASPHEMOUS_CONTRACT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModCurios.MAGNETIC_RING.get().getDefaultInstance());
                output.accept(ModCurios.SUPER_MAGNETIC_RING.get().getDefaultInstance());
                output.accept(ModCurios.ENDER_RING.get().getDefaultInstance());
                output.accept(ModCurios.CRIT_RING.get().getDefaultInstance());
                output.accept(ModCurios.MEMORY_RING.get().getDefaultInstance());
                output.accept(ModCurios.NIGHT_OWL_RING.get().getDefaultInstance());
                output.accept(ModCurios.CRUSH_STONE_RING.get().getDefaultInstance());
                output.accept(ModCurios.INFERNO_RING.get().getDefaultInstance());
                output.accept(ModCurios.ECO_RING.get().getDefaultInstance());
                output.accept(ModCurios.PUREGOLD_RING.get().getDefaultInstance());
                output.accept(ModCurios.BASTION_RING.get().getDefaultInstance());
                output.accept(ModCurios.ROFAP.get().getDefaultInstance());
                //首饰
                output.accept(ModCurios.FPS_EYE.get().getDefaultInstance());
                output.accept(ModCurios.BLACKSTONE_HEART.get().getDefaultInstance());
                output.accept(ModCurios.NIGHT_VISION.get().getDefaultInstance());
                output.accept(ModCurios.DOUBLE_NIGHT_VISION.get().getDefaultInstance());
                output.accept(ModCurios.QUARTER_NIGHT_VISION.get().getDefaultInstance());
                output.accept(ModCurios.JELLYFISH_HELMET.get().getDefaultInstance());
                output.accept(ModCurios.CAPITALIST_HEAT.get().getDefaultInstance());
                output.accept(ModCurios.POSEIDON_RESPIRATOR.get().getDefaultInstance());
                output.accept(ModCurios.EYE_MASK.get().getDefaultInstance());
                output.accept(ModCurios.MARKSMAN_GOGGLES.get().getDefaultInstance());
                output.accept(ModCurios.WITCH_HAT.get().getDefaultInstance());
                output.accept(ModCurios.YELLOW_HEADSCARF.get().getDefaultInstance());
                output.accept(ModCurios.BABY_FEEDER.get().getDefaultInstance());
                output.accept(ModCurios.GILDED_PIPE.get().getDefaultInstance());
                //胸饰
                output.accept(ModCurios.HEAVY_BIGROCK.get().getDefaultInstance());
                output.accept(ModCurios.POCKET_WATCH.get().getDefaultInstance());
                output.accept(ModCurios.SPEEDOMETER.get().getDefaultInstance());
                output.accept(ModCurios.POSITION_TRACKER.get().getDefaultInstance());
                output.accept(ModCurios.GPS.get().getDefaultInstance());
                output.accept(ModCurios.PDA.get().getDefaultInstance());
                output.accept(ModCurios.UFFFD.get().getDefaultInstance());
                output.accept(ModCurios.BONE_ARMOR.get().getDefaultInstance());
                //背饰
                output.accept(ModCurios.MERMAID_TAIL.get().getDefaultInstance());
                output.accept(ModCurios.VOID_TENTACLES.get().getDefaultInstance());
                output.accept(ModCurios.LEATHER_QUIVER.get().getDefaultInstance());
                output.accept(ModCurios.WOOD_GRAIN_QUIVER.get().getDefaultInstance());
                output.accept(ModCurios.GREAT_SHIFT.get().getDefaultInstance());
                //腰带
                output.accept(ModCurios.GOLDEN_HOOK.get().getDefaultInstance());
                output.accept(ModCurios.ADVENTURER_BELT.get().getDefaultInstance());
                output.accept(ModCurios.HIGH_QUALITY_FISHING_LINE.get().getDefaultInstance());
                output.accept(ModCurios.AMULET_POUCH.get().getDefaultInstance());
                output.accept(ModCurios.DOUBLE_HOOK.get().getDefaultInstance());
                output.accept(ModCurios.FISHERMAN_TOOLBOX.get().getDefaultInstance());
                //护符
                output.accept(ModCurios.MAGMA_AMULET.get().getDefaultInstance());
                output.accept(ModCurios.MOON_AMULET.get().getDefaultInstance());
                output.accept(ModCurios.BLAZE_CORE.get().getDefaultInstance());
                output.accept(ModCurios.POWER_AMULET.get().getDefaultInstance());
                output.accept(ModCurios.MYSTERIOUS_GEL.get().getDefaultInstance());
                output.accept(ModCurios.SLIME_EATER.get().getDefaultInstance());
                output.accept(ModCurios.GLUTTONY_AMULET.get().getDefaultInstance());
                output.accept(ModCurios.CREEPFEAR_AMULET.get().getDefaultInstance());
                //手环
                output.accept(ModCurios.RESUSCITATOR.get().getDefaultInstance());
                output.accept(ModCurios.TWILIGHT_MOMENT.get().getDefaultInstance());
                output.accept(ModCurios.MOONLIGHT_BRACELET.get().getDefaultInstance());
                output.accept(ModCurios.VERSATILE_PERSON.get().getDefaultInstance());
                //足部
                output.accept(ModCurios.STRAW_SANDALS.get().getDefaultInstance());
                output.accept(ModCurios.GOAT_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.FOX_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.RABBIT_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.CAT_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.CRYSTAL_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.BLADE_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.GUARD_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.WARRIOR_GREAVES.get().getDefaultInstance());
                output.accept(ModCurios.FLOWER_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.WATERWALK_BOOT.get().getDefaultInstance());
                output.accept(ModCurios.ROLLER_SKATES.get().getDefaultInstance());
                output.accept(ModCurios.JUMPING_FOOTWEAR.get().getDefaultInstance());
                output.accept(ModCurios.WIND_LEAP_BOOTS.get().getDefaultInstance());
                output.accept(ModCurios.SKY_BEAST_SHOES.get().getDefaultInstance());
                //赌徒
                output.accept(ModCurios.GAMBLERS_CORSAGE.get().getDefaultInstance());
                output.accept(ModCurios.GAMBLERS_DICE.get().getDefaultInstance());
                output.accept(ModCurios.GAMBLERS_EARRINGS.get().getDefaultInstance());
                output.accept(ModCurios.GAMBLERS_GOLD_COIN.get().getDefaultInstance());
                output.accept(ModCurios.GAMBLERS_POKER.get().getDefaultInstance());
                //传奇饰品
                output.accept(ModCurios.BLASPHEMOUS_CONTRACT.get().getDefaultInstance());
                output.accept(ModCurios.MIRROR_AND_WATER.get().getDefaultInstance());
                output.accept(ModCurios.ENCHANT_EYE.get().getDefaultInstance());
                output.accept(ModCurios.SOUL_QUIVER.get().getDefaultInstance());
                output.accept(ModCurios.NEWBIE_UMBRELLA.get().getDefaultInstance());
                output.accept(ModCurios.DRAGON_HEART.get().getDefaultInstance());
                output.accept(ModCurios.ERODED_FACE.get().getDefaultInstance());
                output.accept(ModCurios.SOUL_MARK.get().getDefaultInstance());
                output.accept(ModCurios.EMBER_CORE.get().getDefaultInstance());
                output.accept(ModCurios.VALORANT.get().getDefaultInstance());
                output.accept(ModCurios.DREAM_HEART.get().getDefaultInstance());
                output.accept(ModCurios.DISASTER_EMBLEM.get().getDefaultInstance());
                output.accept(ModCurios.THE_EVIL_CURSE.get().getDefaultInstance());
                //物品
                output.accept(ModItems.FLAME_STAFF.get().getDefaultInstance());
                output.accept(ModItems.NECROPSYCHE_PAPILLON.get().getDefaultInstance());
                output.accept(ModItems.ABIGAILS_FLOWER.get().getDefaultInstance());
                output.accept(ModItems.FIREBALL_STAFF.get().getDefaultInstance());
                output.accept(ModItems.PIRATE_SCIMITAR.get().getDefaultInstance());
                output.accept(ModItems.FISH_AXE.get().getDefaultInstance());
                output.accept(ModItems.FISH_PICKAXE.get().getDefaultInstance());
                output.accept(ModItems.FISH_SHOVEL.get().getDefaultInstance());
                output.accept(ModItems.FISH_HOE.get().getDefaultInstance());
                output.accept(ModItems.SOUL_TORCH.get().getDefaultInstance());
                output.accept(ModItems.BINOCULARS.get().getDefaultInstance());
                output.accept(ModItems.DOMAIN_STONE.get().getDefaultInstance());
                output.accept(ModItems.POLYMERIZATION.get().getDefaultInstance());
                output.accept(ModItems.SUPERPOLYMERIZATION.get().getDefaultInstance());
                output.accept(ModItems.FISHING_TREASURE.get().getDefaultInstance());
                output.accept(ModItems.MOJA_COLA.get().getDefaultInstance());
                output.accept(ModItems.BATTERY.get().getDefaultInstance());
                output.accept(ModItems.MULTI_PURPOSE_TOOL.get().getDefaultInstance());
                output.accept(ModItems.INFINITE_APPLE.get().getDefaultInstance());
                output.accept(ModItems.CAPITALIST_CAKE.get().getDefaultInstance());
                output.accept(ModItems.DEMON_BREAKING_SILVER_HUNTER.get().getDefaultInstance());
                output.accept(ModItems.MINIGUN.get().getDefaultInstance());
                output.accept(ModItems.HKMP7.get().getDefaultInstance());
                output.accept(ModItems.FLINTLOCK_BULLET.get().getDefaultInstance());
                output.accept(ModItems.HELL_FIRE.get().getDefaultInstance());
                output.accept(ModItems.SPARKLE_SHARD.get().getDefaultInstance());
                output.accept(ModItems.MOON_INGOT.get().getDefaultInstance());
                //方块
                output.accept(ModBlocks.SOUL_LIGHT_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.MOLTEN_STONE_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.MOLTEN_DIRT_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.BLAST_ABSORBER_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.CURIO_WORKBENCH_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.MOONSTONE_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.WILDFIRE_PORTAL_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.LAB_RUSTY_PLATE_ITEM2.get().getDefaultInstance());
                output.accept(ModBlocks.LAB_BAW_FLOOR_ITEM.get().getDefaultInstance());
                output.accept(ModBlocks.LAB_RUSTY_PLATE_ITEM.get().getDefaultInstance());


                output.accept(ModItems.LUCY_AXE.get().getDefaultInstance());
                output.accept(ModItems.GRAVEDIGGER.get().getDefaultInstance());
                output.accept(ModItems.DEATH_HOOK.get().getDefaultInstance());


            }).build());
}
