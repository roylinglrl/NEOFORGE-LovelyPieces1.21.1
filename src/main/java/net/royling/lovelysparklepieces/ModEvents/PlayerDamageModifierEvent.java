package net.royling.lovelysparklepieces.ModEvents;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ClientEvent.DamageParticlePacket;
import net.royling.lovelysparklepieces.ClientEvent.PlayerSoul;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModConfigs.LSPConfig;
import net.royling.lovelysparklepieces.ModEffect.ModMobEffects;
import net.royling.lovelysparklepieces.ModItem.ModCurios.Group.Set.*;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.PlayerData.ChipsData;
import net.royling.lovelysparklepieces.PlayerData.TemperatureData;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public class PlayerDamageModifierEvent {

    private static final Map<TagKey<DamageType>, String> DAMAGE_TYPE_MAP = Map.ofEntries(
            entry(DamageTypeTags.IS_FIRE, "fire"),
            entry(DamageTypeTags.WITCH_RESISTANT_TO, "magic"),
            entry(DamageTypeTags.IS_PROJECTILE, "arrow"),
            entry(DamageTypeTags.IS_FALL, "fall"),
            entry(DamageTypeTags.IS_LIGHTNING, "thunder"),
            entry(DamageTypeTags.IS_EXPLOSION, "explosion"),
            entry(DamageTypeTags.IS_FREEZING, "frozen"),
            entry(TagKey.create(Registries.DAMAGE_TYPE,ResourceLocation.withDefaultNamespace("is_silver")),"silver")
    );
    private static final Map<TagKey<DamageType>, String> DAMAGE_TYPE_PRIORITY_MAP = new LinkedHashMap<>();
    static {
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_FIRE, "fire");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.WITCH_RESISTANT_TO, "magic");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_PROJECTILE, "arrow");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_FALL, "fall");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_LIGHTNING, "thunder");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_EXPLOSION, "explosion");
        DAMAGE_TYPE_PRIORITY_MAP.put(TagKey.create(Registries.DAMAGE_TYPE,ResourceLocation.withDefaultNamespace("is_silver")), "silver");
        DAMAGE_TYPE_PRIORITY_MAP.put(DamageTypeTags.IS_FREEZING, "frozen");
        // ... 你可以根据需要添加更多标签和优先级
    }
    public static String getDamageTypeString(DamageSource damageType) {
        // 遍历优先级Map
        for (Map.Entry<TagKey<DamageType>, String> entry : DAMAGE_TYPE_PRIORITY_MAP.entrySet()) {
            if (damageType.is(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "attack";
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        // 获取造成伤害的实体（来源实体和直接实体）
        Entity source = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();
        LivingEntity target = event.getEntity();
        // 判断是否由玩家造成伤害
        Player player = null;
        if (source instanceof Player) {
            player = (Player) source;
        } else if (direct instanceof Player) {
            player = (Player) direct;
        }
        // 初始化倍率，用于最终粒子效果显示
        double magnification = 1.0;

        if (player != null) {
            // 获取初始伤害值
            double baseDamage = event.getNewDamage();
            double multiple = 0;
            //熔火核心
            if (ModCurios.hasCurio(player, ModCurios.BLAZE_CORE.get())) {
                if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                    if (TemperatureData.gettemperatures(player) < 50) {
                        multiple += 0.15;
                    } else if (TemperatureData.gettemperatures(player) < 100) {
                        multiple += 0.25;
                    } else {
                        multiple += 0.5;
                    }
                    if (player.hasEffect(ModMobEffects.OVERHEAT_EFFECT)) {
                        baseDamage = 0;
                    }
                    if(player.getEffect(ModMobEffects.OVERHEAT_EFFECT)==null)
                        TemperatureData.addTemperature(player, 10);
                }
                else if(player.hasEffect(ModMobEffects.OVERHEAT_EFFECT)&&!event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)){
                    DamageSource damageSource = player.level().damageSources().indirectMagic(player,player);
                    event.getEntity().hurt(damageSource,2);
                    event.getEntity().invulnerableTime=0;
                }
            }
            //皮革箭袋
            if (ModCurios.hasCurio(player, ModCurios.LEATHER_QUIVER.get())) {
                if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                    multiple += 0.08;
                }
            }
            if (ModCurios.hasCurio(player, ModCurios.WOOD_GRAIN_QUIVER.get())) {
                if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                    multiple += 0.1;
                }
            }
            if (ModCurios.hasCurio(player, ModCurios.SOUL_QUIVER.get())) {
                if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                    multiple += 0.2;
                }
            }
            //神射手护目镜
            if (ModCurios.hasCurio(player, ModCurios.MARKSMAN_GOGGLES.get())) {
                if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                    multiple += 0.1;
                }
            }
            //女巫帽子
            if (ModCurios.hasCurio(player, ModCurios.WITCH_HAT.get())) {
                if (event.getSource().is(DamageTypeTags.WITCH_RESISTANT_TO)) {
                    multiple += 0.1;
                }
            }
            //黄巾
            if (ModCurios.hasCurio(player, ModCurios.YELLOW_HEADSCARF.get())) {
                if (event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
                    multiple += 0.1;
                }
            }
            baseDamage *= (1 + multiple);

            //减速
            if(ModCurios.hasCurio(player,ModCurios.UFFFD.get())){
                if(event.getSource().is(DamageTypes.PLAYER_ATTACK)){
                    if(player.getRandom().nextDouble()<0.25){
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,35,100));
                    }
                }
            }

            // 获取“增伤”属性并进行伤害倍率计算
            AttributeInstance dmgAttr = player.getAttribute(ModAttribute.DAMAGE_MODIFIER);
            if (dmgAttr != null) {
                baseDamage *= dmgAttr.getValue();
            }
            // 获取“暴击率”属性，进行暴击概率判断
            AttributeInstance critAttr = player.getAttribute(ModAttribute.CRIT_CHANCE);
            boolean isCrit = critAttr != null && player.level().random.nextDouble() < critAttr.getValue();
            if (isCrit) {
                // 暴击成功：计算暴击倍率
                double critMultiplier = ModCurios.hasCurio(player, ModCurios.BLASPHEMOUS_CONTRACT.get()) ? 1.5 : 1.75;

                // 如果装备了“赌徒的胸花”，进行暴击加成浮动判定
                if (ModCurios.hasCurio(player, ModCurios.GAMBLERS_CORSAGE.get())) {
                    int chips = ChipsData.getChips(player);
                    double chance = chips < 2 ? 0.5 : 0.75;
                    // 消耗筹码前先检查是否处于某特殊状态（防止多次扣除）
                    if (chips >= 2 && !player.getPersistentData().getBoolean("gambler_5effect")) {
                        ChipsData.removeChip(player, 2);
                    }
                    // 根据概率微调暴击倍率（+0.25 或 -0.25）
                    boolean bonus = player.level().random.nextFloat() < chance;
                    critMultiplier += bonus ? 0.25 : -0.25;
                }

                // 应用最终暴击伤害值
                event.setNewDamage((float) (baseDamage * critMultiplier));
                magnification = critMultiplier;

                // TODO：这里可以添加暴击提示（粒子、音效等）
            } else {
                // 没有暴击，仅使用增伤后数值
                event.setNewDamage((float) baseDamage);
            }

            //银伤害对亡灵增伤50%
            if(event.getSource().is(TagKey.create(Registries.DAMAGE_TYPE,ResourceLocation.withDefaultNamespace("is_silver")))){
                System.out.println("is Silver Damage");
                if(event.getEntity()instanceof LivingEntity entity && entity.getType().is(EntityTypeTags.UNDEAD)) {
                    System.out.println("Is Undead");
                    event.setNewDamage(event.getNewDamage()*1.50f);
                }
            }

            if (LSPConfig.IS_DAMAGE_NUM.get())
            {
                // 如果是服务端玩家，发送伤害粒子包用于客户端显示
                if (player instanceof ServerPlayer serverPlayer) {
                    // 获取目标位置
                    Vec3 pos = event.getEntity().position();
                    // 根据伤害类型标签映射到对应的展示名称
                    String damageType = DAMAGE_TYPE_MAP.entrySet().stream()
                            .filter(entry -> event.getSource().is(entry.getKey()))
                            .map(Map.Entry::getValue)
                            .findFirst()
                            .orElse("attack");
                    String damagetype = getDamageTypeString(event.getSource());
                    // 发送自定义粒子数据包
                    PacketDistributor.sendToPlayer(serverPlayer, new DamageParticlePacket(
                            damagetype,
                            event.getNewDamage(),
                            pos.x,
                            pos.y + event.getEntity().getBbHeight(),
                            pos.z,
                            magnification
                    ));
                }
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if(event.getEntity().level().isClientSide)return;
        Player player = event.getEntity();
        List<ItemStack> equipped = getAllCurioItems(player);
        SetBonusManager.tick(player, SetBonusRegistry.ALL, equipped);
    }
    public static List<ItemStack> getAllCurioItems(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> {
                    List<ItemStack> items = new ArrayList<>();
                    handler.getCurios().keySet().forEach(slotId -> {
                        handler.getStacksHandler(slotId).ifPresent(stacks -> {
                            for (int i = 0; i < stacks.getSlots(); i++) {
                                ItemStack stack = stacks.getStacks().getStackInSlot(i);
                                if (!stack.isEmpty()) {
                                    items.add(stack);
                                }
                            }
                        });
                    });
                    return items;
                })
                .orElse(Collections.emptyList());
    }
}
