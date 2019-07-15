package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import fr.blueslime.slimeperipherals.init.ModBlocks;
import fr.blueslime.slimeperipherals.item.ItemCard;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMagneticCardReader extends TileEntityPeripheral implements ITickable
{
    private BlockMagneticCardReader.EnumState state = BlockMagneticCardReader.EnumState.IDLE;
    private String dataToWrite;
    private boolean dirtyState = true;

    public TileEntityMagneticCardReader()
    {
        this.setHasEventQueue(true);

        this.computerMethodRegistry.register("readData", this::onMethodReadData);
        this.computerMethodRegistry.register("writeData", this::onMethodWriteData);
        this.computerMethodRegistry.register("setBusy", this::onMethodSetBusy);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public void onCardSwipe(EntityPlayer player, ItemStack stack)
    {
        String cardData = ((ItemCard) stack.getItem()).getData(stack);

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD)
        {
            System.out.println("SWIPED CARD '" + cardData + "'");
            this.pushEvent(new Object[] { player.getName(), cardData });
        }
        else if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
        {
            ((ItemCard) stack.getItem()).setData(stack, this.dataToWrite);
            this.state = BlockMagneticCardReader.EnumState.IDLE;
            this.dirtyState = true;
        }
    }

    @Override
    public void update()
    {
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
        this.state = BlockMagneticCardReader.EnumState.values()[nbtTagCompound.getInteger("state")];

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
            this.dataToWrite = nbtTagCompound.getString("dataToWrite");

        this.dirtyState = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        if (this.state == BlockMagneticCardReader.EnumState.WAITING_CARD_WRITE)
            nbtTagCompound.setString("dataToWrite", this.dataToWrite);

        return nbtTagCompound;
    }

    @Override
    public String getComputerName()
    {
        return "magnetic_card_reader";
    }

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

    private Object[] onMethodSetBusy(Object[] args)
    {
        if ((boolean) args[0])
            this.state = BlockMagneticCardReader.EnumState.BUSY;
        else
            this.state = BlockMagneticCardReader.EnumState.IDLE;

        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodSetRejected(Object[] args)
    {
        if ((boolean) args[0])
            this.state = BlockMagneticCardReader.EnumState.REJECTED;
        else
            this.state = BlockMagneticCardReader.EnumState.IDLE;

        this.dirtyState = true;
        return new Object[0];
    }

    private Object[] onMethodGetState(Object[] args)
    {
        return new Object[] { this.state.getName() };
    }
}
