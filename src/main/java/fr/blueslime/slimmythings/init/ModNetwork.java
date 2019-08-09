package fr.blueslime.slimmythings.init;

import fr.blueslime.slimmythings.SlimmyThings;
import fr.blueslime.slimmythings.network.C2SPacketCraftElectronicPad;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork
{
    public static SimpleNetworkWrapper NETWORK;

    public static void init()
    {
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(SlimmyThings.MODID);
        NETWORK.registerMessage(C2SPacketCraftElectronicPad.class, C2SPacketCraftElectronicPad.Message.class, 0, Side.SERVER);
    }
}
