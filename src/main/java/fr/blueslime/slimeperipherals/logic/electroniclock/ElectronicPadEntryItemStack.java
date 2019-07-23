package fr.blueslime.slimeperipherals.logic.electroniclock;

import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ElectronicPadEntryItemStack extends ElectronicPadEntry
{
    private static final String STACK_NBT = "Item";

    private final RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

    private ItemStack stack;

    public ElectronicPadEntryItemStack()
    {
        super(Type.ITEM_STACK, 0);
    }

    public ElectronicPadEntryItemStack(int padPosition, ItemStack stack)
    {
        super(Type.ITEM_STACK, padPosition);
        this.stack = stack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation)
    {
        renderItemStack(this.stack, this.padPosition, x, y, z, orientation);
    }

    @Override
    public void serializeEntryNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setTag(STACK_NBT, this.stack.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void deserializeEntryNBT(NBTTagCompound nbtTagCompound)
    {
        this.stack = new ItemStack(nbtTagCompound.getCompoundTag(STACK_NBT));
    }

    @Override
    public String getName()
    {
        return this.stack.getDisplayName();
    }

    static void renderItemStack(ItemStack stack, int padPosition, double x, double y, double z, BlockMagneticCardReader.EnumOrientation orientation)
    {
        Vec3d renderPosition = getPositionWithOrientation(x, y, z, padPosition, orientation);

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
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
