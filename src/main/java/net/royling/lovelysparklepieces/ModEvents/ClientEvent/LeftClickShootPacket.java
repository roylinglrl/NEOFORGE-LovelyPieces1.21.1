package net.royling.lovelysparklepieces.ModEvents.ClientEvent;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.Gun.GunItem;

public record LeftClickShootPacket(InteractionHand hand) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "left_click_shoot");
    public static final CustomPacketPayload.Type<LeftClickShootPacket> TYPE =
            new CustomPacketPayload.Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, LeftClickShootPacket> STREAM_CODEC = StreamCodec.of(
            LeftClickShootPacket::encode, LeftClickShootPacket::decode
    );
    private static void encode(RegistryFriendlyByteBuf buf, LeftClickShootPacket packet) {
        buf.writeEnum(packet.hand());
    }
    private static LeftClickShootPacket decode(RegistryFriendlyByteBuf buf) {
        return new LeftClickShootPacket(buf.readEnum(InteractionHand.class));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handle(LeftClickShootPacket leftClickShootPacket, IPayloadContext context) {
        // 确保在服务器线程中执行
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            // 调用 Makarov 物品的射击逻辑
            if (sender.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof GunItem gunItem) {
                // 在这里调用 Makarov 的射击方法
                // 由于 use 方法是 InteractionResultHolder，我们可能需要一个独立的射击方法
                gunItem.performShoot(sender.level(), sender, InteractionHand.MAIN_HAND, sender.getItemInHand(InteractionHand.MAIN_HAND));
            }
        });
    }
}
