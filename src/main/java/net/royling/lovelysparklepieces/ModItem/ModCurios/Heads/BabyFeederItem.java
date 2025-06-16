package net.royling.lovelysparklepieces.ModItem.ModCurios.Heads;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.royling.lovelysparklepieces.ModEvents.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.FlintlockBulletItem;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Objects;

public class BabyFeederItem extends UniversalCurio {
    private static final int CHECK_INTERVAL = 120;
    public BabyFeederItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity()instanceof Player player){
            if(player.level().isClientSide)return;
            if(player.tickCount%CHECK_INTERVAL!=0)return;
            if(player.isCreative())return;
            int currentHunger = player.getFoodData().getFoodLevel();
            int maxHunger = 20;
            int lesHunger = maxHunger-currentHunger;
            if(lesHunger<=0)return;
            ItemStack foodToConsume = findFoodInInventory(player);
            int food = Objects.requireNonNull(foodToConsume.getFoodProperties(player)).nutrition();
            if(lesHunger>=food){
                foodToConsume.shrink(1);
                player.getFoodData().eat(Objects.requireNonNull(foodToConsume.getFoodProperties(player)));
                if(!foodToConsume.getFoodProperties(player).usingConvertsTo().isEmpty()){
                    player.getInventory().add(new ItemStack(foodToConsume.getFoodProperties(player).usingConvertsTo().get().getItem()));
                }
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        foodToConsume.getItem().getEatingSound(), player.getSoundSource(), 1.0F, 1.0F + (player.level().random.nextFloat() - player.level().random.nextFloat()) * 0.4F);

            }
        }
    }
    private static ItemStack findFoodInInventory(Player player) {
        // 优先搜索快捷栏 (0-8)
        // 优先检查快捷栏 (0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getFoodProperties(player)!=null) {
                return stack;
            }
        }
        for (int i = 9; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getFoodProperties(player)!=null) {
                return stack;
            }
        }
        return ItemStack.EMPTY; // 未找到食物
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level6").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.baby_feeder.des").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.baby_feeder.des1").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.baby_feeder.des2").withColor(ColorUtil.getRainbow(2f)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.baby_feeder.des3").withColor(ColorUtil.getRainbow(2f)));
    }
}
