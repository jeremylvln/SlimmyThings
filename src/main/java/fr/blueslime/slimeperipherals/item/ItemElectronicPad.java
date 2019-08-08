package fr.blueslime.slimeperipherals.item;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.init.ModItems;
import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadData;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemElectronicPad extends Item
{
    private static final String PAD_NBT = "Pad";

    public ItemElectronicPad()
    {
        this.setHasSubtypes(true);
        this.setRegistryName(SlimePeripherals.MODID, "electronic_pad");
        this.setUnlocalizedName(SlimePeripherals.MODID + "." + "electronic_pad");
        this.setCreativeTab(SlimePeripherals.TAB);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(PAD_NBT))
            return;

        ElectronicPadData padData = getPadData(stack);

        if (padData == null)
            return;

        tooltip.add("Contains the followings:");

        for (int i = 0; i < ElectronicPadData.ENTRIES_NB; i += 1)
            if (padData.getEntry(i) != null)
                tooltip.add(String.format("- At position %d: %s", i + 1, padData.getEntry(i).getStack().getDisplayName()));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
        {
            items.add(new ItemStack(ModItems.ELECTRONIC_PAD, 1));

            ItemStack itemsStack = new ItemStack(ModItems.ELECTRONIC_PAD, 1);
            setPadData(itemsStack, ElectronicPadData.ITEMS);
            items.add(itemsStack);
        }
    }

    public static void setPadData(ItemStack stack, ElectronicPadData padData)
    {
        if (padData == null)
        {
            stack.setTagCompound(null);
            return;
        }

        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setTag(PAD_NBT, padData.serializeNBT());
    }

    public static ElectronicPadData getPadData(ItemStack stack)
    {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(PAD_NBT))
            return null;

        ElectronicPadData padData = new ElectronicPadData();
        padData.deserializeNBT((NBTTagList) stack.getTagCompound().getTag(PAD_NBT));
        return padData;
    }
}
