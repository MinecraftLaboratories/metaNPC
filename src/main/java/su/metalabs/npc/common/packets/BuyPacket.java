package su.metalabs.npc.common.packets;

import com.nikita23830.metanpc.common.RoleAdvanceTrader;
import hohserg.elegant.networking.api.ClientToServerPacket;
import hohserg.elegant.networking.api.ElegantPacket;
import lombok.Value;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@ElegantPacket
@Value
public class BuyPacket implements ClientToServerPacket {
    private static HashMap<UUID, Long> clickCd = new HashMap<>();

    int nbt;
    double x;
    double y;
    double z;
    
    @Override
    public void onReceive(EntityPlayerMP player) {
        try {
            long current = System.currentTimeMillis();
            long prev = clickCd.getOrDefault(player.getUniqueID(), 0L);
            if ((prev + 200L) > current)  {
                return;
            }
            
            clickCd.put(player.getUniqueID(), current);
            List<EntityNPCInterface> e = player.worldObj.getEntitiesWithinAABB(EntityNPCInterface.class, AxisAlignedBB.getBoundingBox((x - 0.7D), (y - 0.7D), (z - 0.7D), (x + 0.7D), (y + 0.7D), (z + 0.7D)));
            if (e.size() == 1 && e.get(0).roleInterface instanceof RoleAdvanceTrader) {
                ((RoleAdvanceTrader)e.get(0).roleInterface).onTryBuy(nbt, player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
