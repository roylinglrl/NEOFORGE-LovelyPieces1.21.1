package net.royling.lovelysparklepieces.ModEvents.ClientEvent;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Binoculars;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = LovelySparklePieces.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ModClientEvents {
    public static final KeyMapping OPEN_ENDER_CHEST = new KeyMapping(
            "key.lsp.open_ender_chest",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "key.categories.lsp"
    );
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(OPEN_ENDER_CHEST);
    }
    @EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        private static int initDelay = 0;
        private static Double originalSensitivity = -1d;
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            if (initDelay <= 40) {
                initDelay += 1;
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.getConnection() == null) return;
            long fps = Math.max(1, mc.getFps());
            double maxMemoryGB = Runtime.getRuntime().maxMemory() / 1024.0 / 1024 / 1024;
            PacketDistributor.sendToServer(new PlayerFps(fps, maxMemoryGB));
            initDelay = 0;
            // 原望远镜灵敏度逻辑
            Player player = mc.player;
            if (player == null) return;
            boolean isUsingBinoculars = player.isUsingItem() &&
                    player.getUseItem().getItem() instanceof Binoculars;
            if (isUsingBinoculars) {
                if (originalSensitivity < 0) {
                    originalSensitivity = mc.options.sensitivity().get();
                }
                mc.options.sensitivity().set(originalSensitivity * 0.2F);
            } else if (originalSensitivity > 0) {
                mc.options.sensitivity().set(originalSensitivity);
                originalSensitivity = -1d;
            }
        }
        @SubscribeEvent
        public static void onInput(InputEvent.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;
            if (OPEN_ENDER_CHEST.consumeClick()) {
                PacketDistributor.sendToServer(new OpenChest());
            }
            if (!mc.player.onGround() && mc.options.keyJump.isDown()) {
                PacketDistributor.sendToServer(new DoubleJump());
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerOverlays(RegisterGuiLayersEvent event) {
        registerOverlay(event, "binoculars", ModItems.BINOCULARS.get(),
                "textures/gui/binoculars.png", true);
        registerNightVisionOverlay(event, "night_version", ModCurios.NIGHT_VISION.get());
        registerNightVisionOverlay(event, "double_night_version", ModCurios.DOUBLE_NIGHT_VISION.get());
        registerNightVisionOverlay(event, "quater_night_version", ModCurios.QUARTER_NIGHT_VISION.get());
    }
    private static void registerNightVisionOverlay(
            RegisterGuiLayersEvent event,
            String overlayId,
            Item curioItem
    ) {
        event.registerBelowAll(
                ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, overlayId),
                (gui, delta) -> {
                    Minecraft mc = Minecraft.getInstance();
                    Player player = mc.player;
                    if (player != null && ModCurios.hasCurio(player, curioItem)) {
                        renderCommonOverlay(gui, "textures/gui/" + overlayId + ".png");
                    }
                }
        );
    }
    private static void renderCommonOverlay(GuiGraphics gui, String texturePath) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Window window = Minecraft.getInstance().getWindow();
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, texturePath);
        gui.blit(
                texture,
                0, 0,
                window.getGuiScaledWidth(), window.getGuiScaledHeight(),
                0, 0,
                window.getGuiScaledWidth(), window.getGuiScaledHeight(),
                window.getGuiScaledWidth(), window.getGuiScaledHeight()
        );
        RenderSystem.disableBlend();
    }
    private static void registerOverlay(
            RegisterGuiLayersEvent event,
            String overlayId,
            Item targetItem,
            String texturePath,
            boolean registerAbove
    ) {
        if (registerAbove) {
            event.registerAboveAll(
                    ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, overlayId),
                    (gui, delta) -> {
                        Minecraft mc = Minecraft.getInstance();
                        Player player = mc.player;
                        if (player != null &&
                                player.isUsingItem() &&
                                player.getUseItem().getItem() == targetItem
                        ) {
                            renderCommonOverlay(gui, texturePath);
                        }
                    }
            );
        }
    }

}
