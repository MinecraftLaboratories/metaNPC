package com.nikita23830.metanpc.common.containers;

import com.nikita23830.metanpc.common.NetworkHandler;
import com.nikita23830.metanpc.common.RoleAdvanceTrader;
import com.nikita23830.metanpc.common.packets.ActualInventoryGMPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.entity.EntityNPCInterface;

public class ContainetAdvTraderServerGM extends ContainerMod {
    private final EntityNPCInterface ent;
    private final EntityPlayer player;
    public final RoleAdvanceTrader.Inventory inventoryIn = new RoleAdvanceTrader.Inventory(48, "", "");
    public final RoleAdvanceTrader.Inventory inventoryOut = new RoleAdvanceTrader.Inventory(48, "", "");

    public ContainetAdvTraderServerGM(InventoryPlayer playerInventory, EntityNPCInterface ent) {
        addPlayerSlots(playerInventory, 40, (174));
        int index = 0;
        for (int k = 0; k < 3; ++k) { // столбцы
            for (int x = 0; x < 2; ++x) {
                for (int y = 0; y < 8; ++y) {
                    addSlotToContainer(new Slot(inventoryIn, index, (5 + (k * 79) + (x * 18)), (8 + (y * 18))));
                    ++index;
                }
            }
        }
        index = 0;
        for (int k = 0; k < 3; ++k) { // столбцы
            for (int x = 0; x < 2; ++x) {
                for (int y = 0; y < 8; ++y) {
                    addSlotToContainer(new Slot(inventoryOut, index, (43 + (k * 79) + (x * 18)), (8 + (y * 18))));
                    ++index;
                }
            }
        }
        RoleAdvanceTrader role = (RoleAdvanceTrader) ent.roleInterface;
        index = 0;
        for (int i = 0; i < 24; ++i) {
            RoleAdvanceTrader.TradeSlot slot = role.getSlot(i);
            if (slot.getSlotIn(0) != null && slot.getSlotIn(0).data != null) { // TODO
                inventoryIn.setInventorySlotContents(index, slot.getSlotIn(0).getStackToGM());
            }
            if (slot.getSlotIn(1) != null && slot.getSlotIn(1).data != null) { // TODO
                inventoryIn.setInventorySlotContents((index + 8), slot.getSlotIn(1).getStackToGM());
            }

            if (slot.getSlotOut(0) != null && slot.getSlotOut(0).data != null) { // TODO
                inventoryOut.setInventorySlotContents(index, slot.getSlotOut(0).getStackToGM());
            }
            if (slot.getSlotOut(1) != null && slot.getSlotOut(1).data != null) { // TODO
                inventoryOut.setInventorySlotContents((index + 8), slot.getSlotOut(1).getStackToGM());
            }

            ++index;
            if (index == 8 || index == 24)
                index += 8;
        }

        this.ent = ent;
        this.player = playerInventory.player;
        if (FMLCommonHandler.instance().getSide().isServer()) {
            NetworkHandler.INSTANCE.sendTo(createPacketToClient(), (EntityPlayerMP) this.player);
        }
    }

    public ActualInventoryGMPacket createPacketToClient() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagList in = new NBTTagList();
        inventoryIn.writeBaseInventoryNBT(in);
        nbt.setTag("in", in);
        NBTTagList out = new NBTTagList();
        inventoryOut.writeBaseInventoryNBT(out);
        nbt.setTag("out", out);
        return new ActualInventoryGMPacket(nbt);
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        RoleAdvanceTrader role = (RoleAdvanceTrader) this.ent.roleInterface;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 8; ++j) {
                ItemStack stack = inventoryIn.getStackInSlot(j + (i * 16));
                ItemStack stack2 = inventoryIn.getStackInSlot(j + (i * 16) + 8);
                if (stack != null) {
                    role.getSlot(j + (i * 8)).setSlotIn(0, new RoleAdvanceTrader.TradeSlotIn(new Object[]{RoleAdvanceTrader.TradeType.STACK.ordinal(), stack.copy()}));
                } else
                    role.getSlot(j + (i * 8)).setSlotIn(0, null);
                if (stack2 != null)
                    role.getSlot(j + (i * 8)).setSlotIn(1, new RoleAdvanceTrader.TradeSlotIn(new Object[]{RoleAdvanceTrader.TradeType.STACK.ordinal(), stack2.copy()}));
                else
                    role.getSlot(j + (i * 8)).setSlotIn(1, null);

                ItemStack stackOut = inventoryOut.getStackInSlot(j + (i * 16));
                ItemStack stackOut2 = inventoryOut.getStackInSlot(j + (i * 16) + 8);
                if (stackOut != null)
                    role.getSlot(j + (i * 8)).setSlotOut(0, new RoleAdvanceTrader.TradeSlotOut(new Object[]{RoleAdvanceTrader.TradeType.STACK.ordinal(), stackOut.copy()}));
                else
                    role.getSlot(j + (i * 8)).setSlotOut(0, null);
                if (stackOut2 != null)
                    role.getSlot(j + (i * 8)).setSlotOut(1, new RoleAdvanceTrader.TradeSlotOut(new Object[]{RoleAdvanceTrader.TradeType.STACK.ordinal(), stackOut2.copy()}));
                else
                    role.getSlot(j + (i * 8)).setSlotOut(1, null);
            }
        }
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
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value){
        super.updateProgressBar(id, value);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot){
        return null;
    }
}
