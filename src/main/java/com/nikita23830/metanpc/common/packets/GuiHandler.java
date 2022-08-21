package com.nikita23830.metanpc.common.packets;

import com.nikita23830.metanpc.client.GuiAdvTrade;
import com.nikita23830.metanpc.client.GuiAdvTradeGM;
import com.nikita23830.metanpc.common.containers.ContainerAdvTraderServer;
import com.nikita23830.metanpc.common.containers.ContainetAdvTraderServerGM;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.List;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        List<EntityNPCInterface> e = world.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((x - 0.7D), (y - 0.7D), (z - 0.7D), (x + 0.7D), (y + 0.7D), (z + 0.7D)));
        if (e.size() ==  1) {
            if (player.isSneaking() && player.capabilities.isCreativeMode)
                return new ContainetAdvTraderServerGM(player.inventory, e.get(0));
            return new ContainerAdvTraderServer(player.inventory, e.get(0));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        List<EntityNPCInterface> e = world.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((x - 0.7D), (y - 0.7D), (z - 0.7D), (x + 0.7D), (y + 0.7D), (z + 0.7D)));
        if (e.size() ==  1) {
            if (player.isSneaking() && player.capabilities.isCreativeMode)
                return new GuiAdvTradeGM(e.get(0), player);
            return new GuiAdvTrade(e.get(0), player);
        }
        return null;
    }
}
