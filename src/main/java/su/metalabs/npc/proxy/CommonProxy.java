package su.metalabs.npc.proxy;

import su.metalabs.lib.utils.RegistryUtils;
import su.metalabs.npc.MetaNpc;
import su.metalabs.npc.common.ItemSystem;
import su.metalabs.npc.common.listeners.SendSellInfo;
import su.metalabs.npc.common.packets.GuiHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.item.Item;

public class CommonProxy {
    private final GuiHandler guiHandler = new GuiHandler();
    public static Item itemSystem;
    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        itemSystem = new ItemSystem();
        NetworkRegistry.INSTANCE.registerGuiHandler(MetaNpc.instance, guiHandler);
        RegistryUtils.registerEventHandler(new SendSellInfo());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
