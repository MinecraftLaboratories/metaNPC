package com.nikita23830.metanpc.common;

import com.nikita23830.metanpc.MetaNPC;
import com.nikita23830.metanpc.common.packets.ActualInventoryGMPacket;
import com.nikita23830.metanpc.common.packets.OpenGuiTrader;
import com.nikita23830.metanpc.common.packets.PacketBuy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE;

    public NetworkHandler() {
    }

    public static void init(){
        int idDescruminator = 0;
        INSTANCE.registerMessage(OpenGuiTrader.class, OpenGuiTrader.class, ++idDescruminator, Side.CLIENT);
        INSTANCE.registerMessage(ActualInventoryGMPacket.class, ActualInventoryGMPacket.class, ++idDescruminator, Side.CLIENT);
        INSTANCE.registerMessage(PacketBuy.class, PacketBuy.class, ++idDescruminator, Side.SERVER);
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MetaNPC.MODID.toLowerCase());
    }
}
