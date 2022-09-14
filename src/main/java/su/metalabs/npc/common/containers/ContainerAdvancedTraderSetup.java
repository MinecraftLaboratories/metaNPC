package su.metalabs.npc.common.containers;

import su.metalabs.npc.Reference;
import com.nikita23830.metanpc.common.RoleAdvanceTrader;
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
import su.metalabs.lib.api.gui.MetaContainer;
import su.metalabs.lib.api.gui.SlotWrapper;
import su.metalabs.npc.common.packets.AdvancedTraderInventoryPacket;

public class ContainerAdvancedTraderSetup extends MetaContainer {
    private float width;
    private float height;
    private EntityNPCInterface npc;
    private EntityPlayer player;
    public final RoleAdvanceTrader.Inventory inventoryIn = new RoleAdvanceTrader.Inventory(48, "", "");
    public final RoleAdvanceTrader.Inventory inventoryOut = new RoleAdvanceTrader.Inventory(48, "", "");
    public final RoleAdvanceTrader.Inventory inventoryXp = new RoleAdvanceTrader.Inventory(24, "", "");

    public ContainerAdvancedTraderSetup(InventoryPlayer inventory, EntityNPCInterface npc) {
        super(inventory);
        this.npc = npc;
        this.width = 918;
        this.height = 768;

        float startX = -width/2.0F;
        float startY = 145;

        float inventorySlotSize = 54;
        addInventory(inventory, new SlotWrapper((-inventorySlotSize * (9 / 2.0F)), startY + 519, inventorySlotSize, true, true));


        RoleAdvanceTrader role = (RoleAdvanceTrader) npc.roleInterface;

        //Input inventory
        int index = 0;
        for (int k = 0; k < 3; ++k) {
            for (int x = 0; x < 2; ++x) {
                for (int y = 0; y < 8; ++y) {
                    addSlot(new Slot(inventoryIn, index, 0,0), new SlotWrapper(startX + 21 + x * inventorySlotSize + k * 300, startY + 21 + y * inventorySlotSize, inventorySlotSize, true, true));
                    ++index;
                }
            }
        }

        //Output inventory
        index = 0;
        for (int k = 0; k < 3; ++k) {
            for (int x = 0; x < 2; ++x) {
                for (int y = 0; y < 8; ++y) {
                    addSlot(new Slot(inventoryOut, index, 0,0), new SlotWrapper(startX + 135 + x * inventorySlotSize + k * 300, startY + 21 + y * inventorySlotSize, inventorySlotSize, true, true));
                    ++index;
                }
            }
        }

        //Xp inventory
        index = 0;
        for (int k = 0; k < 3; ++k) {
            for (int y = 0; y < 8; ++y) {
                addSlot(new Slot(inventoryXp, index, 0,0), new SlotWrapper(startX + 243 + k * 300, startY + 21 + y * inventorySlotSize, inventorySlotSize, true, true).setIcon(Reference.RESOURCE_PREFIX+ "textures/gui/advanced_trader_setup/xp_slot.png"));
                ++index;
            }
        }

        index = 0;
        for (int i = 0; i < 24; ++i) {
            RoleAdvanceTrader.TradeSlot slot = role.getSlot(i);
            if (slot.getSlotIn(0) != null && slot.getSlotIn(0).data != null) {
                inventoryIn.setInventorySlotContents(index, slot.getSlotIn(0).getStackToGM());
            }
            if (slot.getSlotIn(1) != null && slot.getSlotIn(1).data != null) {
                inventoryIn.setInventorySlotContents((index + 8), slot.getSlotIn(1).getStackToGM());
            }

            if (slot.getSlotOut(0) != null && slot.getSlotOut(0).data != null) {
                inventoryOut.setInventorySlotContents(index, slot.getSlotOut(0).getStackToGM());
            }
            if (slot.getSlotOut(1) != null && slot.getSlotOut(1).data != null) {
                inventoryOut.setInventorySlotContents((index + 8), slot.getSlotOut(1).getStackToGM());
            }

            if (slot.getSlotXp() != null && slot.getSlotXp().data != null) {
                inventoryXp.setInventorySlotContents((index), slot.getSlotXp().getStackToGM());
            }

            ++index;
            if (index == 8 || index == 24)
                index += 8;
        }

        this.player = inventory.player;
        if (FMLCommonHandler.instance().getSide().isServer()) {
            createPacketToClient().sendToPlayer((EntityPlayerMP) player);
        }
    }

    public AdvancedTraderInventoryPacket createPacketToClient() {
        NBTTagCompound nbt = new NBTTagCompound();

        NBTTagList in = new NBTTagList();
        inventoryIn.writeBaseInventoryNBT(in);
        nbt.setTag("in", in);

        NBTTagList out = new NBTTagList();
        inventoryOut.writeBaseInventoryNBT(out);
        nbt.setTag("out", out);

        NBTTagList xp = new NBTTagList();
        inventoryXp.writeBaseInventoryNBT(xp);
        nbt.setTag("xp", xp);

        return new AdvancedTraderInventoryPacket(nbt);
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        RoleAdvanceTrader role = (RoleAdvanceTrader) this.npc.roleInterface;
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

                ItemStack stackXp = inventoryXp.getStackInSlot(j + (i * 8));
                if(stackXp != null) {
                    role.getSlot(j + (i * 8)).setSlotXp(new RoleAdvanceTrader.TradeSlotXp(new Object[]{RoleAdvanceTrader.TradeType.XP.ordinal(), stackXp.copy()}));
                } else {
                    role.getSlot(j + (i * 8)).setSlotXp(null);
                }
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



