package su.metalabs.npc.common.containers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.lib.api.gui.MetaContainer;
import su.metalabs.lib.api.gui.SlotWrapper;
import su.metalabs.npc.common.utils.FastSellUtils;

public class ContainerAdvancedTrader extends MetaContainer {
    private EntityNPCInterface npc;
    private int hasFastSellPermissions;
    public ContainerAdvancedTrader(InventoryPlayer inventory, EntityNPCInterface npc, boolean isServer) {
        super(inventory);
        this.npc = npc;
        if(isServer) {
            this.hasFastSellPermissions = FastSellUtils.canFastSell((EntityPlayerMP) inventory.player) ? 1 : 0;
        }

        float inventorySlotSize = 54;
        addInventory(inventory, new SlotWrapper((-inventorySlotSize * (9 / 2.0F)), 767, inventorySlotSize, true, true));
    }

    public int getHasFastSellPermissions() {
        return hasFastSellPermissions;
    }

    public void addCraftingToCrafters(ICrafting crafting) {
        super.addCraftingToCrafters(crafting);
        crafting.sendProgressBarUpdate(this, 0, this.hasFastSellPermissions);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int craftingId, int value) {
        if (craftingId == 0) {
            this.hasFastSellPermissions = value;
        }
    }
}
