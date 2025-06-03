package net.royling.lovelysparklepieces.ModAttributes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.royling.lovelysparklepieces.LovelySparklePieces;

import java.util.function.Supplier;

public class ModAttribute {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, LovelySparklePieces.MODID);
    public static final Holder<Attribute> CRIT_CHANCE = ATTRIBUTES.register("critical_hit_chance",
            ()->new RangedAttribute("attribute.lsp.critical_hit_chance",0d,0d,1d).setSyncable(true));
    public static final Holder<Attribute> DAMAGE_MODIFIER = ATTRIBUTES.register("damage_modifier",
            ()->new RangedAttribute("attribute.lsp.damage_modifier",1d,0d,2147483648d).setSyncable(true));
    public static final Holder<Attribute> MAGIC_DAMAGE_MODIFIER = ATTRIBUTES.register("magic_damage_modifier",
            ()->new RangedAttribute("attribute.lsp.magic_damage_modifier",1d,0d,2147483648d).setSyncable(true));

}
