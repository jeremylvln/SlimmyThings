package fr.blueslime.slimmythings.client;

import fr.blueslime.slimmythings.common.CommonProxy;
import fr.blueslime.slimmythings.tileentity.TileEntityElectronicLock;
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
