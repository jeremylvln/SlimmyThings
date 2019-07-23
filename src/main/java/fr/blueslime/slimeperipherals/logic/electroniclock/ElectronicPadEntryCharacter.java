package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElectronicPadEntryCharacter extends ElectronicPadEntry
{
    private static final String CHARACTER_NBT = "Character";

    private char character;

    public ElectronicPadEntryCharacter()
    {
        super(Type.CHARACTER, 0);
    }

    public ElectronicPadEntryCharacter(int padPosition, char character)
    {
        super(Type.CHARACTER, padPosition);
        this.character = character;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation)
    {

    }

    @Override
    public void serializeEntryNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString(CHARACTER_NBT, this.character + "");
    }

    @Override
    public void deserializeEntryNBT(NBTTagCompound nbtTagCompound)
    {
        this.character = nbtTagCompound.getString(CHARACTER_NBT).charAt(0);
    }

    @Override
    public String getName()
    {
        return String.format("'%c'", this.character);
    }
}
