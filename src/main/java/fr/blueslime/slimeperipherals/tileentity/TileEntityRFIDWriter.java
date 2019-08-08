package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.block.BlockRFIDWriter;
import fr.blueslime.slimeperipherals.init.ModBlocks;
import fr.blueslime.slimeperipherals.item.ItemCard;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRFIDWriter extends TileEntityPeripheral implements ITickable
{
    private static final String STATE_NBT = "State";
    private static final String CARD_NBT = "Card";
    private static final String DATA_TO_WRITE_NBT = "DataToWrite";
    private static final String OPERATION_TICKS_NBT = "OperationTicks";

    private static final int TICKS_NEEDED_TO_WRITE = 20 * 5;

    private BlockRFIDWriter.EnumState state = BlockRFIDWriter.EnumState.IDLE;
    private String dataToWrite;
    private ItemStack currentStack = ItemStack.EMPTY;
    private boolean dirtyState = true;
    private int operationTicks = 0;

    public TileEntityRFIDWriter()
    {
        this.computerMethodRegistry.register("writeData", this::onMethodWriteData);
        this.computerMethodRegistry.register("getProgress", this::onMethodGetProgress);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public void onCardPlace(EntityPlayer player, ItemStack stack)
    {
        if (!this.currentStack.isEmpty())
        {
            this.onCardTake();
            return;
        }

        this.currentStack = stack.copy();
        player.inventory.decrStackSize(player.inventory.currentItem, 1);
        this.dirtyState = true;
    }

    public void onCardTake()
    {
        if (this.state == BlockRFIDWriter.EnumState.BUSY || this.currentStack.isEmpty())
            return;

        InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.currentStack);
        this.currentStack = ItemStack.EMPTY;
        this.dirtyState = true;
    }

    public void onBlockBreak()
    {
        if (!this.currentStack.isEmpty())
            InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.currentStack);
        this.currentStack = ItemStack.EMPTY;
    }

    @Override
    public void update()
    {
        super.update();

        if (this.state == BlockRFIDWriter.EnumState.WAITING_CARD && !this.currentStack.isEmpty())
        {
            this.state = BlockRFIDWriter.EnumState.BUSY;
            this.operationTicks = 0;
            this.dirtyState = true;
        }
        else if (this.state == BlockRFIDWriter.EnumState.BUSY)
        {
            this.operationTicks += 1;

            if (this.operationTicks >= TICKS_NEEDED_TO_WRITE)
            {
                ItemCard.setData(this.currentStack, this.dataToWrite);
                this.state = BlockRFIDWriter.EnumState.IDLE;
                this.operationTicks = 0;
                this.dirtyState = true;
            }
        }

        if (this.dirtyState)
        {
            IBlockState blockState = this.world.getBlockState(this.pos);
            this.world.setBlockState(this.pos, blockState.withProperty(BlockRFIDWriter.STATE, this.state)
                    .withProperty(BlockRFIDWriter.FILLED, !this.currentStack.isEmpty()), 3);
            this.dirtyState = false;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        if (newState.getBlock() == oldState.getBlock() && oldState.getBlock() == ModBlocks.RFID_WRITER)
            return false;
        return super.shouldRefresh(world, pos, oldState, newState);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.state = BlockRFIDWriter.EnumState.values()[nbtTagCompound.getInteger(STATE_NBT)];

        if (nbtTagCompound.hasKey(CARD_NBT))
            this.currentStack = new ItemStack(nbtTagCompound.getCompoundTag(CARD_NBT));

        if (this.state == BlockRFIDWriter.EnumState.WAITING_CARD)
            this.dataToWrite = nbtTagCompound.getString(DATA_TO_WRITE_NBT);
        else if (this.state == BlockRFIDWriter.EnumState.BUSY)
            this.operationTicks = nbtTagCompound.getInteger(OPERATION_TICKS_NBT);

        this.dirtyState = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(STATE_NBT, this.state.ordinal());

        if (!this.currentStack.isEmpty())
        {
            NBTTagCompound stackTag = new NBTTagCompound();
            this.currentStack.writeToNBT(stackTag);
            nbtTagCompound.setTag(CARD_NBT, stackTag);
        }

        if (this.state == BlockRFIDWriter.EnumState.WAITING_CARD)
            nbtTagCompound.setString(DATA_TO_WRITE_NBT, this.dataToWrite);
        else if (this.state == BlockRFIDWriter.EnumState.BUSY)
            nbtTagCompound.setInteger(OPERATION_TICKS_NBT, this.operationTicks);

        return nbtTagCompound;
    }

    @Override
    public String getComputerName()
    {
        return "rfid_writer";
    }

    private Object[] onMethodWriteData(Object[] args)
    {
        this.dataToWrite = String.valueOf(args[0]);
        this.state = BlockRFIDWriter.EnumState.WAITING_CARD;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetProgress(Object[] args)
    {
        return new Object[] { this.operationTicks, TICKS_NEEDED_TO_WRITE };
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetState(Object[] args)
    {
        return new Object[] { this.state.getName() };
    }
}
