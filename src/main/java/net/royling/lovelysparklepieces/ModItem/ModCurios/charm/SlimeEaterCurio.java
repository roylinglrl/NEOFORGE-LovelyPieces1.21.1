package net.royling.lovelysparklepieces.ModItem.ModCurios.charm;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;

import java.util.List;

public class SlimeEaterCurio extends UniversalCurio {
    public SlimeEaterCurio(Properties properties) {
        super(properties.stacksTo(1));
    }
    private static final int COOLDOWN_TICKS = 20;
    private static final int SATURATION = 4;

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level3"));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.slime_eater.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.slime_eater.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.slime_eater.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.slime_eater.des3").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.slime_eater.des4").withColor(ColorUtil.getRainbow(2f)));
    }

    @EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.GAME)
    public static class SlimeEaterEvent{
        @SubscribeEvent
        public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event){
            Player player = event.getEntity();
            Entity entity = event.getTarget();
            if (!(entity instanceof Slime slime)) return;
            if (!ModCurios.hasCurio(player, ModCurios.SLIME_EATER.get())) return;
            if(player.level().isClientSide){
                if (player.getCooldowns().isOnCooldown(ModCurios.SLIME_EATER.get())) return;
                player.level().playSound(player,player.blockPosition(),SoundEvents.PLAYER_BURP,player.getSoundSource(),1f,1f);
                player.level().playSound(player,player.blockPosition(),SoundEvents.SLIME_SQUISH,player.getSoundSource(),1f,1f);
                for (int i = 0; i < 8; i++) {
                    double dx = (player.getRandom().nextDouble() - 0.5) * 0.8;
                    double dy = (player.getRandom().nextDouble() - 0.5) * 0.8;
                    double dz = (player.getRandom().nextDouble() - 0.5) * 0.8;
                    if(slime.getSize()>1)
                        player.level().addParticle(ParticleTypes.ITEM_SLIME,false,slime.getX(),slime.getY()+slime.getBbHeight(),slime.getZ(),dx,dy,dz);
                    else
                        player.level().addParticle(ParticleTypes.HEART,false,slime.getX(),slime.getY()+slime.getBbHeight(),slime.getZ(),dx,dy,dz);

                }
            }else {
                if (!player.getItemInHand(event.getHand()).isEmpty()) return;
                if (!ModCurios.hasCurio(player, ModCurios.SLIME_EATER.get())) return;
                if (player.getCooldowns().isOnCooldown(ModCurios.SLIME_EATER.get())) return;
                consumeSlime(player, slime);
                player.getCooldowns().addCooldown(ModCurios.SLIME_EATER.get(), 20);
                event.setCanceled(true);
            }
        }
        @SubscribeEvent
        public static void onSlimeSetTarget(LivingChangeTargetEvent event){
            if(!(event.getEntity()instanceof Slime slime))return;
            if(!(event.getNewAboutToBeSetTarget() instanceof Player player))return;
            if(ModCurios.hasCurio(player,ModCurios.SLIME_EATER.get())|| ModCurios.hasCurio(player,ModCurios.MYSTERIOUS_GEL.get())){
                slime.setTarget(null);
                event.setCanceled(true);
                if (slime.getRandom().nextFloat() < 0.3f) {
                    slime.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.5F);
                }            }
        }
        @SubscribeEvent
        public static void onPlayerHurt(LivingIncomingDamageEvent event){
            if(!(event.getEntity() instanceof Player player))return;
            if(!(event.getSource().getEntity()instanceof Slime slime))return;
            if(ModCurios.hasCurio(player,ModCurios.SLIME_EATER.get())|| ModCurios.hasCurio(player,ModCurios.MYSTERIOUS_GEL.get())){
                event.setCanceled(true);
            }
        }
    }
    private static void consumeSlime(Player player,Slime slime){
        int newSize = slime.getSize()-1;
        if(newSize <=0) {
            slime.remove(Entity.RemovalReason.KILLED);
            ItemEntity drop = new ItemEntity(
                    slime.level(),
                    slime.getX(),
                    slime.getY() + slime.getBbHeight()/2,
                    slime.getZ(),
                    new ItemStack(Items.SLIME_BALL)
            );
            slime.level().addFreshEntity(drop);
        }else {
            slime.setSize(newSize,true);
        }
        player.getFoodData().eat(SATURATION,0.8F);
    }
}
