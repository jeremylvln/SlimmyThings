package fr.blueslime.slimeperipherals.client;

import fr.blueslime.slimeperipherals.common.CommonProxy;
import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit() {}

    @Override
    public void init()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectronicLock.class, new TileEntityElectronicLockSpecialRenderer());
    }

    @Override
    public void postInit() {}
}
