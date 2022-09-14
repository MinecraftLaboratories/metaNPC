package su.metalabs.npc.common;

import com.nikita23830.metanpc.common.RoleAdvanceTrader;

public interface IContentSlot {
    public RoleAdvanceTrader.TradeType getType();

    public Object getData();
}
