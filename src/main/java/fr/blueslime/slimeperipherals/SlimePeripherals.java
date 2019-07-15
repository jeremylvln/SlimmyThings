package fr.blueslime.slimeperipherals;

import fr.blueslime.slimeperipherals.common.OpenComputersDriver;
import fr.blueslime.slimeperipherals.init.ModItems;
import li.cil.oc.api.Driver;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = SlimePeripherals.MODID, name = SlimePeripherals.NAME, version = SlimePeripherals.VERSION, acceptedMinecraftVersions = "[1.12,)")
public class SlimePeripherals
{
    public static final String MODID = "slimeperipherals";
    public static final String NAME = "Slime Peripherals";
    public static final String VERSION = "${version}";

    public static Logger LOGGER;

    @Mod.Instance
    public static SlimePeripherals INSTANCE;

    public static CreativeTabs TAB = new CreativeTabs(MODID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(ModItems.MAGNETIC_CARD, 1);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        Driver.add(new OpenComputersDriver());
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(SlimePeripherals.MODID))
            ConfigManager.sync(SlimePeripherals.MODID, Config.Type.INSTANCE);
    }
}
