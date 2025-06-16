package net.royling.lovelysparklepieces.ModEnchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModEnchantment.weapon.LightStrikerEnchantmentEffect;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> LIGHT_STRIKER =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"lightning_striker"));

    public static final ResourceKey<Enchantment> MAGIC_BLADE =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID,"magic_blade"));


    public static void bootstrap(BootstrapContext<Enchantment> context){
        var enchantmentHolderGetter = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);
        register(context,LIGHT_STRIKER,Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                6,5,
                Enchantment.dynamicCost(10,6),
                Enchantment.dynamicCost(18,7),
                2,
                EquipmentSlotGroup.MAINHAND))
                        .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                        .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
                                EnchantmentTarget.VICTIM,new LightStrikerEnchantmentEffect())
        );
        register(context,MAGIC_BLADE,Enchantment.enchantment(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                        2,1,
                        Enchantment.dynamicCost(18,7),
                        Enchantment.dynamicCost(29,7),
                        8,
                        EquipmentSlotGroup.MAINHAND))
                .withEffect(EnchantmentEffectComponents.POST_ATTACK, EnchantmentTarget.ATTACKER,
                        EnchantmentTarget.VICTIM,new LightStrikerEnchantmentEffect())
        );
    }

    public static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> resourceKey,
                                Enchantment.Builder builder){
        registry.register(resourceKey,builder.build(resourceKey.location()));
    }
}
