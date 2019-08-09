package fr.blueslime.slimmythings.client.gui;

import fr.blueslime.slimmythings.SlimmyThings;
import fr.blueslime.slimmythings.client.gui.widget.WidgetButton;
import fr.blueslime.slimmythings.client.gui.widget.WidgetIcon;
import fr.blueslime.slimmythings.init.ModNetwork;
import fr.blueslime.slimmythings.inventory.ContainerElectronicPadDesigner;
import fr.blueslime.slimmythings.network.C2SPacketCraftElectronicPad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

import static fr.blueslime.slimmythings.inventory.InventoryElectronicPadDesigner.*;

@SideOnly(Side.CLIENT)
public class GuiElectronicPadDesigner extends GuiContainer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(SlimmyThings.MODID, "textures/gui/container/electronic_pad_designer.png");
    private static final int BACKGROUND_WIDTH = 176;
    private static final int BACKGROUND_HEIGHT = 214;

    private final InventoryPlayer playerInventory;
    private WidgetButton confirmButton;

    public GuiElectronicPadDesigner(InventoryPlayer playerInventory)
    {
        super(new ContainerElectronicPadDesigner(playerInventory));
        this.playerInventory = playerInventory;
        this.xSize = BACKGROUND_WIDTH;
        this.ySize = BACKGROUND_HEIGHT;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.confirmButton = new WidgetButton(0, this.guiLeft + 128, this.guiTop + 57, WidgetIcon.ARROW_DOWN);
        this.confirmButton.enabled = false;
        this.buttonList.add(this.confirmButton);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        this.confirmButton.enabled = ((ContainerElectronicPadDesigner) this.inventorySlots).isCorrectlyFilled();
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ModNetwork.NETWORK.sendToServer(new C2SPacketCraftElectronicPad.Message(player));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = I18n.format("container.slimmythings.electronic_pad_designer");
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int middleX = (this.width - this.xSize) / 2;
        int middleY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(middleX, middleY, 0, 0, this.xSize, this.ySize);
    }
}
