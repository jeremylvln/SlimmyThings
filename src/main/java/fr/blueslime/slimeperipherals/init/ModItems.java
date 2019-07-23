package fr.blueslime.slimeperipherals.init;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.item.ItemCard;
import fr.blueslime.slimeperipherals.item.ItemElectronicPad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = SlimePeripherals.MODID)
public class ModItems
{
    public static final Item MAGNETIC_CARD = new ItemCard("magnetic");
    public static final Item RFID_CARD = new ItemCard("rfid");
    public static final Item ELECTRONIC_PAD = new ItemElectronicPad();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(MAGNETIC_CARD);
        event.getRegistry().register(RFID_CARD);
        event.getRegistry().register(ELECTRONIC_PAD);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerItemsModels(ModelRegistryEvent event)
    {
        for (EnumDyeColor color : EnumDyeColor.values())
        {
            registerModel(MAGNETIC_CARD, color.getMetadata(), color.getUnlocalizedName());
            registerModel(RFID_CARD, color.getMetadata(), color.getUnlocalizedName());
        }

        registerModel(ELECTRONIC_PAD, 0, null, false);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item)
    {
        registerModel(item, 0);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item, int metadata)
    {
        registerModel(item, metadata, null);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item, int metadata, String metadataName)
    {
        registerModel(item, metadata, metadataName, true);
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item, int metadata, String metadataName, boolean specialName)
    {
        String resourceName = item.getUnlocalizedName().substring(5).replace('.', ':');

        if (specialName)
            resourceName += "_" + (metadataName != null ? metadataName : metadata);

        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(resourceName, "inventory"));
    }
}
