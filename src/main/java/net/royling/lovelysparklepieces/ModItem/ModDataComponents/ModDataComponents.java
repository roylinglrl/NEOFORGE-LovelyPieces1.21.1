package net.royling.lovelysparklepieces.ModItem.ModDataComponents;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, LovelySparklePieces.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<BoundaryStoneData>> BOUNDARY_STONE_DATA_HOLDER =
            DATA_COMPONENT_TYPES.register("boundary_stone_data", () ->
                    DataComponentType.<BoundaryStoneData>builder()
                            .persistent(BoundaryStoneData.CODEC)
                            .networkSynchronized(BoundaryStoneData.STREAM_CODEC)
                            .build());


    public static final DeferredHolder<DataComponentType<?>,DataComponentType<EnergyComponent>> ENERGY =
            DATA_COMPONENT_TYPES.register("energy",()->
                    DataComponentType.<EnergyComponent>builder()
                            .persistent(EnergyComponent.CODEC)
                            .build());

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
