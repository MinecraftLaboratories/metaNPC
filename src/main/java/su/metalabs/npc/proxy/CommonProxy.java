package su.metalabs.npc.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.item.Item;
import noppes.npcs.constants.EnumRoleType;
import ru.justagod.cutter.invoke.Invoke;
import su.metalabs.lib.utils.EnumMetaUtils;
import su.metalabs.npc.MetaNpc;
import su.metalabs.npc.client.gui.GuiSetupAttackXp;
import su.metalabs.npc.common.ItemSetup;
import su.metalabs.npc.common.packets.GuiHandler;
import su.metalabs.npc.common.roles.RoleAdvancedTrader;
import su.metalabs.npc.common.roles.RoleAttackXp;
import su.metalabs.npc.common.roles.RoleCommand;

public class CommonProxy {
    public static Item setupItem;

    public static EnumRoleType advancedTraderRole;
    public static EnumRoleType attackXpRole;
    public static EnumRoleType commandRole;

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
        setupItem = new ItemSetup();
        NetworkRegistry.INSTANCE.registerGuiHandler(MetaNpc.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        try {
            advancedTraderRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "AdvancedTrader", new Object[]{RoleAdvancedTrader.class, null});
            attackXpRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "AttackXp", new Object[]{RoleAttackXp.class, null});
            commandRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "Command", new Object[]{RoleCommand.class, null});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
