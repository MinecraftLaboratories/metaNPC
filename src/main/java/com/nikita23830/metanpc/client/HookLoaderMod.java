package com.nikita23830.metanpc.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

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
        registerHookContainer("com.nikita23830.metanpc.client.FixContainerCrash");
    }
}

