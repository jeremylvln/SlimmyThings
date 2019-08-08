package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElectronicPadEntry implements INBTSerializable<NBTTagCompound>
{
    private static final String PAD_POSITION_NBT = "PadPosition";
    private static final String STACK_NBT = "Item";

    private int padPosition;
    private ItemStack stack;

    public ElectronicPadEntry(int padPosition)
    {
        this(padPosition, ItemStack.EMPTY);
    }

    public ElectronicPadEntry(int padPosition, ItemStack stack)
    {
        this.padPosition = padPosition;
        this.stack = stack;
    }

    @SideOnly(Side.CLIENT)
    public void render(double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation, boolean clicked)
    {
        Vec3d renderPosition = getPositionWithOrientation(x, y, z, this.padPosition, orientation);

        if (clicked)
            renderPosition = renderPosition.add(getClickedTranslate(orientation));

        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate(renderPosition.x, renderPosition.y, renderPosition.z);
        GlStateManager.scale(0.0625F * 2, 0.0625F * 2, 0.0625F * 2);
        GlStateManager.translate(0.25D, 0.25D, 0.25D);

        if (orientation.isHorizontal())
        {
            if (orientation == BlockMagneticCardReader.EnumOrientation.DOWN_NORTH
                    || orientation == BlockMagneticCardReader.EnumOrientation.DOWN_SOUTH
                    || orientation == BlockMagneticCardReader.EnumOrientation.UP_NORTH
                    || orientation == BlockMagneticCardReader.EnumOrientation.UP_SOUTH)
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            else
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
        }

        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItem(this.stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setInteger(PAD_POSITION_NBT, this.padPosition);
        nbtTagCompound.setTag(STACK_NBT, this.stack.writeToNBT(new NBTTagCompound()));
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbtTagCompound)
    {
        this.padPosition = nbtTagCompound.getInteger(PAD_POSITION_NBT);
        this.stack = new ItemStack(nbtTagCompound.getCompoundTag(STACK_NBT));
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public static AxisAlignedBB getEntryAABB(double x, double y, double z, int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        AxisAlignedBB box = getRelativeEntryAABB(padPosition, orientation);
        return new AxisAlignedBB(
                box.minX + x, box.minY + y, box.minZ + z,
                box.maxX + x, box.maxY + y, box.maxZ + z
        );
    }

    private static Vec3d getPositionWithOrientation(double x, double y, double z, int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        Vec3d relative = getRelativeFromOrientation(padPosition, orientation);
        return new Vec3d(x + relative.x, y + relative.y, z + relative.z);
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

    private static Vec3d getClickedTranslate(BlockMagneticCardReader.EnumOrientation orientation)
    {
        switch (orientation)
        {
            case SOUTH: return new Vec3d(0.0D, 0.0D, -0.015625D);
            case NORTH: return new Vec3d(0.0D, 0.0D, 0.015625D);
            case WEST: return new Vec3d(0.015625D, 0.0D, 0.0D);
            case EAST: return new Vec3d(-0.015625D, 0.0D, 0.0D);

            case UP_SOUTH:
            case UP_NORTH:
            case UP_WEST:
            case UP_EAST:
                return new Vec3d(0.0D, -0.015625D, 0.0D);

            case DOWN_SOUTH:
            case DOWN_NORTH:
            case DOWN_WEST:
            case DOWN_EAST:
                return new Vec3d(0.0D, 0.015625D, 0.0D);
        }

        throw new IllegalStateException("Invalid orientation");
    }

    private static AxisAlignedBB getRelativeEntryAABB(int padPosition, BlockMagneticCardReader.EnumOrientation orientation)
    {
        Vec3d relative = getRelativeFromOrientation(padPosition, orientation);
        return new AxisAlignedBB(
                relative.x, relative.y, relative.z,
                relative.x + 0.0625D, relative.y + 0.0625D, relative.z + 0.0625D
        ).grow(0.002D);
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
