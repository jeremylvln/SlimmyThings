package fr.blueslime.slimeperipherals.client;

import fr.blueslime.slimeperipherals.logic.electroniclock.ElectronicPadEntry;
import fr.blueslime.slimeperipherals.tileentity.TileEntityElectronicLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class TileEntityElectronicLockSpecialRenderer extends TileEntitySpecialRenderer<TileEntityElectronicLock>
{
    @Override
    public void render(TileEntityElectronicLock te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (te.getPadData() != null)
            te.getPadData().render(te, x, y, z);
    }

    private void showEntriesCollisionBoxes(TileEntityElectronicLock te, float partialTicks)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        double renderPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double renderPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double renderPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        for (int i = 0; i < 12; i += 1)
        {
            AxisAlignedBB aabb = ElectronicPadEntry.getEntryAABB(te.getPos().getX(), te.getPos().getY(),
                    te.getPos().getZ(), i, te.getOrientation());

            RenderGlobal.drawSelectionBoundingBox(aabb.grow(0.002D).offset(-renderPosX, -renderPosY, -renderPosZ), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
