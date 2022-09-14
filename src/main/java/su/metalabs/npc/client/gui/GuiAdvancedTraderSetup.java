package su.metalabs.npc.client.gui;

import su.metalabs.npc.Reference;
import su.metalabs.npc.common.containers.ContainerAdvancedTraderSetup;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.lib.api.gui.GuiContainerMeta;
import su.metalabs.lib.api.gui.MetaAsset;
import su.metalabs.lib.api.gui.utils.RenderUtils;
import su.metalabs.lib.api.gui.utils.ScaleManager;

import java.awt.*;

public class GuiAdvancedTraderSetup extends GuiContainerMeta {
    public GuiAdvancedTraderSetup(InventoryPlayer inventory, EntityNPCInterface npc) {
        super(new ContainerAdvancedTraderSetup(inventory, npc));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY) {
        RenderUtils.drawColoredRect(0,0, width, height, Color.BLACK, 0.7F);
        RenderUtils.drawRect(width / 2.0F - ScaleManager.get(918) / 2.0F, ScaleManager.get(145), ScaleManager.get(918), ScaleManager.get(768), MetaAsset.of(Reference.MOD_ID, "textures/gui/advanced_trader_setup/background.png"));
        super.drawGuiContainerBackgroundLayer(ticks, mouseX, mouseY);
    }

    public void parseNBTContainer(NBTTagCompound nbt) {
        NBTTagList in = nbt.getTagList("in", 10);
        NBTTagList out = nbt.getTagList("out", 10);
        NBTTagList xp = nbt.getTagList("xp", 10);
        ((ContainerAdvancedTraderSetup)this.inventorySlots).inventoryIn.readBaseInventoryNBT(in);
        ((ContainerAdvancedTraderSetup)this.inventorySlots).inventoryOut.readBaseInventoryNBT(out);
        ((ContainerAdvancedTraderSetup)this.inventorySlots).inventoryXp.readBaseInventoryNBT(xp);
    }
}
