package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEvents.Legendarys.BCEvents;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class SoulMarkItem extends UniversalCurio {
    public SoulMarkItem(Properties properties) {
        super(properties.stacksTo(1).durability(32));
    }
    public static final int COOLDOWN_TICKS = 300*20;
    public static final int EFFECT_DURATION = 30 * 20;
    public static void applyProtectionEffect(Player player){
        player.level().playSound(null,player.getX(),player.getY(),player.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS,1.0F,1.0F);

        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, EFFECT_DURATION, 1));
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, EFFECT_DURATION, 0));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, EFFECT_DURATION, 0));

        ItemStack curioStack = CuriosApi.getCuriosInventory(player).flatMap(
                curiosInventory -> curiosInventory.findFirstCurio(ModCurios.SOUL_MARK.get())).get().stack();
        curioStack.hurtAndBreak(1,player, EquipmentSlot.MAINHAND);
        player.getCooldowns().addCooldown(curioStack.getItem(),COOLDOWN_TICKS);
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this) && ModCurios.hasCurio(player,ModCurios.BLASPHEMOUS_CONTRACT.get());
        }
        return true;
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            if(!BCEvents.hasBlasphemousContract(player)){
                CuriosApi.getCuriosInventory(player).ifPresent(curios->{
                    curios.getStacksHandler(slotContext.identifier()).ifPresent(handler->{
                        handler.getStacks().setStackInSlot(slotContext.index(),ItemStack.EMPTY)
                        ;        player.level().addFreshEntity(new ItemEntity(
                                player.level(),
                                player.getX(),
                                player.getY() + 0.5, // 避免卡在方块内
                                player.getZ(),
                                stack.copy()));
                    });
                });
            }
        }
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level8").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.need.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.soul_mark.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.soul_mark.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.soul_mark.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.soul_mark.des3").withColor(ColorUtil.getRainbow(2f)));
    }
}
