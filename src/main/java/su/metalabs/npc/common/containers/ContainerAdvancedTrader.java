package su.metalabs.npc.common.containers;

import net.minecraft.entity.player.InventoryPlayer;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.lib.api.gui.MetaContainer;
import su.metalabs.lib.api.gui.SlotWrapper;

public class ContainerAdvancedTrader extends MetaContainer {
    private EntityNPCInterface npc;
    public ContainerAdvancedTrader(InventoryPlayer inventory, EntityNPCInterface npc) {
        super(inventory);
        this.npc = npc;

        float inventorySlotSize = 54;
        addInventory(inventory, new SlotWrapper((-inventorySlotSize * (9 / 2.0F)), 767, inventorySlotSize, true, true));
    }


}
