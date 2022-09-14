package com.nikita23830.metanpc.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.roles.RoleInterface;
import su.metalabs.lib.handlers.currency.CurrencyHandler;
import su.metalabs.lib.handlers.data.ClientDataHandler;
import su.metalabs.lib.handlers.injection.BalanceHandler;
import su.metalabs.lib.handlers.injection.SkillsHandler;
import su.metalabs.lib.utils.InventoryUtils;
import su.metalabs.npc.MetaNpc;
import su.metalabs.npc.common.IContentSlot;
import su.metalabs.npc.common.ItemSystem;
import su.metalabs.npc.common.packets.BuyPacket;
import su.metalabs.npc.proxy.CommonProxy;

import java.util.ArrayList;
import java.util.List;

public class RoleAdvanceTrader extends RoleInterface {
    private boolean ignoreMeta = false;
    private boolean ignoreNBT = false;
    private TradeSlot[] slots = new TradeSlot[24];

    public RoleAdvanceTrader(EntityNPCInterface npc) {
        super(npc);
        for (int i = 0; i < 24; ++i)
            slots[i] = new TradeSlot();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound data = new NBTTagCompound();
        data.setBoolean("meta", ignoreMeta);
        data.setBoolean("nbt", ignoreNBT);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < 24; ++i) {
            list.appendTag(slots[i].writeNBT());
        }
        data.setTag("slots", list);
        nbtTagCompound.setTag("data", data);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        NBTTagCompound data = nbtTagCompound.getCompoundTag("data");
        ignoreNBT = data.getBoolean("nbt");
        ignoreMeta = data.getBoolean("meta");
        NBTTagList list = data.getTagList("slots", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            slots[i].readNBT(list.getCompoundTagAt(i));
        }
    }

    public String getMessageFromSlot(int slot, EntityPlayer player) {
        if (slot >= 0 && slot < 24)
            return slots[slot].getMessageFromSlot(player, this);
        return "trade.unknownSlot";
    }

    public TradeSlot getSlot(int slot) {
        return slots[slot];
    }

    @Override
    public void interact(EntityPlayer entityPlayer) {
        if (entityPlayer.worldObj.isRemote)
            return;
        entityPlayer.openGui(MetaNpc.instance, 0, entityPlayer.worldObj, MathHelper.floor_double(npc.posX), MathHelper.floor_double(npc.posY), MathHelper.floor_double(npc.posZ));
        Server.sendData((EntityPlayerMP) entityPlayer, EnumPacketClient.GUI_DATA, npc.roleInterface.writeToNBT(new NBTTagCompound()));
    }

    public void onTryBuy(int slot, EntityPlayer player) {
        if (player.worldObj.isRemote) {
            new BuyPacket(slot, this.npc.posX, this.npc.posY, this.npc.posZ).sendToServer();
            return;
        }
        if (slot >= 0 && slot < 24) {
            NBTTagList list = new NBTTagList();
            player.inventory.writeToNBT(list);
            TradeSlot slot1 = getSlot(slot);
            for (int i = 0; i < 2; ++i) {
                if (slot1.getSlotIn(i) != null && slot1.getSlotIn(i).execute(player, this)) {
                    continue;
                } else if (slot1.getSlotIn(i) != null) {
                    player.inventory.readFromNBT(list);
                    if (i == 0)
                        return;
                    else
                        payback(player, slot1.getSlotIn(0));
                    return;
                }
            }
            for (int i = 0; i < 2; ++i) {
                if (slot1.getSlotOut(i) != null)
                    slot1.getSlotOut(i).execute(player, this);
            }

            if(slot1.getSlotXp() != null) {
                slot1.getSlotXp().execute(player, this);
            }
        }
    }

    private void payback(EntityPlayer player, TradeSlotIn in) {
        if (in != null && in.getType() != TradeType.STACK) {
            int to = (int) in.data;
            BalanceHandler.deposit(player.getCommandSenderName(), to, in.type == TradeType.STACK ? CurrencyHandler.getGoldCurrency() : in.type == TradeType.GEMS ? CurrencyHandler.getGemCurrency() : CurrencyHandler.getRubCurrency());
        }
    }

    public static class TradeSlot {
        private TradeSlotIn[] slotIn = new TradeSlotIn[2];
        private TradeSlotOut[] slotOut = new TradeSlotOut[2];

        private TradeSlotXp slotXp = new TradeSlotXp();

        public TradeSlot() {}

        public NBTTagCompound writeNBT() {
            NBTTagCompound n = new NBTTagCompound();
            NBTTagList listIn = new NBTTagList();
            for (TradeSlotIn in : slotIn) {
                if (in != null)
                    listIn.appendTag(in.writeNBT());
            }
            n.setTag("in", listIn);
            NBTTagList listOut = new NBTTagList();
            for (TradeSlotOut out : slotOut) {
                if (out != null)
                    listOut.appendTag(out.writeNBT());
            }
            n.setTag("out", listOut);

            NBTTagList listXp = new NBTTagList();
            if (slotXp != null)
                listXp.appendTag(slotXp.writeNBT());
            n.setTag("xp", listXp);

            return n;
        }

        public void readNBT(NBTTagCompound n) {
            NBTTagList list = n.hasKey("in") ? n.getTagList("in", 10) : new NBTTagList();
            slotIn[0] = null;
            slotIn[1] = null;
            slotOut[0] = null;
            slotOut[1] = null;
            slotXp = null;
            for (int i = 0; i < list.tagCount(); ++i) {
                if (slotIn[i] == null)
                    slotIn[i] = new TradeSlotIn();
                slotIn[i].readNBT(list.getCompoundTagAt(i));
            }
            list = n.hasKey("out") ? n.getTagList("out", 10) : new NBTTagList();
            for (int i = 0; i < list.tagCount(); ++i) {
                if (slotOut[i] == null)
                    slotOut[i] = new TradeSlotOut();
                slotOut[i].readNBT(list.getCompoundTagAt(i));
            }

            list = n.hasKey("xp") ? n.getTagList("xp", 10) : new NBTTagList();
            for (int i = 0; i < list.tagCount(); ++i) {
                if (slotXp == null)
                    slotXp = new TradeSlotXp();
                slotXp.readNBT(list.getCompoundTagAt(0));
            }
        }

        public String getMessageFromSlot(EntityPlayer player, RoleAdvanceTrader role) {
            TradeSlotIn in0 = getSlotIn(0);
            TradeSlotIn in1 = getSlotIn(1);
            if (in0 == null && in1 == null)
                return CanTradeEnum.YES.message;
            Inventory inventory = new Inventory(36, "", "");
            inventory.putFromInventoryPlayer(player.inventory);
            if (in0 == null || in1 == null) {
                TradeSlotIn in;
                if (in0 == null)
                    in = in1;
                else
                    in = in0;
                return TradeType.can(in, inventory, role.ignoreMeta, role.ignoreNBT).message;
            } else {
                return TradeType.can(in0, in1, inventory, role.ignoreMeta, role.ignoreNBT).message;
            }
        }

        public void setSlotIn(int slot, TradeSlotIn in) {
            this.slotIn[slot] = in;
        }

        public void setSlotOut(int slot, TradeSlotOut out) {
            this.slotOut[slot] = out;
        }

        public void setSlotXp(TradeSlotXp xp) {
            this.slotXp = xp;
        }

        public TradeSlotXp getSlotXp() {
            return slotXp;
        }

        public TradeSlotIn getSlotIn(int slot) {
            return this.slotIn[slot];
        }

        public TradeSlotOut getSlotOut(int slot) {
            return this.slotOut[slot];
        }

        public List<String> genTooltip(CanTradeEnum can) {
            ArrayList<String> strings = new ArrayList<>();
            strings.add("§dТы отдаёшь:");
            if (getSlotIn(0) != null)
                strings.add(getSlotIn(0).genTooltip());
            if (getSlotIn(1) != null)
                strings.add(getSlotIn(1).genTooltip());
            strings.add("");
            strings.add("§aВзамен получаешь:");
            if (getSlotOut(0) != null)
                strings.add(getSlotOut(0).genTooltip());
            if (getSlotOut(1) != null)
                strings.add(getSlotOut(1).genTooltip());
            if (getSlotXp() != null && getSlotXp().getXpAmount() != 0) {
                strings.add("§7- §aОпыт торговли §7" + getSlotXp().getXpAmount() + " ед.");
            }
            strings.add("");
            if (can == CanTradeEnum.YES)
                strings.add("§9Кликни для покупки");
            else {
                strings.add("§cНе хватает ресурсов");
            }
            return strings;
        }
    }

    public static class TradeSlotXp implements IContentSlot {
        public Object data;

        @Override
        public TradeType getType() {
            return TradeType.XP;
        }

        public Object getData() {
            return data;
        }

        public TradeSlotXp() {}

        public TradeSlotXp(Object[] data) {
            try {
                this.data = data[1];
                return;
            } catch (Exception ignored) {

            }

            this.data = new ItemStack(Blocks.stone);

        }

        public ItemStack getStackToGM() {
            return (ItemStack) this.data;
        }

        public int getXpAmount() {
            if(data instanceof ItemStack) {
                ItemStack is = (ItemStack) data;
                return is.stackSize;
            }

            return 0;
        }

        public NBTTagCompound writeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            if(data != null) {
                NBTTagCompound n = new NBTTagCompound();
                ((ItemStack) data).writeToNBT(n);
                nbt.setTag("STACK", n);
            }
            return nbt;
        }

        public void readNBT(NBTTagCompound nbt) {
            NBTTagCompound n = nbt.getCompoundTag("STACK");
            this.data = ItemStack.loadItemStackFromNBT(n);
        }


        public boolean execute(EntityPlayer player, RoleAdvanceTrader role) {
            return SkillsHandler.giveSkillXp(player.getCommandSenderName(), "trading", getXpAmount(), false);
        }

        private boolean equalsNBT(ItemStack a, ItemStack b) {
            if (!a.hasTagCompound() && !b.hasTagCompound())
                return true;
            if (a.hasTagCompound() && a.getTagCompound().hasNoTags() && !b.hasTagCompound())
                return true;
            if (b.hasTagCompound() && b.getTagCompound().hasNoTags() && !a.hasTagCompound())
                return true;
            if (a.hasTagCompound() && b.hasTagCompound() && a.equals(b))
                return true;
            return false;
        }
    }


    public static class TradeSlotIn implements IContentSlot {
        private TradeType type;
        public Object data;

        public Object getData() {
            return data;
        }

        public TradeSlotIn() {}

        public TradeSlotIn(Object[] data) {
            if (data.length != 2) {
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
                return;
            }
            try {
                type = TradeType.values()[(int) data[0]];
                this.data = data[1];
                calculated();
            } catch (Exception e) {
                e.printStackTrace();
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
            }
        }

        public void calculated() {
            if (data != null && data instanceof ItemStack && ((ItemStack)data).getItem() == CommonProxy.itemSystem) {
                ItemStack t = (ItemStack) data;
                if (ItemSystem.getRub(t) != -1) {
                    type = TradeType.RUB;
                    data = ItemSystem.getRub(t);
                    return;
                }
                if (ItemSystem.getGem(t) != -1) {
                    type = TradeType.GEMS;
                    data = ItemSystem.getGem(t);
                    return;
                }
                if (ItemSystem.getGold(t) != -1) {
                    type = TradeType.COINS;
                    data = ItemSystem.getGold(t);
                    return;
                }
                if (type == TradeType.STACK) {
                    data = new ItemStack(Blocks.bedrock);
                }
            }
        }

        public ItemStack getStackToGM() {
            switch (type) {
                case STACK: return (ItemStack) this.data;
                case GEMS:
                case RUB:
                case COINS:
                    ItemStack st = new ItemStack(CommonProxy.itemSystem);
                    st.setItemDamage(type == TradeType.RUB ? 0 : type == TradeType.GEMS ? 1 : 2);
                    st.setStackDisplayName(Integer.toString((int) data));
                    return st;
            }
            return new ItemStack(Blocks.bedrock);
        }

        public NBTTagCompound writeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            switch (type) {
                case COINS:
                case RUB:
                case GEMS:
                    nbt.setInteger(type.name(), (int) data);
                    break;
                case STACK:
                    NBTTagCompound n = new NBTTagCompound();
                    ((ItemStack)data).writeToNBT(n);
                    nbt.setTag("STACK", n);
                    break;
            }
            return nbt;
        }

        public void readNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("COINS")) {
                this.type = TradeType.COINS;
                this.data = nbt.getInteger("COINS");
            } else if (nbt.hasKey("GEMS")) {
                this.type = TradeType.GEMS;
                this.data = nbt.getInteger("GEMS");
            } else if (nbt.hasKey("RUB")) {
                this.type = TradeType.RUB;
                this.data = nbt.getInteger("RUB");
            } else if (nbt.hasKey("STACK")) {
                NBTTagCompound n = nbt.getCompoundTag("STACK");
                this.type = TradeType.STACK;
                this.data = ItemStack.loadItemStackFromNBT(n);
            } else {
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
            }
        }

        public TradeType getType() {
            return type;
        }

        public String genTooltip() {
            switch (type) {
                case STACK:
                    return "§7- §f" + ((ItemStack)data).getDisplayName() + " §7" + ((ItemStack)data).stackSize + "§7шт.";
                case COINS:
                    return "§7- §" + CurrencyHandler.getGoldCurrency().getCurrencyDisplayColor() + CurrencyHandler.getGoldCurrency().getCurrencyName() + " §7" + data + " шт.";
                case GEMS:
                    return "§7- §" + CurrencyHandler.getGemCurrency().getCurrencyDisplayColor() + CurrencyHandler.getGemCurrency().getCurrencyName() + " §7" + data + " шт.";
                case RUB:
                    return "§7- §" + CurrencyHandler.getRubCurrency().getCurrencyDisplayColor() + CurrencyHandler.getRubCurrency().getCurrencyName() + " §7" + data + " шт.";
            }
            return "";
        }

        public boolean execute(EntityPlayer player, RoleAdvanceTrader role) {
            switch (type) {
                case STACK: {
                    int sleek = 0;
                    ItemStack cln = ((ItemStack) data).copy();
                    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                        ItemStack st = player.inventory.getStackInSlot(i);
                        if (st != null && st.getItem() == cln.getItem() && (role.ignoreMeta || (st.getItemDamage() == cln.getItemDamage())) && (role.ignoreNBT || equalsNBT(st, cln))) {
                            int can = st.stackSize;
                            if (cln.stackSize > can) {
                                sleek += st.stackSize;
                                cln.stackSize -= sleek;
                                player.inventory.setInventorySlotContents(i, null);
                            } else {
                                st.stackSize -= cln.stackSize;
                                if (st.stackSize <= 0)
                                    st = null;
                                player.inventory.setInventorySlotContents(i, st);
                                return true;
                            }
                        }

                        if (cln.stackSize <= 0) {
                            return true;
                        }
                    }
                    if (cln.stackSize > 0) {
                        cln.stackSize = sleek;
                        return false;
                    }
                    return true;
                }
                case GEMS: {
                    if (!BalanceHandler.withdraw(player.getCommandSenderName(), (int) data, CurrencyHandler.getGemCurrency()))
                        return false;
                    return true;
                }
                case COINS: {
                    if (!BalanceHandler.withdraw(player.getCommandSenderName(), (int) data, CurrencyHandler.getGoldCurrency()))
                        return false;
                    return true;
                }
                case RUB: {
                    if (!BalanceHandler.withdraw(player.getCommandSenderName(), (int) data, CurrencyHandler.getRubCurrency()))
                        return false;
                    return true;
                }
            }
            return false;
        }

        private boolean equalsNBT(ItemStack a, ItemStack b) {
            if (!a.hasTagCompound() && !b.hasTagCompound())
                return true;
            if (a.hasTagCompound() && a.getTagCompound().hasNoTags() && !b.hasTagCompound())
                return true;
            if (b.hasTagCompound() && b.getTagCompound().hasNoTags() && !a.hasTagCompound())
                return true;
            if (a.hasTagCompound() && b.hasTagCompound() && a.equals(b))
                return true;
            return false;
        }
    }

    public static class TradeSlotOut implements IContentSlot {
        private TradeType type;
        public Object data;

        public TradeSlotOut() {}

        public TradeSlotOut(Object[] data) {
            if (data.length != 2) {
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
                return;
            }
            try {
                type = TradeType.values()[(int) data[0]];
                this.data = data[1];
                calculated();
            } catch (Exception e) {
                e.printStackTrace();
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
            }
        }

        public Object getData() {
            return data;
        }

        public void calculated() {
            if (data != null && data instanceof ItemStack && ((ItemStack)data).getItem() == CommonProxy.itemSystem) {
                ItemStack t = (ItemStack) data;
                if (ItemSystem.getRub(t) != -1) {
                    type = TradeType.RUB;
                    data = ItemSystem.getRub(t);
                    return;
                }
                if (ItemSystem.getGem(t) != -1) {
                    type = TradeType.GEMS;
                    data = ItemSystem.getGem(t);
                    return;
                }
                if (ItemSystem.getGold(t) != -1) {
                    type = TradeType.COINS;
                    data = ItemSystem.getGold(t);
                    return;
                }
                if (type == TradeType.STACK) {
                    data = new ItemStack(Blocks.bedrock);
                }
            }
        }

        public ItemStack getStackToGM() {
            switch (type) {
                case STACK: return (ItemStack) this.data;
                case GEMS:
                case RUB:
                case COINS:
                    ItemStack st = new ItemStack(CommonProxy.itemSystem);
                    st.setItemDamage(type == TradeType.RUB ? 0 : type == TradeType.GEMS ? 1 : 2);
                    st.setStackDisplayName(Integer.toString((int) data));
                    return st;
            }
            return new ItemStack(Blocks.bedrock);
        }

        public NBTTagCompound writeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            switch (type) {
                case COINS:
                case RUB:
                case GEMS:
                    nbt.setInteger(type.name(), (int) data);
                    break;
                case STACK:
                    NBTTagCompound n = new NBTTagCompound();
                    ((ItemStack)data).writeToNBT(n);
                    nbt.setTag("STACK", n);
                    break;
            }
            return nbt;
        }

        public void readNBT(NBTTagCompound nbt) {
            if (nbt.hasKey("COINS")) {
                this.type = TradeType.COINS;
                this.data = nbt.getInteger("COINS");
            } else if (nbt.hasKey("GEMS")) {
                this.type = TradeType.GEMS;
                this.data = nbt.getInteger("GEMS");
            } else if (nbt.hasKey("RUB")) {
                this.type = TradeType.RUB;
                this.data = nbt.getInteger("RUB");
            } else if (nbt.hasKey("STACK")) {
                NBTTagCompound n = nbt.getCompoundTag("STACK");
                this.type = TradeType.STACK;
                this.data = ItemStack.loadItemStackFromNBT(n);
            } else {
                type = TradeType.STACK;
                this.data = new ItemStack(Blocks.stone);
            }
        }

        public TradeType getType() {
            return type;
        }

        public String genTooltip() {
            switch (type) {
                case STACK:
                    return "§7- §f" + ((ItemStack)data).getDisplayName() + " §7" + ((ItemStack)data).stackSize + "§7шт.";
                case COINS:
                    return "§7- §" + CurrencyHandler.getGoldCurrency().getCurrencyDisplayColor() + CurrencyHandler.getGoldCurrency().getCurrencyName() + " §7" + data + " шт.";
                case GEMS:
                    return "§7- §" + CurrencyHandler.getGemCurrency().getCurrencyDisplayColor() + CurrencyHandler.getGemCurrency().getCurrencyName() + " §7" + data + " шт.";
                case RUB:
                    return "§7- §" + CurrencyHandler.getRubCurrency().getCurrencyDisplayColor() + CurrencyHandler.getRubCurrency().getCurrencyName() + " §7" + data + " шт.";
            }
            return "";
        }

        public void execute(EntityPlayer player, RoleAdvanceTrader role) {
            switch (type) {
                case STACK: {
                    ItemStack a = ((ItemStack) data).copy();
                    InventoryUtils.giveItem(player, a);
                    break;
                }
                case RUB:
                    BalanceHandler.deposit(player.getCommandSenderName(), (int) data, CurrencyHandler.getRubCurrency());
                    break;
                case COINS:
                    BalanceHandler.deposit(player.getCommandSenderName(), (int) data, CurrencyHandler.getGoldCurrency());
                    break;
                case GEMS:
                    BalanceHandler.deposit(player.getCommandSenderName(), (int) data, CurrencyHandler.getGemCurrency());
                    break;
            }
        }
    }

    public enum TradeType {
        COINS,
        GEMS,
        RUB,
        XP,
        STACK;

        private static int canF(TradeSlotIn in, Inventory inventory, boolean meta, boolean nbt, boolean doit) {
            if (in.type == TradeType.COINS) {
                return ((int) in.data <= ClientDataHandler.getIntDataValue(CurrencyHandler.getGoldCurrency().getCurrencyId())) ? 0 : 1 ; // TODO 1
            } else if (in.type == TradeType.GEMS)
                return ((int) in.data <= ClientDataHandler.getIntDataValue(CurrencyHandler.getGemCurrency().getCurrencyId())) ? 0 : 2; // TODO 2
            else if (in.type == TradeType.RUB)
                return ((int) in.data <= ClientDataHandler.getIntDataValue(CurrencyHandler.getRubCurrency().getCurrencyId())) ? 0 : 3; // TODO 3
            else if (in.type == STACK) {
                return inventory.decrStack(((ItemStack) in.data).copy(), meta, nbt, doit) ? 0 : 4;
            }
            return 5;
        }

        public static CanTradeEnum can(TradeSlotIn in, Inventory inventory, boolean meta, boolean nbt) {
            return CanTradeEnum.values()[canF(in, inventory, meta, nbt, false)];
        }

        public static CanTradeEnum can(TradeSlotIn in0, TradeSlotIn in1, Inventory inventory, boolean meta, boolean nbt) {
            NBTTagList save = new NBTTagList();
            inventory.writeBaseInventoryNBT(save);
            int i0 = canF(in0, inventory, meta, nbt, true);
            int i1 = canF(in1, inventory, meta, nbt, true);
            inventory.readBaseInventoryNBT(save);
            if (i0 == 0 && i1 == 0)
                return CanTradeEnum.YES;
            if (i0 != 0)
                return CanTradeEnum.values()[i0];
            else
                return CanTradeEnum.values()[i1];
        }
    }

    public enum CanTradeEnum {
        YES(""),
        NO_COINS("trade.no_coins"),
        NO_GEMS("trade.no_gams"),
        NO_RUB("trade.no_rub"),
        NO_STACK("trade.no_stack"),
        UNKNOWNERROR("trade.unknownSlot");

        public final String message;
        private CanTradeEnum(String msg) {
            this.message = msg;
        }

        public static CanTradeEnum getEnumByMessage(String msg) {
            for (CanTradeEnum en : values()) {
                if (en.message.equals(msg))
                    return en;
            }
            return null;
        }
    }

    public static class Inventory implements IInventory {
        public ItemStack[] inventory;
        public int slotCount;
        public int maxStackSize = 64;
        public final String name;
        public final String playerName;
        private boolean online;

        public Inventory(int countSlots, String name, String player) {
            this.slotCount = countSlots;
            this.inventory = new ItemStack[countSlots];
            this.name = name;
            this.playerName = player;

        }

        @Override
        public int getSizeInventory() {
            return slotCount;
        }

        @Override
        public ItemStack getStackInSlot(int ind) {
            return inventory[ind];
        }

        @Override
        public ItemStack decrStackSize(int idSlot, int stackSize) {
            if(this.inventory[idSlot] != null) {
                ItemStack itemstack;
                if(this.inventory[idSlot].stackSize <= stackSize) {
                    itemstack = this.inventory[idSlot];
                    this.inventory[idSlot] = null;
                    this.markDirty();
                    return itemstack;
                } else {
                    itemstack = this.inventory[idSlot].splitStack(stackSize);
                    if(this.inventory[idSlot].stackSize == 0) {
                        this.inventory[idSlot] = null;
                    }

                    this.markDirty();
                    return itemstack;
                }
            } else {
                return null;
            }
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int index) {
            if(this.inventory[index] != null) {
                ItemStack itemstack = this.inventory[index];
                this.inventory[index] = null;
                return itemstack;
            } else {
                return null;
            }
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack item) {
            this.inventory[index] = item;
            if(item != null && item.stackSize > this.getInventoryStackLimit()) {
                item.stackSize = this.getInventoryStackLimit();
            }

            this.markDirty();
        }

        @Override
        public String getInventoryName() {
            return this.name;
        }

        @Override
        public boolean hasCustomInventoryName() {
            return true;
        }

        @Override
        public int getInventoryStackLimit() {
            return maxStackSize;
        }

        @Override
        public void markDirty() {

        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
            return true;
        }

        @Override
        public void openInventory() {}

        public void closeInventory() {

        }

        @Override
        public boolean isItemValidForSlot(int slot, ItemStack stack) {
            if (slot < 4)
                return true;
            return false;
        }

        public NBTTagCompound writeToNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            NBTTagList tagList = new NBTTagList();

            for(int i = 0; i < 4; ++i) {
                if (this.inventory[i] != null) {
                    NBTTagCompound invSlot = new NBTTagCompound();
                    invSlot.setByte("Slot", (byte)i);
                    this.inventory[i].writeToNBT(invSlot);
                    tagList.appendTag(invSlot);
                }
            }

            nbt.setTag("Baubles.Inventory", tagList);
            return nbt;
        }

        public void readFromNBT(NBTTagCompound tags) {
            NBTTagList tagList = tags.getTagList("Baubles.Inventory", 10);

            for(int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = tagList.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
                if (itemstack != null) {
                    this.inventory[j] = itemstack;
                }
            }
        }

        public void readBaseInventoryNBT(NBTTagList p_70443_1_) {
            for (int i = 0; i < p_70443_1_.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = p_70443_1_.getCompoundTagAt(i);
                int j = nbttagcompound.getByte("Slot") & 255;
                ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);

                if (itemstack != null) {
                    if (j >= 0 && j < this.inventory.length) {
                        this.inventory[j] = itemstack;
                    }

                    if (j >= 100 && j < 104) {
                        this.inventory[27 + j - 100] = itemstack;
                    }
                }
            }
        }

        public void writeBaseInventoryNBT(NBTTagList p_70443_1_) {
            for (int i = 0; i < getSizeInventory(); ++i) {
                if (this.inventory[i] != null) {
                    NBTTagCompound invSlot = new NBTTagCompound();
                    invSlot.setByte("Slot", (byte)i);
                    this.inventory[i].writeToNBT(invSlot);
                    p_70443_1_.appendTag(invSlot);
                }
            }
        }

        public boolean isEmpty() {
            for (ItemStack s : inventory) {
                if (s != null)
                    return false;
            }
            return true;
        }

        public void putFromInventoryPlayer(InventoryPlayer player) {
            for (int i = 0; i < 36; ++i) {
                if (player.getStackInSlot(i) != null)
                    setInventorySlotContents(i, player.getStackInSlot(i).copy());
            }
        }

        public boolean decrStack(ItemStack stack, boolean meta, boolean nbt, boolean doit) {
            NBTTagList list = new NBTTagList();
            writeBaseInventoryNBT(list);
            for (int i = 0; i < 36; ++i) {
                ItemStack st = getStackInSlot(i);
                if (st != null && equalsStack(stack, st, meta, nbt)) {
                    int canTake = st.stackSize;
                    if (stack.stackSize > canTake) {
                        stack.stackSize -= canTake;
                        setInventorySlotContents(i, null);
                    } else if (stack.stackSize == canTake) {
                        stack.stackSize = 0;
                        setInventorySlotContents(i, null);
                    } else {
                        st.stackSize -= stack.stackSize;
                        setInventorySlotContents(i, st);
                        stack.stackSize = 0;
                    }
                }
            }

            if (stack.stackSize == 0) {
                if (doit)
                    return true;
                else {
                    readBaseInventoryNBT(list);
                    return true;
                }
            } else {
                readBaseInventoryNBT(list);
                return false;
            }
        }

        private boolean equalsStack(ItemStack a, ItemStack b, boolean meta, boolean nbt) {
            boolean mt = false;
            boolean n = false;
            if (a.getItem() == b.getItem()) {
                if (meta)
                    mt = true;
                else if (a.getItemDamage() == b.getItemDamage())
                    mt = true;
                else
                    return false;
                if (nbt)
                    n = true;
                else {
                    if (!a.hasTagCompound() && !b.hasTagCompound())
                        n = true;
                    else if (!a.hasTagCompound() && b.hasTagCompound() && b.getTagCompound().hasNoTags())
                        n = true;
                    else if (!b.hasTagCompound() && a.hasTagCompound() && a.getTagCompound().hasNoTags())
                        n= true;
                    else if (a.hasTagCompound() && b.hasTagCompound() && ItemStack.areItemStackTagsEqual(a, b))
                        n = true;
                }
                if (mt && n)
                    return true;
            }
            return false;
        }

        public Inventory copy() {
            Inventory inv = new Inventory(36, "", "");
            for (int i = 0; i < 36; ++i)
                inv.setInventorySlotContents(i, getStackInSlot(i) == null ? null : getStackInSlot(i).copy());
            return inv;
        }
    }
}
