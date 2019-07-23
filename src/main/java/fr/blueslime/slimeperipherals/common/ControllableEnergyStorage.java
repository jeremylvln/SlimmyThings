package fr.blueslime.slimeperipherals.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ControllableEnergyStorage extends EnergyStorage implements INBTSerializable<NBTTagCompound>
{
    private static final String CAPACITY_NBT = "Capacity";
    private static final String ENERGY_NBT = "Energy";
    private static final String MAX_RECEIVE_NBT = "MaxReceive";
    private static final String MAX_EXTRACT_NBT = "MaxExtract";

    public ControllableEnergyStorage(int capacity)
    {
        super(capacity);
    }

    public ControllableEnergyStorage(int capacity, int maxTransfer)
    {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public ControllableEnergyStorage(int capacity, int maxReceive, int maxExtract)
    {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public ControllableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy)
    {
        super(capacity, maxReceive, maxExtract, energy);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setInteger(CAPACITY_NBT, this.capacity);
        nbtTagCompound.setInteger(ENERGY_NBT, this.energy);
        nbtTagCompound.setInteger(MAX_RECEIVE_NBT, this.maxReceive);
        nbtTagCompound.setInteger(MAX_EXTRACT_NBT, this.maxExtract);

        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbtTagCompound)
    {
        this.capacity = nbtTagCompound.getInteger(CAPACITY_NBT);
        this.energy = nbtTagCompound.getInteger(ENERGY_NBT);
        this.maxReceive = nbtTagCompound.getInteger(MAX_RECEIVE_NBT);
        this.maxExtract = nbtTagCompound.getInteger(MAX_EXTRACT_NBT);
    }

    public void receiveEnergy(IEnergyStorage storage, boolean simulate)
    {
        this.energy += storage.extractEnergy(Math.min(this.getEnergyStored(), this.getMaxExtract()), simulate);
    }

    public void extractEnergy(IEnergyStorage storage, boolean simulate)
    {
        this.energy -= storage.receiveEnergy(Math.min(this.getEnergyStored(), this.getMaxExtract()), simulate);
    }

    public void setMaxTransfer(int maxTransfer)
    {
        this.setMaxReceive(maxTransfer);
        this.setMaxExtract(maxTransfer);
    }

    public void setMaxReceive(int maxReceive)
    {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract)
    {
        this.maxExtract = maxExtract;
    }

    public int getMaxReceive()
    {
        return this.maxReceive;
    }

    public int getMaxExtract()
    {
        return this.maxExtract;
    }
}
