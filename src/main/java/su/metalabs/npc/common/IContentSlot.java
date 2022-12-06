package su.metalabs.npc.common;

import su.metalabs.npc.common.roles.RoleAdvancedTrader;

public interface IContentSlot {
    public RoleAdvancedTrader.TradeType getType();

    public Object getData();
}
