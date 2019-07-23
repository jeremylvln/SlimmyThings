package fr.blueslime.slimeperipherals.client;

import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityElectronicLockSpecialRenderer extends TileEntitySpecialRenderer<TileEntityElectronicLock>
{
    @Override
    public void render(TileEntityElectronicLock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te.getPadData() != null)
            te.getPadData().render(x, y, z, te.getOrientation());
    }
}
