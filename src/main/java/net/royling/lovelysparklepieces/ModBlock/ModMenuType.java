package net.royling.lovelysparklepieces.ModBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.royling.lovelysparklepieces.LovelySparklePieces;
import net.royling.lovelysparklepieces.ModBlock.CurioWorkbench.CurioWorkMenu;

import java.util.function.Supplier;

public class ModMenuType {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, LovelySparklePieces.MODID);

    public static final Supplier<MenuType<CurioWorkMenu>> CURIO_WORK_MENU =
            MENUS.register("curio_work",()-> IMenuTypeExtension.create(CurioWorkMenu::new));
}
