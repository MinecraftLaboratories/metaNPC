package com.nikita23830.metanpc.common.containers;

import com.nikita23830.metanpc.MetaNPC;
import com.nikita23830.metanpc.client.GuiAbsTrade;
import com.nikita23830.metanpc.client.GuiAdvTrade;
import com.nikita23830.metanpc.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ContainerAdvTraderClient extends GuiAbsTrade {

    public ResourceLocation guiTexture;
    private final IInventory inventory;
    private FontRenderer fonts = null;

    public ContainerAdvTraderClient(Container container, IInventory inventory) {
        super(container);
        guiTexture = new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/1.png");
        this.xSize = 256;
        this.ySize = 256;
        this.inventory = inventory;
    }

    public FontRenderer getFontRender() {
        return this.fonts;
    }

    public int[] preDrawScreen(int mouseX, int mouseY) {
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, (double)this.width, (double)this.height, 0.0, 0.0, 3000.0);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
        GL11.glEnable(3042);
        GL11.glAlphaFunc(516, 1.0E-4F);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        return new int[]{mouseX, mouseY};
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
//        mc.getTextureManager().bindTexture(guiTexture);
//        drawTexturedModalRect(guiLeft, (guiTop), 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fonts = fontRendererObj;
    }
}
