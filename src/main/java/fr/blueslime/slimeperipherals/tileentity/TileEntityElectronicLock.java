package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.block.BlockElectronicLock;
import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import fr.blueslime.slimeperipherals.init.ModBlocks;
import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class TileEntityElectronicLock extends TileEntityPeripheral implements ITickable
{
    private static final String STATE_NBT = "State";
    private static final String OWNER_NBT = "Owner";
    private static final String PAD_NBT = "Pad";

    private BlockElectronicLock.EnumState state = BlockElectronicLock.EnumState.IDLE;
    private UUID owner = null;
    private ItemStack pad = null;
    private ElectronicPadData padData = ElectronicPadData.ITEMS;

    private boolean dirtyState = true;

    public TileEntityElectronicLock()
    {
        this.setHasEventQueue();

        this.computerMethodRegistry.register("setGranted", this::onMethodSetGranted);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    @Override
    public void update()
    {
        if (this.dirtyState)
        {
            IBlockState blockState = this.world.getBlockState(this.pos);
            this.world.setBlockState(this.pos, blockState.withProperty(BlockElectronicLock.STATE, this.state), 3);
            this.dirtyState = false;
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        if (newState.getBlock() == oldState.getBlock() && oldState.getBlock() == ModBlocks.ELECTRONIC_LOCK)
            return false;
        return super.shouldRefresh(world, pos, oldState, newState);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.state = BlockElectronicLock.EnumState.values()[nbtTagCompound.getInteger(STATE_NBT)];
        this.owner = UUID.fromString(nbtTagCompound.getString(OWNER_NBT));

        if (nbtTagCompound.hasKey(PAD_NBT))
            this.pad = new ItemStack(nbtTagCompound.getCompoundTag(PAD_NBT));

        this.dirtyState = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(STATE_NBT, this.state.ordinal());
        nbtTagCompound.setString(OWNER_NBT, this.owner.toString());

        if (this.pad != null)
            nbtTagCompound.setTag(PAD_NBT, this.pad.writeToNBT(new NBTTagCompound()));

        return nbtTagCompound;
    }

    public void setOwner(UUID owner)
    {
        this.owner = owner;
    }

    public BlockMagneticCardReader.EnumOrientation getOrientation()
    {
        return this.world.getBlockState(this.pos).getValue(BlockElectronicLock.ORIENTATION);
    }

    public ElectronicPadData getPadData()
    {
        return this.padData;
    }

    @Override
    public String getComputerName()
    {
        return "electronic_lock";
    }

    @SuppressWarnings({ "unused" })
    private Object[] onMethodSetGranted(Object[] args)
    {
        this.state = BlockElectronicLock.EnumState.GRANTED;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused" })
    private Object[] onMethodSetRejected(Object[] args)
    {
        this.state = BlockElectronicLock.EnumState.REJECTED;
        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetState(Object[] args)
    {
        return new Object[] { this.state.getName() };
    }
}
