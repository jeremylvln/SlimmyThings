package fr.blueslime.slimmythings.tileentity;

import fr.blueslime.slimmythings.block.BlockMagneticCardReader;
import fr.blueslime.slimmythings.init.ModBlocks;
import fr.blueslime.slimmythings.item.ItemCard;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMagneticCardReader extends TileEntityPeripheral
{
    private static final String STATE_NBT = "State";
    private static final String DATA_TO_WRITE_NBT = "DataToWrite";

    private BlockMagneticCardReader.EnumState state = BlockMagneticCardReader.EnumState.IDLE;
    private String dataToWrite;

    private boolean dirtyState = true;

    public TileEntityMagneticCardReader()
    {
        this.setHasEventQueue();

        this.computerMethodRegistry.register("readData", this::onMethodReadData);
        this.computerMethodRegistry.register("writeData", this::onMethodWriteData);
        this.computerMethodRegistry.register("setIdle", this::onMethodSetIdle);
        this.computerMethodRegistry.register("setBusy", this::onMethodSetBusy);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public void onCardSwipe(EntityPlayer player, ItemStack stack)
    {
        String cardData = ItemCard.getData(stack);

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD)
        {
            this.pushEvent(new Object[] { player.getName(), cardData });
        }
        else if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
        {
            ItemCard.setData(stack, this.dataToWrite);
            this.state = BlockMagneticCardReader.EnumState.IDLE;
            this.dirtyState = true;
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (this.dirtyState)
        {
            IBlockState blockState = this.world.getBlockState(this.pos);
            this.world.setBlockState(this.pos, blockState.withProperty(BlockMagneticCardReader.STATE, this.state), 3);
            this.dirtyState = false;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        if (newState.getBlock() == oldState.getBlock() && oldState.getBlock() == ModBlocks.MAGNETIC_CARD_READER)
            return false;
        return super.shouldRefresh(world, pos, oldState, newState);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.state = BlockMagneticCardReader.EnumState.values()[nbtTagCompound.getInteger(STATE_NBT)];

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
            this.dataToWrite = nbtTagCompound.getString(DATA_TO_WRITE_NBT);

        this.dirtyState = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setInteger(STATE_NBT, this.state.ordinal());

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
            nbtTagCompound.setString(DATA_TO_WRITE_NBT, this.dataToWrite);

        return nbtTagCompound;
    }

    @Override
    public String getComputerName()
    {
        return "magnetic_card_reader";
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodReadData(Object[] args)
    {
        this.state = BlockMagneticCardReader.EnumState.WAITING_CARD;
        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodWriteData(Object[] args)
    {
        this.dataToWrite = String.valueOf(args[0]);
        this.state = BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodSetIdle(Object[] args)
    {
        this.state = BlockMagneticCardReader.EnumState.IDLE;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodSetBusy(Object[] args)
    {
        this.state = BlockMagneticCardReader.EnumState.BUSY;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodSetRejected(Object[] args)
    {
        this.state = BlockMagneticCardReader.EnumState.REJECTED;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetState(Object[] args)
    {
        return new Object[] { this.state.getName() };
    }
}
