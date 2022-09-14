package su.metalabs.npc.common.packets;

import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import su.metalabs.npc.client.gui.GuiAdvancedTraderSetup;

@ElegantPacket
@Value
public class AdvancedTraderInventoryPacket implements ServerToClientPacket {
    NBTTagCompound nbt;

    @Override
    public void onReceive(Minecraft minecraft) {
        try {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiAdvancedTraderSetup) {
                GuiAdvancedTraderSetup gui = (GuiAdvancedTraderSetup) Minecraft.getMinecraft().currentScreen;
                gui.parseNBTContainer(nbt);
            }
        } catch (Exception ignored) {

        }
    }
}
