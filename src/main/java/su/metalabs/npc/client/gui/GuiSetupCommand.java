//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package su.metalabs.npc.client.gui;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.npc.common.roles.RoleCommand;

public class GuiSetupCommand extends GuiNPCInterface2 implements ITextfieldListener {
    private RoleCommand role;

    public GuiSetupCommand(EntityNPCInterface npc) {
        super(npc);
        super.ySize = 220;
        this.role = (RoleCommand)npc.roleInterface;
    }

    public void initGui() {
        super.initGui();
        super.buttonList.clear();
        super.drawDefaultBackground();

        this.addLabel(new GuiNpcLabel(0, "Команда которую игрок будет исполнять при клике", super.guiLeft + 10, super.guiTop + 10));
        this.addTextField(new GuiNpcTextField(1, this, super.guiLeft + 10, super.guiTop + 20, 180, 20, this.role.getCommand()));
    }

    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
    }

    public void save() {
        Client.sendData(EnumPacketServer.RoleSave, new Object[]{this.role.writeToNBT(new NBTTagCompound())});
    }

    public void unFocused(GuiNpcTextField guiNpcTextField) {
        switch (guiNpcTextField.id) {
            case 1: {
                String entered = guiNpcTextField.getText();
                if (!entered.equals(this.role.getCommand())) {
                    this.role.setCommand(entered);
                    save();
                }
                break;
            }
        }
    }

}
