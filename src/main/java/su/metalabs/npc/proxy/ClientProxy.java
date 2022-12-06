package su.metalabs.npc.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import noppes.npcs.constants.EnumRoleType;
import ru.justagod.cutter.invoke.Invoke;
import su.metalabs.lib.utils.EnumMetaUtils;
import su.metalabs.npc.client.gui.GuiSetupAdvancedTrader;
import su.metalabs.npc.client.gui.GuiSetupAttackXp;
import su.metalabs.npc.client.gui.GuiSetupCommand;
import su.metalabs.npc.common.roles.RoleAdvancedTrader;
import su.metalabs.npc.common.roles.RoleAttackXp;
import su.metalabs.npc.common.roles.RoleCommand;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        //super.postInit(event);
        try {
            advancedTraderRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "AdvancedTrader", new Object[]{RoleAdvancedTrader.class, GuiSetupAdvancedTrader.class});
            attackXpRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "AttackXp", new Object[]{RoleAttackXp.class, GuiSetupAttackXp.class});
            commandRole = EnumMetaUtils.addToEnum(EnumRoleType.class, "Command", new Object[]{RoleCommand.class, GuiSetupCommand.class});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
