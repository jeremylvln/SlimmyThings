package fr.blueslime.slimeperipherals.item;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCard extends Item
{
    private static final String DATA_NBT_KEY = "data";

    public ItemCard(String type)
    {
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setRegistryName(SlimePeripherals.MODID, type + "_card");
        this.setUnlocalizedName(SlimePeripherals.MODID + "." + type + "_card");
        this.setCreativeTab(SlimePeripherals.TAB);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(DATA_NBT_KEY))
            return;

        tooltip.add("Written");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (this.isInCreativeTab(tab))
            for (int i = 0; i < 16; i += 1)
                items.add(new ItemStack(this, 1, i));
    }

    public void setData(ItemStack stack, String data)
    {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setString(DATA_NBT_KEY, data);
    }

    public String getData(ItemStack stack)
    {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(DATA_NBT_KEY))
            return null;
        return stack.getTagCompound().getString(DATA_NBT_KEY);
    }
}
