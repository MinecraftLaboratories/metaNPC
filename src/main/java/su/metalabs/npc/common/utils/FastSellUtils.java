package su.metalabs.npc.common.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.justagod.cutter.invoke.Invoke;
import su.metalabs.lib.handlers.injection.PermissionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

@UtilityClass
public class FastSellUtils {
    public static boolean canFastSell(EntityPlayerMP player) {
        AtomicBoolean canSell = new AtomicBoolean(false);

        Invoke.server(() -> {
            canSell.set(PermissionHandler.hasPermission(player, "oneblock.sell.all") || PermissionHandler.isOp(player));
        });

        return canSell.get();
    }
}
