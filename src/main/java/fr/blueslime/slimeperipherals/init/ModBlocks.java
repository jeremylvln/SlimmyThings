package fr.blueslime.slimeperipherals.init;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.block.BlockMagneticCardReader;
import fr.blueslime.slimeperipherals.tileentity.TileEntityMagneticCardReader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = SlimePeripherals.MODID)
public class ModBlocks
{
    private static final List<ItemBlock> ITEM_BLOCK_CACHE = new ArrayList<>();

    public static final Block MAGNETIC_CARD_READER = new BlockMagneticCardReader();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        registerBlock(event, MAGNETIC_CARD_READER, "magnetic_card_reader");
        registerTileEntity(TileEntityMagneticCardReader.class, "magnetic_card_reader");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event)
    {
        ITEM_BLOCK_CACHE.forEach(itemBlock -> event.getRegistry().register(itemBlock));
        ITEM_BLOCK_CACHE.clear();
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, String registryName)
    {
        registerBlock(event, block, registryName, (itemBlock) ->
                ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName(), "inventory")));
    }

    private static void registerBlock(RegistryEvent.Register<Block> event, Block block, String registryName, Consumer<ItemBlock> modelCallback)
    {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(SlimePeripherals.MODID, registryName);

        event.getRegistry().register(block);
        ITEM_BLOCK_CACHE.add(itemBlock);

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            modelCallback.accept(itemBlock);
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String registryName)
    {
        GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(SlimePeripherals.MODID, registryName));
    }
}
