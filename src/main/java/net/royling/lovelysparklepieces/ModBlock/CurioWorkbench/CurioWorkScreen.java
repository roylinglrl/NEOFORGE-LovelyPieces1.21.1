package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.royling.lovelysparklepieces.LovelySparklePieces;

public class CurioWorkScreen extends AbstractContainerScreen<CurioWorkMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LovelySparklePieces.MODID, "textures/gui/curio_workbench_gui.png");

    public CurioWorkScreen(CurioWorkMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 220;
        this.imageHeight = 200;
        this.inventoryLabelY = 111;
    }

    @Override
    protected void init() {
        super.init();
        // 标题居中
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
        this.titleLabelY = 10; // 标题上移（更靠近顶部）

        // 玩家物品栏标签居中
        this.inventoryLabelX = (this.imageWidth - this.font.width(this.playerInventoryTitle)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        // 绘制整个背景纹理
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(guiGraphics, pMouseX, pMouseY, pPartialTick);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);
        // 输出槽高亮效果
        if (!menu.getSlot(9).getItem().isEmpty()) {
            int outputX = this.leftPos + 159; // 输出槽X坐标（相对于GUI左上角）
            int outputY = this.topPos + 61;   // 输出槽Y坐标
            guiGraphics.fill(outputX, outputY, outputX + 18, outputY + 18, 0x66FFAA00);
        }
    }
}
