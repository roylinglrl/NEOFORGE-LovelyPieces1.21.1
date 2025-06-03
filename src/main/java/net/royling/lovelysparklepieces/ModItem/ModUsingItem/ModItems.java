package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.FlintlockBulletItem;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.DemonBreakingSilverHunter;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.HKMP7Item;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.MiniGunItem;
import net.royling.lovelysparklepieces.ModSounds.ModSounds;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, LovelySparklePieces.MODID);
    public static final Supplier<Item> FLAME_STAFF = ITEMS.register("flame_soul_staff",
            ()->new FlameSoulStaff(new Item.Properties()));
    public static final Supplier<Item> SOUL_TORCH = ITEMS.register("soul_torch",
            ()->new SoulTorchItem(new Item.Properties()));
    public static final Supplier<Item> BINOCULARS = ITEMS.register("binoculars",
            ()->new Binoculars(new Item.Properties()));
    public static final Supplier<Item> NECROPSYCHE_PAPILLON = ITEMS.register("necropsyche_papillon",
            ()->new NecropsychePapillonItem(new Item.Properties()));
    public static final Supplier<Item> DOMAIN_STONE = ITEMS.register("domain_stone",
            ()->new DomainStoneItem(new Item.Properties()));
    public static final Supplier<Item> FIREBALL_STAFF  = ITEMS.register("fireball_staff",
            ()->new FireballStaffItem(new Item.Properties()));
    public static final Supplier<Item> POLYMERIZATION = ITEMS.register("polymerization",
            ()->new PolymerizationItem(new Item.Properties()));
    public static final Supplier<Item> SUPERPOLYMERIZATION = ITEMS.register("superpolymerization",
            ()->new SuperPolymerizationItem(new Item.Properties()));
    public static final Supplier<Item> FISHING_TREASURE = ITEMS.register("fishing_treasure",
            ()->new FishingTreasure(new Item.Properties(), ModSounds.TREASURE_OPEN.get()));

    public static final Tier PIRATE_SCIMITAR_TIRE = new SimpleTier(BlockTags.INCORRECT_FOR_IRON_TOOL,
            320,6f,2f,28,()-> Ingredient.of(Items.IRON_INGOT));

    public static final Supplier<Item> PIRATE_SCIMITAR = ITEMS.register("pirate_scimitar",
            ()->new SwordItem(PIRATE_SCIMITAR_TIRE,new Item.Properties().stacksTo(1)
                    .attributes(SwordItem.createAttributes(PIRATE_SCIMITAR_TIRE,3,-2.4f))));
    public static final Supplier<Item> FISH_PICKAXE = ITEMS.register("fish_pickaxe",
            ()->new PickaxeItem(PIRATE_SCIMITAR_TIRE,new Item.Properties().stacksTo(1)
                    .attributes(SwordItem.createAttributes(PIRATE_SCIMITAR_TIRE,1,-2.8f))));
    public static final Supplier<Item> FISH_AXE = ITEMS.register("fish_axe",
            ()->new AxeItem(PIRATE_SCIMITAR_TIRE,new Item.Properties().stacksTo(1)
                    .attributes(SwordItem.createAttributes(PIRATE_SCIMITAR_TIRE,6,-3.1f))));
    public static final Supplier<Item> FISH_SHOVEL = ITEMS.register("fish_shovel",
            ()->new ShovelItem(PIRATE_SCIMITAR_TIRE,new Item.Properties().stacksTo(1)
                    .attributes(SwordItem.createAttributes(PIRATE_SCIMITAR_TIRE,0.5f,-1f))));
    public static final Supplier<Item> FISH_HOE = ITEMS.register("fish_hoe",
            ()->new HoeItem(PIRATE_SCIMITAR_TIRE,new Item.Properties().stacksTo(1)
                    .attributes(SwordItem.createAttributes(PIRATE_SCIMITAR_TIRE,-2,-1f))));

    public static final Supplier<Item> MOJA_COLA = ITEMS.register("moja_cola",
            ()->new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).build())){
                @Override
                public SoundEvent getEatingSound() {
                    return SoundEvents.GENERIC_DRINK;
                }
            });

    public static final Supplier<Item> BATTERY = registerItem("battery");
    public static final Supplier<Item> NEW_ITEM = registerItem("new_item");

    public static final Supplier<Item> MULTI_PURPOSE_TOOL = ITEMS.register("multi_purpose_tool",
            ()->new MultiPurposeTool(new Item.Properties()));
    public static final Supplier<Item> INFINITE_APPLE = ITEMS.register("infinite_apple",
            ()->new InfiniteApple(new Item.Properties()));
    public static final Supplier<Item> DEMON_BREAKING_SILVER_HUNTER = ITEMS.register("demon_breaking_silver_hunter",
            ()->new DemonBreakingSilverHunter(new Item.Properties()));
    public static final Supplier<Item> MINIGUN = ITEMS.register("minigun",
            ()->new MiniGunItem(new Item.Properties()));
    public static final Supplier<Item> HKMP7 = ITEMS.register("hkmp7",
            ()->new HKMP7Item(new Item.Properties()));

    public static final Supplier<Item> FLINTLOCK_BULLET = ITEMS.register("flintlock_bullet",
            ()->new FlintlockBulletItem(new Item.Properties()));
    public static final Supplier<Item> CAPITALIST_CAKE = ITEMS.register("capitalist_cake",
            ()->new CapitalistCake(new Item.Properties()));



    public static Supplier<Item> registerItem(String id){
        return ITEMS.register(id,()->new Item(new Item.Properties()));
    }

}
