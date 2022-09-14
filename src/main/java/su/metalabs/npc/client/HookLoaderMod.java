package su.metalabs.npc.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;
import su.metalabs.npc.Reference;

public class HookLoaderMod extends HookLoader {

    public static String[] classes = new String[]{
            "net.minecraft.inventory.Container"
    };

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{PrimaryClassTransformer.class.getName()};
    }

    @Override
    public void registerHooks() {
        registerHookContainer(Reference.MOD_GROUP + ".client.FixContainerCrash");
    }
}

