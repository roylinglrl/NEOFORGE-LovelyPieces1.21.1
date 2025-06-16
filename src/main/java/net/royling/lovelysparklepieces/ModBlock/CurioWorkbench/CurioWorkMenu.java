package net.royling.lovelysparklepieces.ModBlock.CurioWorkbench;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.royling.lovelysparklepieces.ModBlock.ModBlocks;
import net.royling.lovelysparklepieces.ModBlock.ModMenuType;

public class CurioWorkMenu extends AbstractContainerMenu {

    private final CurioWorkbenchBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    public CurioWorkMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf buf) {
        // 添加空值检查，确保 buf 不为 null
        this(pContainerId, playerInventory,
                buf != null ? getBlockEntity(playerInventory.player.level(), buf.readBlockPos())
                        : new CurioWorkbenchBlockEntity(BlockPos.ZERO, ModBlocks.CURIO_WORKBENCH.get().defaultBlockState()));
    }
    private static CurioWorkbenchBlockEntity getBlockEntity(Level level, BlockPos pos) {
        if (level == null || !level.isLoaded(pos)) {
            return new CurioWorkbenchBlockEntity(pos, ModBlocks.CURIO_WORKBENCH.get().defaultBlockState());
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CurioWorkbenchBlockEntity) {
            return (CurioWorkbenchBlockEntity) blockEntity;
        }
        return new CurioWorkbenchBlockEntity(pos, ModBlocks.CURIO_WORKBENCH.get().defaultBlockState());
    }
    public CurioWorkMenu(int pContainerId, Inventory playerInventory, CurioWorkbenchBlockEntity blockEntity) {
        super(ModMenuType.CURIO_WORK_MENU.get(), pContainerId); // Replace with your actual MenuType registration
        this.blockEntity = blockEntity;
        this.levelAccess = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());
        final int CIRCLE_CENTER_X = 60;  // 圆心X坐标（左侧）
        final int CIRCLE_CENTER_Y = 62;  // 圆心Y坐标（上移15像素）
        final int CIRCLE_RADIUS = 40;    // 圆半径

        // 放置中心槽位（索引4）
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 4, CIRCLE_CENTER_X, CIRCLE_CENTER_Y));

        // 放置圆形排列的8个外围槽位
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            int x = (int) (CIRCLE_CENTER_X + CIRCLE_RADIUS * Math.cos(angle));
            int y = (int) (CIRCLE_CENTER_Y + CIRCLE_RADIUS * Math.sin(angle));
            int slotIndex = i < 4 ? i : i + 1;
            this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), slotIndex, x, y));
        }
        // --- 输出槽位 - 右侧 ---
        final int OUTPUT_SLOT_X = CIRCLE_CENTER_X + 100;  // 圆心右侧100像素
        final int OUTPUT_SLOT_Y = CIRCLE_CENTER_Y;        // 与圆心Y坐标相同
        this.addSlot(new CurioResultSlot(blockEntity, blockEntity.getItemHandler(), 9, OUTPUT_SLOT_X, OUTPUT_SLOT_Y));
        // --- 玩家物品栏 - 整体上移 ---
        final int PLAYER_INV_START_X = (220 - 162) / 2;   // 水平居中：(总宽度220 - 物品栏宽度162)/2 = 29
        final int PLAYER_INV_START_Y = 122;
        // 玩家主物品栏
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9,
                        PLAYER_INV_START_X + col * 18,
                        PLAYER_INV_START_Y + row * 18));
            }
        }
        // 玩家快捷栏
        final int PLAYER_HOTBAR_START_Y = PLAYER_INV_START_Y + 58;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col,
                    PLAYER_INV_START_X + col * 18,
                    PLAYER_HOTBAR_START_Y));
        }
    }
    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            // Logic for moving from Block Entity to Player Inventory
            // Block Entity input slots are 0-8
            // Block Entity output slot is 9
            // Player inventory starts after block entity slots
            int numBlockEntitySlots = blockEntity.getItemHandler().getSlots(); // 10 slots

            if (pIndex < numBlockEntitySlots) { // From block entity inventory
                if (!this.moveItemStackTo(slotStack, numBlockEntitySlots, this.slots.size(), true)) { // To player inventory
                    return ItemStack.EMPTY;
                }
                // If it's the output slot, trigger consumption (only if the stack was moved)
//                if (pIndex == 9 && slotStack.isEmpty()) { // Check if the output stack is now empty after quick move
//                    blockEntity.consumeIngredients(); // This might be tricky with quickMoveStack, ensure it fires correctly
//                    // Best practice is to consume on `onTake` of the output slot.
//                }
            } else if (pIndex >= numBlockEntitySlots) { // From player inventory
                // Try to move to input slots (0-8)
                if (!this.moveItemStackTo(slotStack, 0, 9, false)) { // To input slots
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotStack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        // Use ContainerLevelAccess to check if the block is still there and reachable
        return stillValid(levelAccess, pPlayer, ModBlocks.CURIO_WORKBENCH.get()); // Replace with your block
    }
    public static class CurioResultSlot extends SlotItemHandler {
        private final CurioWorkbenchBlockEntity blockEntity;
        private final Player player;
        private int removeCount;

        public CurioResultSlot(CurioWorkbenchBlockEntity blockEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.blockEntity = blockEntity;
            this.player = null; // We'll set this via the menu constructor if needed, or pass it directly.
            // For now, blockEntity has a reference to level, so we don't strictly need player here.
        }
        // Override to only allow extracting, not inserting, into the output slot
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
        // Called when an item is removed from the slot (e.g., player takes it)
        @Override
        public void onTake(Player pPlayer, ItemStack pStack) {
            //this.checkTakeAchievements(pStack);
            // This is where you trigger the consumption of ingredients
            blockEntity.consumeIngredients();
            super.onTake(pPlayer, pStack); // Call super after your custom logic
        }
        @Override
        public ItemStack remove(int amount) {
            if (!this.hasItem()) {
                return ItemStack.EMPTY;
            } else {
                this.removeCount += Math.min(amount, this.getItem().getCount());
                return super.remove(amount);
            }
        }
    }
}
