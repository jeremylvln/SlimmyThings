package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.block.BlockElectronicLock;
import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import fr.blueslime.slimeperipherals.init.ModBlocks;
import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.item.ItemElectronicPad;
import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadData;
import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.UUID;

public class TileEntityElectronicLock extends TileEntityPeripheral
{
    public static final String PERIPHERAL_NAME = "electronic_lock";

    private static final String STATE_NBT = "State";
    private static final String PAD_NBT = "Pad";

    private BlockElectronicLock.EnumState state = BlockElectronicLock.EnumState.IDLE;
    private ItemStack pad = ItemStack.EMPTY;
    private ElectronicPadData padData = null;

    private boolean dirtyState = true;

    public TileEntityElectronicLock()
    {
        this.setHasEventQueue();

        this.computerMethodRegistry.register("acceptInputs", this::onMethodAcceptInputs);
        this.computerMethodRegistry.register("setIdle", this::onMethodSetIdle);
        this.computerMethodRegistry.register("setRejected", this::onMethodSetRejected);
        this.computerMethodRegistry.register("getState", this::onMethodGetState);
    }

    public void onPlayerInteract(EntityPlayer player, float hitX, float hitY, float hitZ)
    {
        if (this.pad.isEmpty())
            return;

        int padPosition = ElectronicPadEntry.getPadPositionFromHitPoint(hitX, hitY, hitZ,
                this.world.getBlockState(this.pos).getValue(BlockElectronicLock.ORIENTATION));

        player.sendMessage(new TextComponentString(
                this.world.getBlockState(this.pos).getValue(BlockElectronicLock.ORIENTATION).getName() + ": " +
                        padPosition + " [" + hitX + "; " + hitY + "; " + hitZ + "]"));

        if (padPosition != -1)
        {
            this.pushEvent(new Object[] { player.getName(), padPosition });
            this.world.playSound(player, this.pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }
    }

    public void onPadMovement(EntityPlayer player, ItemStack stack)
    {
        if (!this.assertInteractWithSecurity(player))
            return;

        if (!this.pad.isEmpty())
        {
            InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.pad);
            this.pad = ItemStack.EMPTY;
            this.padData = null;

            IBlockState blockState = this.world.getBlockState(this.pos);
            this.world.notifyBlockUpdate(this.pos, blockState, blockState, 2);

            return;
        }

        if (stack.getItem() != ModItems.ELECTRONIC_PAD)
            return;

        this.pad = stack.copy();
        this.pad.setCount(1);

        this.padData = ItemElectronicPad.getPadData(this.pad);
        player.inventory.decrStackSize(player.inventory.currentItem, 1);

        IBlockState blockState = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, blockState, blockState, 2);
    }

    public void onBlockBreak()
    {
        if (!this.pad.isEmpty())
            InventoryHelper.spawnItemStack(this.world, this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D, this.pad);
        this.pad = ItemStack.EMPTY;
    }

    @Override
    public void update()
    {
        super.update();

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

        if (nbtTagCompound.hasKey(PAD_NBT))
        {
            this.pad = new ItemStack(nbtTagCompound.getCompoundTag(PAD_NBT));
            this.padData = ItemElectronicPad.getPadData(this.pad);
        }

        this.dirtyState = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger(STATE_NBT, this.state.ordinal());

        if (!this.pad.isEmpty())
            nbtTagCompound.setTag(PAD_NBT, this.pad.writeToNBT(new NBTTagCompound()));

        return nbtTagCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        NBTTagCompound nbtTagCompound = packet.getNbtCompound();

        if (nbtTagCompound.hasKey(PAD_NBT))
        {
            this.pad = new ItemStack(nbtTagCompound.getCompoundTag(PAD_NBT));
            this.padData = ItemElectronicPad.getPadData(this.pad);
        }
        else
        {
            this.pad = ItemStack.EMPTY;
            this.padData = null;
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();

        if (!this.pad.isEmpty())
            nbtTagCompound.setTag(PAD_NBT, this.pad.writeToNBT(new NBTTagCompound()));

        return new SPacketUpdateTileEntity(this.getPos(), 1, nbtTagCompound);
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
        return PERIPHERAL_NAME;
    }

    @SuppressWarnings({ "unused" })
    private Object[] onMethodAcceptInputs(Object[] args)
    {
        if ((boolean) args[0])
            this.state = BlockElectronicLock.EnumState.WAITING_INPUT;
        else
            this.state = BlockElectronicLock.EnumState.IDLE;

        this.dirtyState = true;
        return new Object[0];
    }

    @SuppressWarnings({ "unused" })
    private Object[] onMethodSetIdle(Object[] args)
    {
        this.state = BlockElectronicLock.EnumState.IDLE;
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
