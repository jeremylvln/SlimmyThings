package fr.blueslime.slimeperipherals.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WidgetButton extends GuiButton
{
    private final WidgetIcon icon;

    public WidgetButton(int buttonId, int x, int y, WidgetIcon icon)
    {
        super(buttonId, x, y, WidgetIcon.BUTTON_IDLE.getWidth(), WidgetIcon.BUTTON_IDLE.getHeight(), "");
        this.icon = icon;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (!this.visible)
            return;

        mc.getTextureManager().bindTexture(WidgetIcon.TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        WidgetIcon backgroundIcon = WidgetIcon.BUTTON_IDLE;

        if (!this.enabled)
            backgroundIcon = WidgetIcon.BUTTON_DISABLED;
        else if (this.hovered)
            backgroundIcon = WidgetIcon.BUTTON_HOVER;

        this.drawTexturedModalRect(this.x, this.y, backgroundIcon.getX(), backgroundIcon.getY(), this.width, this.height);
        this.drawTexturedModalRect(this.x, this.y, this.icon.getX(), this.icon.getY(), this.width, this.height);
    }
}
