package su.metalabs.npc.common.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import su.metalabs.lib.handlers.injection.PermissionHandler;

@UtilityClass
public class FastSellUtils {
    public static boolean canFastSell(EntityPlayer player) {
        return PermissionHandler.hasPermission(player, "oneblock.sell.all") || PermissionHandler.isOp((EntityPlayerMP) player);
    }
}
