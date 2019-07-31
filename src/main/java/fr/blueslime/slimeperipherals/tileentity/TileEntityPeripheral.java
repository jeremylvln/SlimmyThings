package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.integration.computer.ComputerMethodRegistry;
import fr.blueslime.slimeperipherals.integration.computer.IComputerIntegration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TileEntityPeripheral extends TileEntity implements IComputerIntegration
{
    private static final String OWNER_NBT = "Owner";

    protected ComputerMethodRegistry computerMethodRegistry = new ComputerMethodRegistry();
    protected UUID owner = null;

    private Queue<Object[]> queuedEvents = new ConcurrentLinkedQueue<>();

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.owner = UUID.fromString(nbtTagCompound.getString(OWNER_NBT));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setString(OWNER_NBT, this.owner.toString());
        return nbtTagCompound;
    }

    @Override
    public Object[] invokeComputerMethod(int index, Object[] args) throws NoSuchMethodException
    {
        return this.computerMethodRegistry.invokeMethod(index, args);
    }

    protected void pushEvent(Object[] payload)
    {
        this.queuedEvents.add(payload);
    }

    public void setOwner(UUID owner)
    {
        this.owner = owner;
    }

    protected void setHasEventQueue()
    {
        this.computerMethodRegistry.register("pollEvent", this::onMethodPollEvent);
        this.computerMethodRegistry.register("hasEvent", this::onMethodHasEvent);
    }

    @Override
    public String[] getComputerMethods()
    {
        return this.computerMethodRegistry.getMethods();
    }

    public UUID getOwner()
    {
        return this.owner;
    }

    protected boolean assertInteractWithSecurity(EntityPlayer player)
    {
        if (this.owner != player.getUniqueID())
        {
            player.sendMessage(new TextComponentTranslation("slimeperipherals.peripheral.access_refused"));
            return false;
        }

        return true;
    }

    private Object[] onMethodPollEvent(Object[] args)
    {
        if (this.queuedEvents.isEmpty())
            return new Object[0];

        return this.queuedEvents.poll();
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodHasEvent(Object[] args)
    {
        return new Object[] { !this.queuedEvents.isEmpty() };
    }
}
