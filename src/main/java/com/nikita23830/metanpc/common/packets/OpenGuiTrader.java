package com.nikita23830.metanpc.common.packets;

import com.nikita23830.metanpc.client.GuiAdvTrade;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.List;

public class OpenGuiTrader implements IMessage, IMessageHandler<OpenGuiTrader, IMessage> {
    private int buttonID;
    private double xc;
    private double yc;
    private double zc;

    public OpenGuiTrader() {}

    public OpenGuiTrader(int buttonID, double xc, double yc, double zc) {
        this.buttonID = buttonID;
        this.xc = xc;
        this.yc = yc;
        this.zc = zc;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
            return;
        this.buttonID = byteBuf.readInt();
        this.xc = byteBuf.readDouble();
        this.yc = byteBuf.readDouble();
        this.zc = byteBuf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        byteBuf.writeInt(this.buttonID);
        byteBuf.writeDouble(this.xc);
        byteBuf.writeDouble(this.yc);
        byteBuf.writeDouble(this.zc);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(OpenGuiTrader message, MessageContext messageContext) {
        try {
//            World world = Minecraft.getMinecraft().thePlayer.worldObj;
//            List<EntityNPCInterface> e = world.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((message.xc - 0.3D), (message.yc - 0.3D), (message.zc - 0.3D), (message.xc + 0.3D), (message.yc + 0.3D), (message.zc + 0.3D)));
//            if (e.size() != 1)
//                return null;
//            Minecraft.getMinecraft().displayGuiScreen(new GuiAdvTrade(e.get(0)));
        } catch (Exception exception) {
            //
        }
        return null;
    }
}
