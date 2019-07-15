package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.common.ComputerMethodRegistry;
import fr.blueslime.slimeperipherals.common.IComputerIntegration;
import net.minecraft.tileentity.TileEntity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class TileEntityPeripheral extends TileEntity implements IComputerIntegration
{
    protected ComputerMethodRegistry computerMethodRegistry = new ComputerMethodRegistry();
    private Queue<Object[]> queuedEvents = new ConcurrentLinkedQueue<>();

    @Override
    public Object[] invokeComputerMethod(int index, Object[] args) throws NoSuchMethodException
    {
        return this.computerMethodRegistry.invokeMethod(index, args);
    }

    protected void pushEvent(Object[] payload)
    {
        this.queuedEvents.add(payload);
    }

    protected void setHasEventQueue(boolean flag)
    {
        if (!flag)
        {
            this.computerMethodRegistry.remove("pollEvent");
            this.computerMethodRegistry.remove("hasEvent");
        }
        else
        {
            this.computerMethodRegistry.register("pollEvent", this::onMethodPollEvent);
            this.computerMethodRegistry.register("hasEvent", this::onMethodHasEvent);
        }
    }

    @Override
    public String[] getComputerMethods()
    {
        return this.computerMethodRegistry.getMethods();
    }

    private Object[] onMethodPollEvent(Object[] args)
    {
        if (this.queuedEvents.isEmpty())
            return new Object[0];

        return this.queuedEvents.poll();
    }

    private Object[] onMethodHasEvent(Object[] args)
    {
        return new Object[] { !this.queuedEvents.isEmpty() };
    }
}
