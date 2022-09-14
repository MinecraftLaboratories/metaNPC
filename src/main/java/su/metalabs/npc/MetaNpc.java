package su.metalabs.npc;

import su.metalabs.npc.proxy.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@Mod(modid = Reference.MOD_ID, version = Reference.VERSION, name = Reference.NAME, dependencies = Reference.DEPENDENCIES)
public class MetaNpc {
    public static final Logger logger = LogManager.getLogger(Reference.NAME);

    @Mod.Instance(Reference.MOD_ID)
    public static MetaNpc instance;
    @SidedProxy(clientSide = Reference.CLIENT_PROXY_LOCATION, serverSide = Reference.COMMON_PROXY_LOCATION)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @GradleSideOnly(GradleSide.SERVER)
    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
    }

    @GradleSideOnly(GradleSide.SERVER)
    @EventHandler
    public void serverStop(FMLServerStoppingEvent event) {

    }
}
