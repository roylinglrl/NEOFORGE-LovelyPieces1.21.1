package net.royling.lovelysparklepieces;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.royling.lovelysparklepieces.ClientEvent.*;
import net.royling.lovelysparklepieces.ClientEvent.Particle.DamageNumberParticle;
import net.royling.lovelysparklepieces.ClientEvent.Particle.ModParticles;
import net.royling.lovelysparklepieces.ModAttributes.ModAttribute;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModCommand.ModCommands;
import net.royling.lovelysparklepieces.ModConfigs.LSPConfig;
import net.royling.lovelysparklepieces.ModCreative.ModCreative;
import net.royling.lovelysparklepieces.ModEffect.ModMobEffects;
import net.royling.lovelysparklepieces.ModEntity.ModEntities;
import net.royling.lovelysparklepieces.ModEvents.Gamblers.GamblersEvents;
import net.royling.lovelysparklepieces.ModEvents.HeartSystem;
import net.royling.lovelysparklepieces.ModEvents.Legendarys.*;
import net.royling.lovelysparklepieces.ModEvents.necklace.LavaDefance;
import net.royling.lovelysparklepieces.ModItem.ModCurios.ModCurios;

import net.royling.lovelysparklepieces.ModEvents.PlayerDamageModifierEvent;
import net.royling.lovelysparklepieces.ModEvents.PlayerJoinHandler;
import net.royling.lovelysparklepieces.ModEvents.boot.DoubleJumpEvent;
import net.royling.lovelysparklepieces.ModEvents.boot.SteelBoot;
import net.royling.lovelysparklepieces.ModItem.ModCuriosRender.MagmaAmuletRender;
import net.royling.lovelysparklepieces.ModItem.ModCuriosRender.WitchHatRender;
import net.royling.lovelysparklepieces.ModItem.ModDataComponents.ModDataComponents;
import net.royling.lovelysparklepieces.ModItem.ModUsingItem.ModItems;
import net.royling.lovelysparklepieces.ModSounds.ModSounds;
import net.royling.lovelysparklepieces.PlayerData.ChipsData;
import net.royling.lovelysparklepieces.PlayerData.SoulData;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(LovelySparklePieces.MODID)
public class LovelySparklePieces
{
    public static final String MODID = "lovely_sparkle_pieces";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation resLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public LovelySparklePieces(IEventBus modEventBus, ModContainer modContainer)
    {
        modContainer.registerConfig(ModConfig.Type.COMMON,LSPConfig.SPEC);
        modEventBus.addListener(this::commonSetup);
        ModCurios.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreative.CREATIVE_MODE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::registerNetwork);
        ModAttribute.ATTRIBUTES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModParticles.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModMobEffects.MOB_EFFECTS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModCurios.STRANGE_ITEMS.register(modEventBus);

        NeoForge.EVENT_BUS.register(PlayerJoinHandler.class);
        NeoForge.EVENT_BUS.register(SoulData.class);
        NeoForge.EVENT_BUS.register(ChipsData.class);
        NeoForge.EVENT_BUS.register(PlayerDamageModifierEvent.class);
        NeoForge.EVENT_BUS.register(BCEvents.class);
        NeoForge.EVENT_BUS.register(GamblersEvents.class);
        NeoForge.EVENT_BUS.register(BowHandler.class);
        NeoForge.EVENT_BUS.register(SteelBoot.class);
        NeoForge.EVENT_BUS.register(DoubleJumpEvent.class);
        NeoForge.EVENT_BUS.register(LavaDefance.class);
        NeoForge.EVENT_BUS.register(HeartSystem.class);
        NeoForge.EVENT_BUS.register(PlayerDeadEvent.class);
        NeoForge.EVENT_BUS.register(EmberCoreEvent.class);

    }
    private void registerNetwork(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID)
                .versioned("1.0");
                registrar.playToServer(OpenChest.TYPE, OpenChest.STREAM_CODEC, OpenChest::handle);
                registrar.playToServer(DoubleJump.TYPE, DoubleJump.STREAM_CODEC, DoubleJump::handle);
                registrar.playToServer(PlayerFps.TYPE,PlayerFps.STREAM_CODEC,PlayerFps::handle);
                registrar.playToClient(PlayerSoul.TYPE,PlayerSoul.STREAM_CODEC,PlayerSoul::handle);
                registrar.playToClient(PlayerChip.TYPE,PlayerChip.STREAM_CODEC,PlayerChip::handle);
                registrar.playToClient(PlayerHeart.TYPE,PlayerHeart.STREAM_CODEC,PlayerHeart::handle);
                registrar.playToClient(PlayerUsingItemData.TYPE,PlayerUsingItemData.STREAM_CODEC,PlayerUsingItemData::handle);
                registrar.playToClient(PlayerTemperature.TYPE,PlayerTemperature.STREAM_CODEC,PlayerTemperature::handle);
                registrar.playToClient(DamageParticlePacket.TYPE,DamageParticlePacket.STREAM_CODEC,DamageParticlePacket::handle);
                registrar.playToClient(PlayerLavadef.TYPE,PlayerLavadef.STREAM_CODEC,PlayerLavadef::handle);
                registrar.playToServer(LeftClickShootPacket.TYPE,LeftClickShootPacket.STREAM_CODEC,LeftClickShootPacket::handle);
    }
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event){
        ModCommands.register(event.getDispatcher());
    }
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(()->{
                Minecraft.getInstance().particleEngine.register(
                        ModParticles.DAMAGE_NUMBER_PARTICLE.get(), new DamageNumberParticle.Provider()
                );
            });
            CuriosRendererRegistry.register(ModCurios.MAGMA_AMULET.get(), MagmaAmuletRender::new);
            CuriosRendererRegistry.register(ModCurios.WITCH_HAT.get(), WitchHatRender::new);
        }
    }

}
