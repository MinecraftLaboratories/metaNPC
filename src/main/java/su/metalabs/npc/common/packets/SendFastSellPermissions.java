package su.metalabs.npc.common.packets;

import hohserg.elegant.networking.api.ElegantPacket;
import hohserg.elegant.networking.api.ServerToClientPacket;
import lombok.Value;
import net.minecraft.client.Minecraft;
import su.metalabs.npc.proxy.ClientProxy;

@ElegantPacket
@Value
public class SendFastSellPermissions implements ServerToClientPacket {
    boolean allowed;

    @Override
    public void onReceive(Minecraft minecraft) {
        ClientProxy.isFastSellAllowed = allowed;
    }
}
