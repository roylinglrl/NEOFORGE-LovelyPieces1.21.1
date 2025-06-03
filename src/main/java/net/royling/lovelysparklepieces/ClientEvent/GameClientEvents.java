package net.royling.lovelysparklepieces.ClientEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.GunItem;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = LovelySparklePieces.MODID,bus= EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class GameClientEvents {
    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event){
        if(event.getPlayer().isUsingItem()&&event.getPlayer().getUseItem().getItem() == ModItems.FLAME_STAFF.get()){
            float fovModifier = 1f;
            int tickUsing = event.getPlayer().getTicksUsingItem();
            float deltaTick = (float) tickUsing/20f;
            if(deltaTick>1f){
                deltaTick=1f;
            }else {
                deltaTick *= deltaTick;
            }
            fovModifier *= 1f-deltaTick*0.15f;
            event.setNewFovModifier(fovModifier);
        }
    }
    @SubscribeEvent
    public static void onComputeFovModifierBinoculars(ComputeFovModifierEvent event){
        if(event.getPlayer().isUsingItem()&&event.getPlayer().getUseItem().getItem() == ModItems.BINOCULARS.get()){
            event.setNewFovModifier(0);
        }
    }

    public static boolean isLeftButton = false;
    @SubscribeEvent
    public static void onMouseButton(InputEvent.MouseButton.Post event){
        if(Minecraft.getInstance().player == null)return;
        if(event.getButton() == 0){
            isLeftButton = event.getAction() == GLFW.GLFW_PRESS;
        }
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event){
        if(!event.isAttack())return;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        // **新增：如果玩家打开了任何 GUI 界面，则不执行射击逻辑**
        if (minecraft.screen != null) {
            return;
        }
        ItemStack mainHand = player.getMainHandItem();
        if(mainHand.getItem()instanceof GunItem && isLeftButton){
            player.stopUsingItem();
            event.setSwingHand(false);
            event.setCanceled(true);
            PacketDistributor.sendToServer(new LeftClickShootPacket(InteractionHand.MAIN_HAND));
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event){
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if(player == null) return;
        // **新增：如果玩家打开了任何 GUI 界面，则不执行射击逻辑**
        if (minecraft.screen != null) {
            return;
        }

        ItemStack mainHandItem = player.getMainHandItem();
        if(mainHandItem.getItem() instanceof GunItem gunItem){
            if (isLeftButton && gunItem.isAuto()) {
                player.stopUsingItem();
                PacketDistributor.sendToServer(new LeftClickShootPacket(InteractionHand.MAIN_HAND));
            }
        }

    }
}
