package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElectronicPadData implements INBTSerializable<NBTTagList>
{
    public static final int ENTRIES_NB = 12;

    public static final ElectronicPadData ITEMS = new Builder()
            .with(0, new ItemStack(Blocks.LOG))
            .with(1, new ItemStack(Blocks.SAND))
            .with(2, new ItemStack(Blocks.COBBLESTONE))
            .with(3, new ItemStack(Blocks.QUARTZ_BLOCK))
            .with(4, new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.BRICKS_META))
            .with(5, new ItemStack(Blocks.BEDROCK))
            .with(6, new ItemStack(Blocks.GLOWSTONE))
            .with(7, new ItemStack(Blocks.MAGMA))
            .with(8, new ItemStack(Blocks.GLASS))
            .with(9, new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata()))
            .with(10, new ItemStack(Blocks.COAL_BLOCK))
            .with(11, new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GREEN.getMetadata()))
            .build();

    private final ElectronicPadEntry[] entries;

    public ElectronicPadData()
    {
        this.entries = new ElectronicPadEntry[ENTRIES_NB];
    }

    private ElectronicPadData(ElectronicPadEntry[] entries)
    {
        this.entries = entries;
    }

    @SideOnly(Side.CLIENT)
    public void render(TileEntityElectronicLock te, double x, double y, double z)
    {
        for (int i = 0; i < ENTRIES_NB; i += 1)
            this.entries[i].render(x, y, z, te.getOrientation(), te.isClicked(i));
    }

    @Override
    public NBTTagList serializeNBT()
    {
        NBTTagList nbtTagList = new NBTTagList();

        for (int i = 0; i < ENTRIES_NB; i += 1)
            if (this.entries[i] != null)
                nbtTagList.appendTag(this.entries[i].serializeNBT());

        return nbtTagList;
    }

    @Override
    public void deserializeNBT(NBTTagList nbtTagList)
    {
        for (int i = 0; i < ENTRIES_NB; i += 1)
        {
            this.entries[i] = new ElectronicPadEntry(i);
            this.entries[i].deserializeNBT(nbtTagList.getCompoundTagAt(i));
        }
    }

    public ElectronicPadEntry getEntry(int index)
    {
        return this.entries[index];
    }

    public static class Builder
    {
        private final ElectronicPadEntry[] entries;

        public Builder()
        {
            this.entries = new ElectronicPadEntry[ENTRIES_NB];
        }

        public ElectronicPadData build()
        {
            if (!this.validate())
                throw new IllegalStateException("A pad entry cannot be null");

            return new ElectronicPadData(this.entries);
        }


        public Builder with(int pos, ItemStack stack)
        {
            this.entries[pos] = new ElectronicPadEntry(pos, stack);
            return this;
        }

        public boolean validate()
        {
            for (int i = 0; i < ENTRIES_NB; i += 1)
                if (this.entries[i] == null)
                    return false;

            return true;
        }
    }
}
