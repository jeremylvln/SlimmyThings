package fr.blueslime.slimeperipherals.init;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.block.*;
import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import fr.blueslime.slimeperipherals.tileentity.TileEntityMagneticCardReader;
import fr.blueslime.slimeperipherals.tileentity.TileEntityRFIDAntenna;
import fr.blueslime.slimeperipherals.tileentity.TileEntityRFIDWriter;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = SlimePeripherals.MODID)
public class ModBlocks
{
    private static final Map<ItemBlock, Consumer<ItemBlock>> ITEM_BLOCK_CACHE = new HashMap<>();

    public static final Block MAGNETIC_CARD_READER = new BlockMagneticCardReader();
    public static final Block RFID_ANTENNA = new BlockRFIDAntenna();
    public static final Block RFID_WRITER = new BlockRFIDWriter();
    public static final Block ELECTRONIC_LOCK = new BlockElectronicLock();
    public static final Block ELECTRONIC_PAD_DESIGNER = new BlockElectronicPadDesigner();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        registerBlock(event, MAGNETIC_CARD_READER, TileEntityMagneticCardReader.class);
        registerBlock(event, RFID_ANTENNA, TileEntityRFIDAntenna.class);
        registerBlock(event, RFID_WRITER, TileEntityRFIDWriter.class);
        registerBlock(event, ELECTRONIC_LOCK, TileEntityElectronicLock.class);
        registerBlock(event, ELECTRONIC_PAD_DESIGNER);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event)
    {
        ITEM_BLOCK_CACHE.forEach((itemBlock, modelCallback) ->
        {
            event.getRegistry().register(itemBlock);

            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
                modelCallback.accept(itemBlock);
        });

        ITEM_BLOCK_CACHE.clear();
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block)
    {
        registerBlock(event, block, null);
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, Class<? extends TileEntity> tileEntityClass)
    {
        registerBlock(event, block, tileEntityClass, (itemBlock) ->
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName(), "inventory")));
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, Class<? extends TileEntity> tileEntityClass, Consumer<ItemBlock> modelCallback)
    {
        event.getRegistry().register(block);

        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());

        ITEM_BLOCK_CACHE.put(itemBlock, modelCallback);

        if (tileEntityClass != null)
            GameRegistry.registerTileEntity(tileEntityClass, block.getRegistryName());
    }
}
