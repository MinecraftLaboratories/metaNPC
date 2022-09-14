package su.metalabs.npc.common.listeners;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import su.metalabs.mobs.common.entity.base.EntityBoss;
import su.metalabs.mobs.common.entity.boss.EntitySkyGriffin;
import su.metalabs.npc.common.packets.SendFastSellPermissions;
import su.metalabs.npc.common.utils.FastSellUtils;

public class SendSellInfo {
    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        new SendFastSellPermissions(FastSellUtils.canFastSell(event.player)).sendToPlayer((EntityPlayerMP) event.player);
    }
}
