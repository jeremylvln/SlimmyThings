package fr.blueslime.slimeperipherals.init;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import fr.blueslime.slimeperipherals.network.C2SPacketCraftElectronicPad;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork
{
    public static SimpleNetworkWrapper NETWORK;

    public static void init()
    {
        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(SlimePeripherals.MODID);
        NETWORK.registerMessage(C2SPacketCraftElectronicPad.class, C2SPacketCraftElectronicPad.Message.class, 0, Side.SERVER);
    }
}
