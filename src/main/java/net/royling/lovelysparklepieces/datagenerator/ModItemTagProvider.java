package net.royling.lovelysparklepieces.datagenerator;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, LovelySparklePieces.MODID,existingFileHelper);
    }
    private static TagKey<Item> create(String namespace,String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
    }
    public static final TagKey<Item> ENCHANTABLE = create("c","enchantables");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ENCHANTABLE).add(
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get(),
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_SHOVEL.get(),
                ModItems.GRAVEDIGGER.get(),
                ModItems.DEMON_BREAKING_SILVER_HUNTER.get(),
                ModItems.MINIGUN.get(),
                ModItems.HKMP7.get(),
                ModItems.FISH_HOE.get()
        );

        this.tag(ItemTags.PICKAXES).add(
                ModItems.FISH_PICKAXE.get()
        );
        this.tag(ItemTags.AXES).add(
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get()
        );
        this.tag(ItemTags.SWORDS).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get()
        );
        this.tag(ItemTags.SHOVELS).add(
                ModItems.FISH_SHOVEL.get(),
                ModItems.GRAVEDIGGER.get()
        );
        this.tag(ItemTags.HOES).add(
                ModItems.FISH_HOE.get()
        );
        this.tag(ItemTags.MINING_ENCHANTABLE).add(
                ModItems.FISH_SHOVEL.get(),
                ModItems.GRAVEDIGGER.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get(),
                ModItems.FISH_PICKAXE.get()
        );
        this.tag(ItemTags.CLUSTER_MAX_HARVESTABLES).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get()
        );
        this.tag(ItemTags.WEAPON_ENCHANTABLE).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get()
        );
        this.tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get()
        );
        this.tag(ItemTags.FIRE_ASPECT_ENCHANTABLE).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get()
        );
        this.tag(ItemTags.SWORD_ENCHANTABLE).add(
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get()
        );
        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get(),
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_SHOVEL.get(),
                ModItems.GRAVEDIGGER.get(),
                ModItems.DEMON_BREAKING_SILVER_HUNTER.get(),
                ModItems.MINIGUN.get(),
                ModItems.HKMP7.get(),
                ModItems.FISH_HOE.get()
        );
        this.tag(ItemTags.VANISHING_ENCHANTABLE).add(
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_AXE.get(),
                ModItems.LUCY_AXE.get(),
                ModItems.PIRATE_SCIMITAR.get(),
                ModItems.HELL_FIRE.get(),
                ModItems.FISH_PICKAXE.get(),
                ModItems.FISH_SHOVEL.get(),
                ModItems.GRAVEDIGGER.get(),
                ModItems.FISH_HOE.get()
        );

    }
}
