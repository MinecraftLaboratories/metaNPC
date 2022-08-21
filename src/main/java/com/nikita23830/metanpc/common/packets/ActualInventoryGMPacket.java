package com.nikita23830.metanpc.common.packets;

import com.nikita23830.metanpc.client.GuiAdvTrade;
import com.nikita23830.metanpc.client.GuiAdvTradeGM;
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

public class ActualInventoryGMPacket implements IMessage, IMessageHandler<ActualInventoryGMPacket, IMessage> {
    private NBTTagCompound nbt;

    public ActualInventoryGMPacket() {}

    public ActualInventoryGMPacket(NBTTagCompound nbt) {
        this.nbt = nbt;
    }

    @Override
    public void fromBytes(ByteBuf byteBuf) {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER)
            return;
        this.nbt = ByteBufUtils.readTag(byteBuf);
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        ByteBufUtils.writeTag(byteBuf, this.nbt);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(ActualInventoryGMPacket message, MessageContext messageContext) {
        try {
            EntityPlayer ep = Minecraft.getMinecraft().thePlayer;
            if (Minecraft.getMinecraft().currentScreen instanceof GuiAdvTradeGM) {
                GuiAdvTradeGM gui = (GuiAdvTradeGM) Minecraft.getMinecraft().currentScreen;
                gui.parseNBTContainer(message.nbt);
            }
        } catch (Exception e) {

        }
        return null;
    }
}
