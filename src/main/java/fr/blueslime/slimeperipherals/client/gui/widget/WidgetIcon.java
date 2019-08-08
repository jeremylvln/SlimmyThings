package fr.blueslime.slimeperipherals.client.gui.widget;

import fr.blueslime.slimeperipherals.SlimePeripherals;
import net.minecraft.util.ResourceLocation;

public enum WidgetIcon
{
    BUTTON_IDLE(0, 0),
    BUTTON_HOVER(0, 14),
    BUTTON_DISABLED(0, 14 * 2),

    ARROW_DOWN(14, 0);

    public static final ResourceLocation TEXTURE = new ResourceLocation(SlimePeripherals.MODID, "textures/gui/widgets.png");

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    WidgetIcon(int x, int y)
    {
        this(x, y, 14);
    }

    WidgetIcon(int x, int y, int size)
    {
        this(x, y, size, size);
    }

    WidgetIcon(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }
}
