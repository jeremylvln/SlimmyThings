package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
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

    public static final ElectronicPadData NUMERIC = new Builder()
            .withCharacter(0, '1').withCharacter(1, '2').withCharacter(2, '3')
            .withCharacter(3, '4').withCharacter(4, '5').withCharacter(5, '6')
            .withCharacter(6, '7').withCharacter(7, '8').withCharacter(8, '9')
            .withCharacter(9, 'A').withCharacter(10, '0').withCharacter(11, 'B')
            .build();

    public static final ElectronicPadData ITEMS = new Builder()
            .withItemStack(0, new ItemStack(Blocks.LOG)).withItemStack(1, new ItemStack(Blocks.SAND)).withItemStack(2, new ItemStack(Blocks.COBBLESTONE))
            .withItemStack(3, new ItemStack(Blocks.QUARTZ_BLOCK)).withItemStack(4, new ItemStack(Blocks.PRISMARINE, 1, BlockPrismarine.BRICKS_META)).withItemStack(5, new ItemStack(Blocks.BEDROCK))
            .withItemStack(6, new ItemStack(Blocks.GLOWSTONE)).withItemStack(7, new ItemStack(Blocks.MAGMA)).withItemStack(8, new ItemStack(Blocks.GLASS))
            .withCharacter(9, 'A').withItemStack(10, new ItemStack(Blocks.COAL_BLOCK)).withCharacter(11, 'B')
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
    public void render(double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation)
    {
        for (int i = 0; i < ENTRIES_NB; i += 1)
            this.entries[i].render(x, y, z, orientation);
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
        for (int i = 0; i < ENTRIES_NB; i += 1) {
            this.entries[i] = ElectronicPadEntry.byType(nbtTagList.getCompoundTagAt(i));

            if (this.entries[i] == null)
                throw new IllegalStateException("Unknown electronic pad entry");

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

        public Builder withCharacter(int pos, char character)
        {
            this.entries[pos] = new ElectronicPadEntryCharacter(pos, character);
            return this;
        }

        public Builder withItemStack(int pos, ItemStack stack)
        {
            this.entries[pos] = new ElectronicPadEntryItemStack(pos, stack);
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
