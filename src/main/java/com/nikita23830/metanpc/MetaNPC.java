package com.nikita23830.metanpc;

import com.nikita23830.metanpc.common.CommonProxy;
import com.nikita23830.metanpc.common.ItemSystem;
import com.nikita23830.metanpc.common.NetworkHandler;
import com.nikita23830.metanpc.common.packets.GuiHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.item.Item;
import noppes.npcs.constants.EnumRoleType;
import sun.reflect.ConstructorAccessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Mod(modid = MetaNPC.MODID, version = MetaNPC.VERSION, name = "Meta NPC", dependencies = "after:customnpcs")
public class MetaNPC {
    public static final String MODID = "metanpc";
    public static final String VERSION = "1.0";
    @Mod.Instance
    public static MetaNPC instance;
    @SidedProxy(
            serverSide = "com.nikita23830.metanpc.common.CommonProxy",
            clientSide = "com.nikita23830.metanpc.client.ClientProxy"
    )
    public static CommonProxy proxy;
    private final GuiHandler guiHandler = new GuiHandler();
    public static Item itemSystem;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
//        try {
//            addToEnum(EnumRoleType.class, "ADVTRADER", new Object[]{});
//            addToEnum(EnumRoleType.class, "ADVTRADERS", new Object[]{});
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        itemSystem = new ItemSystem();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, guiHandler);
        NetworkHandler.init();
    }

    static void addToEnum(Class<? extends Enum> clz, String newEnumName, Object[] data) throws Exception {
        Field VALUES = clz.getDeclaredField("$VALUES");
        Class<EnumRoleType> monsterClass = EnumRoleType.class;
        Constructor<?> constructor = monsterClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        Field constructorAccessorField = Constructor.class.getDeclaredField("constructorAccessor");
        constructorAccessorField.setAccessible(true);
        ConstructorAccessor ca = (ConstructorAccessor) constructorAccessorField.get(constructor);
        if (ca == null) {
            Method acquireConstructorAccessorMethod = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
            acquireConstructorAccessorMethod.setAccessible(true);
            ca = (ConstructorAccessor) acquireConstructorAccessorMethod.invoke(constructor);
        }
        Object[] dataEnum = new Object[2 + data.length];
        dataEnum[0] = newEnumName;
        dataEnum[1] = clz.getDeclaredFields().length;
        for (int j = 0; j < data.length; ++j) {
            dataEnum[2+j] = data[j];
        }
        EnumRoleType enumValue = (EnumRoleType) ca.newInstance(dataEnum);
        VALUES.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(VALUES, VALUES.getModifiers() & ~Modifier.FINAL);
        EnumRoleType[] oldValues = (EnumRoleType[]) VALUES.get(null);
        EnumRoleType[] newValues = new EnumRoleType[oldValues.length + 1];
        System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
        newValues[oldValues.length] = enumValue;
        VALUES.set(null, newValues);
    }
}
