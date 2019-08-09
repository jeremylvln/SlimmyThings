package fr.blueslime.slimmythings;

import fr.blueslime.slimmythings.client.ModGuiHandler;
import fr.blueslime.slimmythings.common.CommonProxy;
import fr.blueslime.slimmythings.init.ModIntegrations;
import fr.blueslime.slimmythings.init.ModItems;
import fr.blueslime.slimmythings.init.ModNetwork;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = SlimmyThings.MODID, name = SlimmyThings.NAME, version = SlimmyThings.VERSION, acceptedMinecraftVersions = "[1.12,)")
public class SlimmyThings
{
    public static final String MODID = "slimmythings";
    public static final String NAME = "Slimmy Things";
    public static final String VERSION = "${version}";

    public static Logger LOGGER;

    @Mod.Instance
    public static SlimmyThings INSTANCE;

    @SidedProxy(serverSide = "fr.blueslime.slimmythings.common.CommonProxy", clientSide = "fr.blueslime.slimmythings.client.ClientProxy")
    public static CommonProxy PROXY;

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
        NetworkRegistry.INSTANCE.registerGuiHandler(SlimmyThings.INSTANCE, new ModGuiHandler());
        ModNetwork.init();

        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        ModIntegrations.init();
        PROXY.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(SlimmyThings.MODID))
            ConfigManager.sync(SlimmyThings.MODID, Config.Type.INSTANCE);
    }
}
