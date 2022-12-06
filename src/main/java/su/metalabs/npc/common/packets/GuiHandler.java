package su.metalabs.npc.common.packets;

import net.minecraft.entity.Entity;
import su.metalabs.npc.client.gui.GuiAdvancedTrader;
import su.metalabs.npc.client.gui.GuiAdvancedTraderSetup;
import su.metalabs.npc.common.containers.ContainerAdvancedTrader;
import su.metalabs.npc.common.containers.ContainerAdvancedTraderSetup;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.List;

public class GuiHandler implements IGuiHandler {
    @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            List<EntityNPCInterface> e = world.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((x - 0.7D), (y - 0.7D), (z - 0.7D), (x + 0.7D), (y + 0.7D), (z + 0.7D)));
            if (e.size() ==  1) {
                if (((Entity)player).isSneaking() && player.capabilities.isCreativeMode) {
                    return new ContainerAdvancedTraderSetup(player.inventory, e.get(0));
                }

                return new ContainerAdvancedTrader(player.inventory, e.get(0), true);
            }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        List<EntityNPCInterface> e = world.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((x - 0.7D), (y - 0.7D), (z - 0.7D), (x + 0.7D), (y + 0.7D), (z + 0.7D)));
        if (e.size() ==  1) {
            if (((Entity)player).isSneaking() && player.capabilities.isCreativeMode) {
                return new GuiAdvancedTraderSetup(player.inventory, e.get(0));
            }


            return new GuiAdvancedTrader(player.inventory, e.get(0));
        }
        return null;
    }
}
