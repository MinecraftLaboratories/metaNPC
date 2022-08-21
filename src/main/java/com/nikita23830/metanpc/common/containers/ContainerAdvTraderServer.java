package com.nikita23830.metanpc.common.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.entity.EntityNPCInterface;

public class ContainerAdvTraderServer extends ContainerMod {
    private final EntityNPCInterface ent;
    private final EntityPlayer player;

    public ContainerAdvTraderServer(InventoryPlayer playerInventory, EntityNPCInterface ent){
        addPlayerSlots(playerInventory, 40, (174));
        this.ent = ent;
        this.player = playerInventory.player;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void detectAndSendChanges(){
        super.detectAndSendChanges();
    }

    @Override
    public ItemStack slotClick(int p_75144_1_, int p_75144_2_, int p_75144_3_, EntityPlayer p_75144_4_) {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value){
        super.updateProgressBar(id, value);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        return null;
    }
}