package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ElectronicPadEntry implements INBTSerializable<NBTTagCompound>, IStringSerializable
{
    public enum Type { CHARACTER, ITEM_STACK }

    private static final String TYPE_NBT = "Type";
    private static final String PAD_POSITION_NBT = "PadPosition";

    protected Type type;
    protected int padPosition;

    public ElectronicPadEntry(Type type, int padPosition)
    {
        this.type = type;
        this.padPosition = padPosition;
    }

    @SideOnly(Side.CLIENT)
    public abstract void render(double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation);

    public abstract void serializeEntryNBT(NBTTagCompound nbtTagCompound);
    public abstract void deserializeEntryNBT(NBTTagCompound nbtTagCompound);

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setInteger(TYPE_NBT, this.type.ordinal());
        nbtTagCompound.setInteger(PAD_POSITION_NBT, this.padPosition);
        this.serializeEntryNBT(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbtTagCompound)
    {
        this.padPosition = nbtTagCompound.getInteger(PAD_POSITION_NBT);
        this.deserializeEntryNBT(nbtTagCompound);
    }

    public static ElectronicPadEntry byType(NBTTagCompound nbtTagCompound)
    {
        switch (Type.values()[nbtTagCompound.getInteger(TYPE_NBT)])
        {
            case CHARACTER: return new ElectronicPadEntryCharacter();
            case ITEM_STACK: return new ElectronicPadEntryItemStack();
            default: return null;
        }
    }

    public static Vec3d getPositionWithOrientation(double x, double y, double z, int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        Vec3d relative = getRelativeFromOrientation(padPosition, orientation);
        return new Vec3d(x + relative.x, y + relative.y, z + relative.z);
    }

    public static AxisAlignedBB getEntryAABB(double x, double y, double z, int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        AxisAlignedBB box = getRelativeEntryAABB(padPosition, orientation);
        return new AxisAlignedBB(
                box.minX + x, box.minY + y, box.minZ + z,
                box.maxX + x, box.maxY + y, box.maxZ + z
        );
    }

    private static Vec3d getRelativeFromOrientation(int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        double padColumn = padPosition % 3;
        double padLine = Math.floor(padPosition / 3);

        switch (orientation)
        {
            case SOUTH:
                return new Vec3d(
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125),
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.09375D
                );
            case NORTH:
                return new Vec3d(
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125),
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.84375D
                );
            case WEST:
                return new Vec3d(
                        0.84375D,
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125)
                );
            case EAST:
                return new Vec3d(
                        0.09375D,
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125)
                );
            case UP_SOUTH:
                return new Vec3d(
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125),
                        0.09375D,
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125)
                );
            case UP_NORTH:
                return new Vec3d(
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125),
                        0.09375D,
                        0.34375D + (padLine * 0.0625D) + (padLine * 0.03125)
                );
            case UP_WEST:
                return new Vec3d(
                        0.34375D + (padLine * 0.0625D) + (padLine * 0.03125),
                        0.09375D,
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125)
                );
            case UP_EAST:
                return new Vec3d(
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.09375D,
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125)
                );
            case DOWN_SOUTH:
                return new Vec3d(
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125),
                        0.84375D,
                        0.34375D + (padLine * 0.0625D) + (padLine * 0.03125)
                );
            case DOWN_NORTH:
                return new Vec3d(
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125),
                        0.84375D,
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125)
                );
            case DOWN_WEST:
                return new Vec3d(
                        0.59375D - (padLine * 0.0625D) - (padLine * 0.03125),
                        0.84375D,
                        0.5625D - (padColumn * 0.0625D) - (padColumn * 0.03125)
                );
            case DOWN_EAST:
                return new Vec3d(
                        0.34375D + (padLine * 0.0625D) + (padLine * 0.03125),
                        0.84375D,
                        0.375D + (padColumn * 0.0625D) + (padColumn * 0.03125)
                );
        }

        throw new IllegalStateException("Invalid orientation");
    }

    private static AxisAlignedBB getRelativeEntryAABB(int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        Vec3d relative = getRelativeFromOrientation(padPosition, orientation);
        return new AxisAlignedBB(
                relative.x, relative.y, relative.z,
                relative.x + 0.0625D, relative.y + 0.0625D, relative.z + 0.0625D
        );
    }

    public static int getPadPositionFromHitPoint(float hitX, float hitY, float hitZ, BlockMagneticCardReader.EnumOrientation orientation)
    {
        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1)
        {
            AxisAlignedBB buttonCube = getRelativeEntryAABB(i, orientation);

            if (buttonCube.contains(new Vec3d(hitX, hitY, hitZ)))
                return i;
        }

        return -1;
    }
}
