package su.metalabs.npc.common.packets;

import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ElegantPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import su.metalabs.lib.handlers.injection.PermissionHandler;
import su.metalabs.npc.common.utils.FastSellUtils;

@ElegantPacket
public class RequestFastSellPermissions implements ClientToServerPacket {
    @Override
    public void onReceive(EntityPlayerMP player) {
        new SendFastSellPermissions(FastSellUtils.canFastSell(player)).sendToPlayer(player);
    }
}
