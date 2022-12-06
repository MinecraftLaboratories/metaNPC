package su.metalabs.npc.common.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import su.metalabs.npc.client.gui.GuiAdvancedTraderSetup;

@ElegantPacket
@Value
public class ExecuteCommandPacket implements ServerToClientPacket {
    String command;

    @SideOnly(Side.CLIENT)
    @Override
    public void onReceive(Minecraft minecraft) {
        if(!command.isEmpty()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + command);
        }
    }
}
