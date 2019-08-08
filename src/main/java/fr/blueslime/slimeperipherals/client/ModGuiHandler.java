package fr.blueslime.slimeperipherals.client;

import fr.blueslime.slimeperipherals.client.gui.GuiElectronicPadDesigner;
import fr.blueslime.slimeperipherals.inventory.ContainerElectronicPadDesigner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler
{
    public static final int ELECTRONIC_PAD_DESIGNER_ID = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (id)
        {
            case ELECTRONIC_PAD_DESIGNER_ID:
                return new ContainerElectronicPadDesigner(player.inventory);

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (id)
        {
            case ELECTRONIC_PAD_DESIGNER_ID:
                return new GuiElectronicPadDesigner(player.inventory);

            default:
                return null;
        }
    }
}
