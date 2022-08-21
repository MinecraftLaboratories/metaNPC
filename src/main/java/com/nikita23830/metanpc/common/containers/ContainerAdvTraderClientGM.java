package com.nikita23830.metanpc.common.containers;

import com.nikita23830.metanpc.MetaNPC;
import com.nikita23830.metanpc.client.GuiAbsTrade;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ContainerAdvTraderClientGM extends GuiContainer {

    public ResourceLocation guiTexture;
    private final IInventory inventory;
    private FontRenderer fonts = null;

    public ContainerAdvTraderClientGM(Container container, IInventory inventory) {
        super(container);
        guiTexture = new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/2.png");
        this.xSize = 256;
        this.ySize = 256;
        this.inventory = inventory;
    }

    public FontRenderer getFontRender() {
        return this.fonts;
    }

    public int[] preDrawScreen(int mouseX, int mouseY) {
        mouseX = Mouse.getEventX();
        mouseY = height - Mouse.getY();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, width, height, 0.0D, 0.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.0001f);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        return new int[]{mouseX, mouseY};
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, (guiTop), 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fonts = fontRendererObj;
    }
}

