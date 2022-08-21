package com.nikita23830.metanpc.client;

import com.nikita23830.metanpc.common.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.entity.EntityNPCInterface;

public class ClientProxy extends CommonProxy {
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

//    @SubscribeEvent
//    public void openGui(GuiOpenEvent event) {
//        if (event.gui != null && event.gui instanceof GuiNpcAdvanced) {
//            EntityNPCInterface npc = ((GuiNpcAdvanced)event.gui).npc;
//            event.gui = new GuiNpcAdvancedPatch(npc);
//        }
//    }
}
