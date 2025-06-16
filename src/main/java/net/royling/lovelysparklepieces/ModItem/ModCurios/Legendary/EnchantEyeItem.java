package net.royling.lovelysparklepieces.ModItem.ModCurios.Legendary;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModEvents.Legendarys.BCEvents;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class EnchantEyeItem extends UniversalCurio {
    public EnchantEyeItem(Properties properties) {
        super(properties.rarity(Rarity.RARE).stacksTo(1));
    }
    public static boolean hasEye(Player player) {
        return CuriosApi.getCuriosInventory(player).flatMap(inv->inv.findFirstCurio(ModCurios.ENCHANT_EYE.get())).isPresent();
    }

    @Override @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level10"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.require")
                .withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.bonus")
                .withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.cost")
                .withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.unique")
                .withColor(ColorUtil.getRainbow()));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.unreliable")
                .withColor(ColorUtil.getRainbow()));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player && BCEvents.hasBlasphemousContract(player) && !ModCurios.hasCurio(player,this);
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
}
