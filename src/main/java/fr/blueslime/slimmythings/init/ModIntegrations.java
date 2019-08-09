package fr.blueslime.slimmythings.init;

import fr.blueslime.slimmythings.integration.computer.OpenComputersDriver;
import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

public class ModIntegrations
{
    private static final String OPENCOMPUTERS_MOD_ID = "opencomputers";

    public static void init()
    {
        if (Loader.isModLoaded(OPENCOMPUTERS_MOD_ID))
            loadOpenComputersIntegration();
    }

    @Optional.Method(modid = OPENCOMPUTERS_MOD_ID)
    private static void loadOpenComputersIntegration()
    {
        Driver.add(new OpenComputersDriver());
    }
}
