package com.nikita23830.metanpc.client;

import com.nikita23830.metanpc.common.containers.ContainerAdvTraderClient;
import com.nikita23830.metanpc.common.containers.ContainerAdvTraderClientGM;
import com.nikita23830.metanpc.common.containers.ContainerAdvTraderServer;
import com.nikita23830.metanpc.common.containers.ContainetAdvTraderServerGM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.lib.api.gui.utils.ScaleManager;

public class GuiAdvTradeGM extends ContainerAdvTraderClientGM {

    private final EntityNPCInterface npc;

    public GuiAdvTradeGM(EntityNPCInterface npc, EntityPlayer player) {
        super(new ContainetAdvTraderServerGM(player.inventory, npc), player.inventory);
        this.npc = npc;
    }

    @Override
    public void initGui() {
        super.initGui();
        //ScaleManager.update();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    public void parseNBTContainer(NBTTagCompound nbt) {
        NBTTagList in = nbt.getTagList("in", 10);
        NBTTagList out = nbt.getTagList("out", 10);
        ((ContainetAdvTraderServerGM)this.inventorySlots).inventoryIn.readBaseInventoryNBT(in);
        ((ContainetAdvTraderServerGM)this.inventorySlots).inventoryOut.readBaseInventoryNBT(out);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
