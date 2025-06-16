package net.royling.lovelysparklepieces.ModItem.ModUsingItem;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;
import net.royling.lovelysparklepieces.ModEntity.Butterfly.SoulButterflyEntity;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

import java.util.List;

public class NecropsychePapillonItem extends Item {
    public NecropsychePapillonItem(Properties properties) {
        super(properties.stacksTo(1)
                .rarity(Rarity.EPIC)
                .durability(576)

        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if(level.isClientSide)return InteractionResultHolder.success(player.getItemInHand(usedHand));
        if(player.isShiftKeyDown()){
            clearPlayerButterflies(player);
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), false);
        }
        if(!ModCurios.hasCurio(player,ModCurios.BLASPHEMOUS_CONTRACT.get())){
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand),false);
        }
        int butterflyCount = SoulButterflyEntity.getButterflyCountByOwner(player);
        int toSpawn = Math.min(3, 3 - butterflyCount);

        if(toSpawn<=0){
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), false);
        }
        if(!player.isCreative())
            player.getItemInHand(usedHand).hurtAndBreak(1,player, EquipmentSlot.MAINHAND);
        for (int i = 0; i < toSpawn; i++) {
            SoulButterflyEntity soulButterflyEntity = ModEntities.BUTTERFLY.get().create(level);
            if (soulButterflyEntity != null) {
                double x = player.getX() + level.random.nextGaussian();
                double y = player.getY() + 1.0;
                double z = player.getZ() + level.random.nextGaussian();
                soulButterflyEntity.moveTo(x, y, z, player.getYRot(), 0.0F);
                soulButterflyEntity.setOwner(player);
                level.addFreshEntity(soulButterflyEntity);
            }
        }
        player.getCooldowns().addCooldown(this,40);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand),level.isClientSide());
    }
    // 清除玩家所有魂蝶
    private void clearPlayerButterflies(Player player) {
        if (player.level() instanceof ServerLevel) {
            // 重置玩家计数
            SoulButterflyEntity.resetCountForPlayer(player);

            // 遍历所有维度清除魂蝶
            MinecraftServer server = player.getServer();
            if (server == null) return;

            for (ServerLevel level : server.getAllLevels()) {
                for (SoulButterflyEntity butterfly : level.getEntitiesOfClass(
                        SoulButterflyEntity.class,
                        player.getBoundingBox().inflate(1000) // 大范围搜索
                )) {
                    if (butterfly.getOwner() != null &&
                            butterfly.getOwner().getUUID().equals(player.getUUID())) {
                        butterfly.discard();
                    }
                }
            }
        }
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level12").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.need.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp1").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp6").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp4").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.np_desp5").withColor(ColorUtil.getRainbow()));

    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 22;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

}
