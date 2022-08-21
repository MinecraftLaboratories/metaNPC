package com.nikita23830.metanpc.common.packets;

import com.nikita23830.metanpc.client.GuiAdvTradeGM;
import com.nikita23830.metanpc.common.RoleAdvanceTrader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PacketBuy implements IMessage, IMessageHandler<PacketBuy, IMessage> {
    private int nbt;
    private double x;
    private double y;
    private double z;
    private static HashMap<UUID, Long> clickCd = new HashMap<>();

    public PacketBuy() {}

    public PacketBuy(int nbt, double x, double y, double z) {
        this.nbt = nbt;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        this.nbt = byteBuf.readInt();
        this.x = byteBuf.readDouble();
        this.y = byteBuf.readDouble();
        this.z = byteBuf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.nbt);
        byteBuf.writeDouble(this.x);
        byteBuf.writeDouble(this.y);
        byteBuf.writeDouble(this.z);
    }

    @Override
    public IMessage onMessage(PacketBuy message, MessageContext messageContext) {
        try {
            long current = System.currentTimeMillis();
            long prev = clickCd.getOrDefault(messageContext.getServerHandler().playerEntity.getUniqueID(), 0L);
            if ((prev + 200L) > current)
                return null;
            clickCd.put(messageContext.getServerHandler().playerEntity.getUniqueID(), current);
            List<EntityNPCInterface> e = messageContext.getServerHandler().playerEntity.worldObj.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((message.x - 0.7D), (message.y - 0.7D), (message.z - 0.7D), (message.x + 0.7D), (message.y + 0.7D), (message.z + 0.7D)));
            if (e.size() == 1 && e.get(0).roleInterface instanceof RoleAdvanceTrader) {
                ((RoleAdvanceTrader)e.get(0).roleInterface).onTryBuy(message.nbt, messageContext.getServerHandler().playerEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
