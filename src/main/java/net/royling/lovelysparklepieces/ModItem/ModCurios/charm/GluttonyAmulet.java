package net.royling.lovelysparklepieces.ModItem.ModCurios.charm;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.royling.lovelysparklepieces.ClientEvent.ColorUtil;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModCurios.UniversalCurio;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GluttonyAmulet extends UniversalCurio {
    public GluttonyAmulet(Properties properties) {
        super(properties.stacksTo(1));
    }
    private static final float EATING_SPEED_MULTIPLIER = 1.5f;

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.level4").withColor(ColorUtil.getRainbow(2)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gluttony.des").withColor(ColorUtil.getRainbow(2)));
        tooltipComponents.add(Component.translatable("tooltip.lovely_sparkle_pieces.gluttony.des1").withColor(ColorUtil.getRainbow(2)));
    }

    @EventBusSubscriber(modid = LovelySparklePieces.MODID,bus = EventBusSubscriber.Bus.GAME)
    public static class CluttonyAmuletHandler{
        private static final Map<UUID, Integer> originalUseTimes = new HashMap<>();
        @SubscribeEvent
        public static void onStartUsingItem(LivingEntityUseItemEvent event){
            if(!(event.getEntity()instanceof Player player))return;
            if(event.getItem().getItem().getFoodProperties(event.getItem(),player)==null)return;
            if(event.getItem().is(ModItems.INFINITE_APPLE.get())){
                event.setDuration(16);
            }else {
                if (!ModCurios.hasCurio(player, ModCurios.GLUTTONY_AMULET.get())) return;
                int original = event.getDuration();
                event.setDuration(original - 2);
            }
        }
    }

}
