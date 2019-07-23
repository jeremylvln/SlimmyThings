package fr.blueslime.slimeperipherals.tileentity;

import fr.blueslime.slimeperipherals.common.ControllableEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEntityPoweredPeripheral extends TileEntityPeripheral implements ITickable
{
    private static final String ENERGY_STORAGE_NBT = "Energy";

    protected ControllableEnergyStorage energyStorage;

    public TileEntityPoweredPeripheral(int energyStorageCapacity)
    {
        this.energyStorage = new ControllableEnergyStorage(energyStorageCapacity);
        this.computerMethodRegistry.register("getEnergyStored", this::onMethodGetEnergyStored);
        this.computerMethodRegistry.register("getMaxEnergyStored", this::onMethodGetMaxEnergyStored);
    }

    @Override
    public void update()
    {
        for (EnumFacing face : EnumFacing.VALUES)
        {
            TileEntity tile = this.world.getTileEntity(this.pos.offset(face));

            if (tile == null)
                continue;

            if (tile.hasCapability(CapabilityEnergy.ENERGY, face.getOpposite()))
            {
                IEnergyStorage targetStorage = tile.getCapability(CapabilityEnergy.ENERGY, face.getOpposite());

                if (targetStorage.canReceive())
                    this.energyStorage.extractEnergy(targetStorage, false);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);
        this.energyStorage.deserializeNBT(nbtTagCompound.getCompoundTag(ENERGY_STORAGE_NBT));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setTag(ENERGY_STORAGE_NBT, this.energyStorage.serializeNBT());
        return nbtTagCompound;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return (T) this.energyStorage;

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetEnergyStored(Object[] args)
    {
        return new Object[] { this.energyStorage.getEnergyStored() };
    }

    @SuppressWarnings({ "unused "})
    private Object[] onMethodGetMaxEnergyStored(Object[] args)
    {
        return new Object[] { this.energyStorage.getMaxEnergyStored() };
    }
}
