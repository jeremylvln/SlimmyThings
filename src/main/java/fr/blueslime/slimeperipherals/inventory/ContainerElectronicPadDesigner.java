package fr.blueslime.slimeperipherals.inventory;

import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.item.ItemElectronicPad;
import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static fr.blueslime.slimeperipherals.inventory.InventoryElectronicPadDesigner.*;

public class ContainerElectronicPadDesigner extends Container
{
    private final InventoryPlayer playerInventory;

    public ContainerElectronicPadDesigner(InventoryPlayer playerInventory)
    {
        this.playerInventory = playerInventory;

        IInventory blockInventory = new InventoryElectronicPadDesigner();

        this.addSlotToContainer(new Slot(blockInventory, PAD_INPUT_SLOT, 127, 36)
        {
            @Override
            public void onSlotChanged()
            {
                ContainerElectronicPadDesigner.this.importPadConfiguration();
            }

            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == ModItems.ELECTRONIC_PAD;
            }
        });

        this.addSlotToContainer(new Slot(blockInventory, PAD_OUTPUT_SLOT, 127, 80)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return false;
            }
        });

        for (int y = 0; y < 4; y += 1)
            for (int x = 0; x < 3; x += 1)
                this.addSlotToContainer(new Slot(blockInventory, ICON_INPUT_SLOT_START + (y * 3) + x, 26 + x * 26, 20 + y * 26));

        for (int y = 0; y < 3; y += 1)
            for (int x = 0; x < 9; x += 1)
                this.addSlotToContainer(new Slot(playerInventory, (y * 9) + x + 9, 8 + x * 18, 132 + y * 18));

        for (int x = 0; x < 9; x += 1)
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 190));
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);

        if (!playerIn.world.isRemote)
        {
            for (int i = PAD_INPUT_SLOT; i < PLAYER_INVENTORY_SLOT_START; i += 1)
            {
                Slot slot = this.getSlot(i);
                ItemStack slotStack = slot.decrStackSize(slot.getSlotStackLimit());

                if (!slotStack.isEmpty())
                    playerIn.dropItem(slotStack, false);
            }
        }
    }

    public void processCraft()
    {
        if (!this.isCorrectlyFilled())
            return;

        ItemStack result = new ItemStack(ModItems.ELECTRONIC_PAD, 1);

        ElectronicPadData.Builder padDataBuilder = new ElectronicPadData.Builder();

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1)
        {
            padDataBuilder.with(i, this.getSlot(ICON_INPUT_SLOT_START + i).getStack().copy());
            this.getSlot(ICON_INPUT_SLOT_START + i).decrStackSize(1);
        }

        ItemElectronicPad.setPadData(result, padDataBuilder.build());
        this.getSlot(PAD_INPUT_SLOT).decrStackSize(1);
        this.getSlot(PAD_OUTPUT_SLOT).putStack(result);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack finalStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            finalStack = slotStack.copy();

            if (index == PAD_INPUT_SLOT || index == PAD_OUTPUT_SLOT)
            {
                // If we try to take the items from the output slot, we try to put them in the inventory
                if (!this.mergeItemStack(slotStack, PLAYER_INVENTORY_SLOT_START, PLAYER_HOTBAR_SLOT_END, true))
                    return ItemStack.EMPTY;

                slot.onSlotChange(slotStack, finalStack);
            }
            else if (index >= PLAYER_INVENTORY_SLOT_START)
            {
                // If we try to put items in the inventory

                if (slotStack.getItem() == ModItems.ELECTRONIC_PAD)
                {
                    // If the item we try to put is a Electronic Pad and share the same NBTs as the stack
                    // already present, we merge them.
                    if (!this.inventorySlots.get(PAD_INPUT_SLOT).getStack().isEmpty()
                    && !ItemStack.areItemStackTagsEqual(slotStack, this.inventorySlots.get(PAD_INPUT_SLOT).getStack()))
                        return ItemStack.EMPTY;

                    if (!this.mergeItemStack(slotStack, PAD_INPUT_SLOT, PAD_INPUT_SLOT + 1, false))
                        return ItemStack.EMPTY;
                }
                else if (index < PLAYER_HOTBAR_SLOT_START)
                {
                    // If the items are not compatible but are in the player's inventory, we try to place
                    // them in the player's hotbar.
                    if (!this.mergeItemStack(slotStack, PLAYER_HOTBAR_SLOT_START, PLAYER_HOTBAR_SLOT_END, false))
                        return ItemStack.EMPTY;
                }
                else if (!this.mergeItemStack(slotStack, PLAYER_INVENTORY_SLOT_START, PLAYER_INVENTORY_SLOT_END, false))
                {
                    // Inverse case scenario than above.
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty())
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();

            if (slotStack.getCount() == finalStack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(playerIn, slotStack);
        }

        return finalStack;
    }

    private void importPadConfiguration()
    {
        Slot slot = this.getSlot(PAD_INPUT_SLOT);

        if (!slot.getHasStack())
            return;

        ElectronicPadData padData = ItemElectronicPad.getPadData(slot.getStack());

        if (padData == null)
            return;

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1)
        {
            Slot entrySlot = this.getSlot(ICON_INPUT_SLOT_START + i);

            if (!entrySlot.getStack().isEmpty())
                this.playerInventory.player.dropItem(entrySlot.getStack(), false);

            entrySlot.putStack(padData.getEntry(i).getStack());
        }

        ItemElectronicPad.setPadData(slot.getStack(), null);
        slot.onSlotChanged();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public boolean isCorrectlyFilled()
    {
        boolean filled = true;

        for (int i = ICON_INPUT_SLOT_START; i < ICON_INPUT_SLOT_END; i += 1)
        {
            if (!this.getSlot(i).getHasStack())
            {
                filled = false;
                break;
            }
        }

        if (!filled)
            return false;

        return this.getSlot(PAD_INPUT_SLOT).getHasStack() && !this.getSlot(PAD_OUTPUT_SLOT).getHasStack();
    }
}
