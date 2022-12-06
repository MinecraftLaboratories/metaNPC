//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package su.metalabs.npc.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.Client;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;
import su.metalabs.npc.common.roles.RoleAdvancedTrader;
import su.metalabs.npc.common.roles.RoleAttackXp;

public class GuiSetupAdvancedTrader extends GuiNPCInterface2 implements ITextfieldListener {
    private RoleAdvancedTrader role;

    public GuiSetupAdvancedTrader(EntityNPCInterface npc) {
        super(npc);
        super.ySize = 220;
        this.role = (RoleAdvancedTrader)npc.roleInterface;
    }

    public void initGui() {
        super.initGui();
        super.buttonList.clear();
        super.drawDefaultBackground();

        this.addLabel(new GuiNpcLabel(0, "Рисовать окно быстрой продажи", super.guiLeft + 10, super.guiTop + 50));
        this.addButton(new GuiNpcButton(1, this.guiLeft + 10, this.guiTop + 60, 60, 20, new String[] { "gui.no", "gui.yes" }, this.role.isDrawFastSell() ? 1 : 0));
    }

    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
    }

    public void actionPerformed(GuiButton guibutton) {
        switch (guibutton.id) {
            case 1: {
                boolean drawFastSell = ((GuiNpcButton) guibutton).getValue() == 1;
                if(drawFastSell != this.role.isDrawFastSell()) {
                    this.role.setDrawFastSell(drawFastSell);
                    save();
                }
                break;
            }
        }


    }

    public void save() {
        Client.sendData(EnumPacketServer.RoleSave, new Object[]{this.role.writeToNBT(new NBTTagCompound())});
    }

    @Override
    public void unFocused(GuiNpcTextField guiNpcTextField) {

    }
}
