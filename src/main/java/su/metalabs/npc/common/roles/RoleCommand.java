package su.metalabs.npc.common.roles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleInterface;
import su.metalabs.npc.common.packets.ExecuteCommandPacket;

public class RoleCommand extends RoleInterface {
    private String command = "";

    public RoleCommand(EntityNPCInterface npc) {
        super(npc);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setString("command", this.command);
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.command = nbttagcompound.getString("command");
    }

    public void interact(EntityPlayer player) {
        if(!player.worldObj.isRemote && !command.isEmpty()) {
            new ExecuteCommandPacket(getCommand()).sendToPlayer((EntityPlayerMP) player);
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
