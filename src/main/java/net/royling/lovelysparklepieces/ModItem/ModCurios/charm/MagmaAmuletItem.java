package net.royling.lovelysparklepieces.ModItem.ModCurios.charm;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.PlayerLavadef;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class MagmaAmuletItem extends UniversalCurio {
    public MagmaAmuletItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity().level().isClientSide)return;
        if(slotContext.entity() instanceof Player player){
            if(player.getPersistentData().get("lsp_lava_def")==null){
                player.getPersistentData().putInt("lsp_lava_def",0);
                PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));
            }
            else if(player.isInLava()||player.isOnFire()){
                player.getPersistentData().putInt("lsp_lava_def",player.getPersistentData().getInt("lsp_lava_def")-1);
                if(player.getPersistentData().getInt("lsp_lava_def")>0){
                    player.setRemainingFireTicks(0);
                }
                if(player.getPersistentData().getInt("lsp_lava_def")<0){
                    player.getPersistentData().putInt("lsp_lava_def",0);
                }
                PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));
            }else {
                player.getPersistentData().putInt("lsp_lava_def",Math.min(140,player.getPersistentData().getInt("lsp_lava_def")+1));
                PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));
            }
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            player.getPersistentData().putInt("lsp_lava_def",0);
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            player.getPersistentData().remove("lsp_lava_def");
            PacketDistributor.sendToPlayer((ServerPlayer) player,new PlayerLavadef(player.getPersistentData().getInt("lsp_lava_def")));
        }
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level3").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.magna.des").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.magna.des2").withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.magna.des3").withColor(ColorUtil.getRainbow()));

    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player){
            return !ModCurios.hasCurio(player,this);
        }
        return true;
    }
}
