package fr.blueslime.slimmythings.inventory;

import fr.blueslime.slimmythings.init.ModItems;
import fr.blueslime.slimmythings.logic.electroniclock.ElectronicPadData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryElectronicPadDesigner implements IInventory
{
    public static final int PAD_INPUT_SLOT = 0;
    public static final int PAD_OUTPUT_SLOT = 1;
    public static final int ICON_INPUT_SLOT_START = 2;
    public static final int ICON_INPUT_SLOT_END = ICON_INPUT_SLOT_START + ElectronicPadData.ENTRIES_NB;
    public static final int PLAYER_INVENTORY_SLOT_START = ICON_INPUT_SLOT_END;
    public static final int PLAYER_INVENTORY_SLOT_END = PLAYER_INVENTORY_SLOT_START + (9 * 3);
    public static final int PLAYER_HOTBAR_SLOT_START = PLAYER_INVENTORY_SLOT_END;
    public static final int PLAYER_HOTBAR_SLOT_END = PLAYER_HOTBAR_SLOT_START + 9;

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2 + ElectronicPadData.ENTRIES_NB, ItemStack.EMPTY);

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void clear()
    {
        this.inventory.clear();
    }

    @Override
    public void markDirty() {}

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return this.inventory.get(index);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public String getName()
    {
        return "container.slimmythings.electronic_pad_designer";
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TextComponentTranslation(this.getName());
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index == PAD_INPUT_SLOT)
            return stack.getItem() == ModItems.ELECTRONIC_PAD;
        else
            return index != PAD_OUTPUT_SLOT;
    }

    @Override
    public boolean isEmpty()
    {
        return this.inventory.stream().anyMatch(stack -> !stack.isEmpty());
    }
}
