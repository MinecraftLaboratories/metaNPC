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
import noppes.npcs.roles.RoleCommand;
import su.metalabs.npc.common.roles.RoleAttackXp;

public class GuiSetupAttackXp extends GuiNPCInterface2 implements ITextfieldListener {
    private RoleAttackXp role;

    public GuiSetupAttackXp(EntityNPCInterface npc) {
        super(npc);
        super.ySize = 220;
        this.role = (RoleAttackXp)npc.roleInterface;
    }

    public void initGui() {
        super.initGui();
        super.buttonList.clear();
        super.drawDefaultBackground();

        this.addLabel(new GuiNpcLabel(0, "Сколько опыта атаки давать за убийство", super.guiLeft + 10, super.guiTop + 10));
        this.addTextField(new GuiNpcTextField(1, this, super.guiLeft + 10, super.guiTop + 20, 180, 20, String.valueOf(this.role.getXp())));
        this.getTextField(1).numbersOnly = true;

        this.addLabel(new GuiNpcLabel(2, "Игнорировать дневной лимит опыта", super.guiLeft + 10, super.guiTop + 50));
        this.addButton(new GuiNpcButton(3, this.guiLeft + 10, this.guiTop + 60, 60, 20, new String[] { "gui.no", "gui.yes" }, this.role.isIgnoreLimit() ? 1 : 0));

        this.addLabel(new GuiNpcLabel(4, "Айди способности, при наличии которой,", super.guiLeft + 10, super.guiTop + 90));
        this.addLabel(new GuiNpcLabel(14, "дроп будет падать сразу игроку в инвентарь", super.guiLeft + 10, super.guiTop + 100));
        this.addTextField(new GuiNpcTextField(5, this, super.guiLeft + 10, super.guiTop + 110, 180, 20, this.role.getAbilityId() != null ? this.role.getAbilityId() : ""));

    }

    public void drawScreen(int i, int j, float f) {
        super.drawScreen(i, j, f);
    }

    public void actionPerformed(GuiButton guibutton) {
        switch (guibutton.id) {
            case 3: {
                boolean selectedIgnoreLimit = ((GuiNpcButton) guibutton).getValue() == 1;
                if(selectedIgnoreLimit != this.role.isIgnoreLimit()) {
                    this.role.setIgnoreLimit(selectedIgnoreLimit);
                    save();
                }
                break;
            }
        }


    }

    public void save() {
        Client.sendData(EnumPacketServer.RoleSave, new Object[]{this.role.writeToNBT(new NBTTagCompound())});
    }

    public void unFocused(GuiNpcTextField guiNpcTextField) {
        switch (guiNpcTextField.id) {
            case 1: {
                int entered = getXp(guiNpcTextField.getText());
                if (entered != this.role.getXp()) {
                    this.role.setXp(entered);
                    save();
                }
                break;
            }
            case 5: {
                if(guiNpcTextField.isEmpty()) {
                    break;
                }

                String entered = guiNpcTextField.getText();
                if (entered == null || !entered.equals(this.role.getAbilityId())) {
                    this.role.setAbilityId(entered);
                    save();
                }
                break;
            }
        }
    }

    public int getXp(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}
