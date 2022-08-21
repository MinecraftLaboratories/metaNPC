package com.nikita23830.metanpc.client;

import com.nikita23830.metanpc.MetaNPC;
import com.nikita23830.metanpc.common.RoleAdvanceTrader;
import com.nikita23830.metanpc.common.containers.ContainerAdvTraderClient;
import com.nikita23830.metanpc.common.containers.ContainerAdvTraderServer;
import cpw.mods.fml.client.config.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.roles.*;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumJobType;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;
import su.metalabs.lib.api.fonts.CustomFontRenderer;
import su.metalabs.lib.api.fonts.FontTypes;
import su.metalabs.lib.api.fonts.PlaceType;
import su.metalabs.lib.api.gui.MetaAsset;
import su.metalabs.lib.api.gui.utils.RenderUtils;
import su.metalabs.lib.api.gui.utils.ScaleManager;
import su.metalabs.lib.handlers.currency.CurrencyHandler;
import su.metalabs.lib.handlers.data.ClientDataHandler;
import su.metalabs.lib.handlers.data.ClientDataUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuiAdvTrade extends ContainerAdvTraderClient implements IGuiData {
    private final EntityNPCInterface npc;
    private HashMap<Integer, List<String>> cache_tooltip = new HashMap<>();
    private HashMap<Integer, String> cache_status = new HashMap<>();
    private long check_cache = 0L;
    private ResourceLocation location = new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/3.png");
    private ResourceLocation location1 = new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/3.png");
    private ResourceLocation location2 = new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/3.png");
    private long cd = 0L;
    private String textUp = null;
    private RoleAdvanceTrader.CanTradeEnum canHover = null;

    public GuiAdvTrade(EntityNPCInterface npc, EntityPlayer player) {
        super(new ContainerAdvTraderServer(player.inventory, npc), player.inventory);
        this.npc = npc;
        check_cache = System.currentTimeMillis();
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaleManager.update();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if ((System.currentTimeMillis() - 10000L) > check_cache) {
            cache_tooltip = new HashMap<>();
            cache_status = new HashMap<>();
            check_cache = System.currentTimeMillis();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }

    @Override
    public void drawWorldBackground(int p_146270_1_) {
        //
    }

    @Override
    public void drawScreen(int _x, int _y, float ticks) {
        int[] a = super.preDrawScreen(_x, _y);

        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL11.glScalef((1F / sr.getScaleFactor()), (1F / sr.getScaleFactor()), 1);
        int x = _x * sr.getScaleFactor();
        int y = _y * sr.getScaleFactor();
        GL11.glScalef((1F * sr.getScaleFactor()), (1F * sr.getScaleFactor()), 1F);
        ScaleManager.update();
        RenderHelper.drawGradientRect(0, 0, -500, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, new Color(0x5F000000, true).getRGB(), new Color(0x5F000000, true).getRGB());
        RenderHelper.drawGradientRect(0, ScaleManager.get(199), -500, Minecraft.getMinecraft().displayWidth, ScaleManager.get(199 + 630), new Color(0xA8000000, true).getRGB(), new Color(0xA8000000, true).getRGB());
        GL11.glPushMatrix();
        int vis = this.npc.display.showName;
        this.npc.display.showName = 1;
        GL11.glTranslated(0, 0, -401);
        GuiInventory.func_147046_a(ScaleManager.get(417), ScaleManager.get(1197), ScaleManager.get(500), -30, 2, this.npc);
        this.npc.display.showName = vis;
        GL11.glPopMatrix();
        int cx = width / 2;
        int cy = height / 2;
        // finish

        RenderUtils.drawRect(ScaleManager.get(678), ScaleManager.get(292), ScaleManager.get(695), ScaleManager.get(454), new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/dialog-message2.png"));
        RenderUtils.drawRect(ScaleManager.get(760), ScaleManager.get(716), ScaleManager.get(528), ScaleManager.get(300), new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/dialog-message1.png"));

        RenderUtils.drawGradientRect(ScaleManager.get(703F), ScaleManager.get(317F), 0, ScaleManager.get(1336F), ScaleManager.get(709F), new Color(0x262626).getRGB(), new Color(0x262626).getRGB(), 1.0F);

        // title
        GL11.glPushMatrix();
        String title = npc.display.name;
        String sub = npc.display.title;
        float w = CustomFontRenderer.getStringWidth(FontTypes.minecraftRus, title);
        float boost = title.length() < 5 ? 1.5F : title.length() < 10 ? 1.65F : 1.8F;
        GL11.glTranslatef(ScaleManager.get(1010), ScaleManager.get(148), 1);
        GL11.glScalef(ScaleManager.get(4), ScaleManager.get(4), 1F);
        CustomFontRenderer.drawString(FontTypes.minecraftRus, title, 0, 0, 24F, Color.decode("#ffffff").getRGB(), PlaceType.CENTERED);
        w = CustomFontRenderer.getStringWidth(FontTypes.minecraftRus, sub);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(ScaleManager.get(1010), ScaleManager.get(236), 1);
        GL11.glScalef(ScaleManager.get(1.5F), ScaleManager.get(1.5F), 1F);
        CustomFontRenderer.drawString(FontTypes.minecraftRus, sub, 0, 0, 24F, Color.decode("#FF55FF").getRGB(), PlaceType.CENTERED);
        GL11.glPopMatrix();

        // Render invenotyr
        RenderItem ri = new RenderItem();
        List<String> tp = null;
        float tpx = -1;
        float tpy = -1;
        InventoryPlayer player = Minecraft.getMinecraft().thePlayer.inventory;
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = player.getStackInSlot(i);
            float xc = (ScaleManager.get(785) + (ScaleManager.get(54F) * i));
            boolean hover = x >= xc && x <= (xc + ScaleManager.get(48)) && y >= ScaleManager.get(942) && y <= (ScaleManager.get(942) + ScaleManager.get(48));
            GL11.glPushMatrix();
            GL11.glTranslatef(xc, ScaleManager.get(945), 1);
            if (hover)
                RenderUtils.drawGradientRect(0, 0, 1, ScaleManager.get(48), ScaleManager.get(48), new Color(0xc5c5c5).getRGB(), new Color(0xc5c5c5).getRGB(), 1F);
            if (stack != null) {
                if (hover) {
                    tp = (List<String>) stack.getTooltip(player.player, true);
                    tpx = xc + MathHelper.floor_float(x - xc);
                    tpy = ScaleManager.get(944) + (y - ScaleManager.get(943));
                }
                GL11.glScalef(ScaleManager.get(3F), ScaleManager.get(3F), 1);
                RenderHelper.renderItemStackIntoGUI(stack, 0, 0, ri, 1.0f, 2);
            }
            GL11.glPopMatrix();
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                int slot = j + (i * 9);
                ItemStack stack = player.getStackInSlot(9 + slot);
                float xc = (ScaleManager.get(785) + (ScaleManager.get(54F) * j));
                boolean hover = x >= xc && x <= (xc + ScaleManager.get(48)) && y >= ScaleManager.get(770 + (i * 54)) && y <= (ScaleManager.get(770 + (i * 54)) + ScaleManager.get(48));
                GL11.glPushMatrix();
                GL11.glTranslatef(xc, ScaleManager.get(770 + (i * 54)), 1);
                if (hover)
                    RenderUtils.drawGradientRect(0, 0, 1, ScaleManager.get(48), ScaleManager.get(48), new Color(0xc5c5c5).getRGB(), new Color(0xc5c5c5).getRGB(), 1F);
                if (stack != null) {
                    if (hover) {
                        tp = (List<String>) stack.getTooltip(player.player, true);
                        tpx = xc + MathHelper.floor_float(x - xc);
                        tpy = ScaleManager.get(770 + (i * 54)) + (y - ScaleManager.get(770 + (i * 54)));
                    }
                    GL11.glScalef(ScaleManager.get(3F), ScaleManager.get(3F), 1);
                    RenderHelper.renderItemStackIntoGUI(stack, 0, 0, ri, 1.0f, 2);
                }
                GL11.glPopMatrix();
            }
        }

        if (tp != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(tpx, tpy, 0);
            GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1);
            RenderHelper.renderTooltip(0, 0, tp);
            GL11.glPopMatrix();
        }

        if (npc.roleInterface instanceof RoleAdvanceTrader) {
            RoleAdvanceTrader role = (RoleAdvanceTrader) npc.roleInterface;
            String textUp = "trade.no_coins";
            RoleAdvanceTrader.CanTradeEnum can = null;

            for (int i = 0; i < 24; ++i) {
                RoleAdvanceTrader.TradeSlot slot = role.getSlot(i);
                double xc = i < 8 ? ScaleManager.get(708.0D) : i < 16 ? ScaleManager.get(916.0D) : ScaleManager.get(1124.0D);
                int yc$ = i % 8;
                double yc = ScaleManager.get(322.0D) + ScaleManager.get(yc$ * 48D);
                if (cache_status.containsKey(i)) {
                    textUp = cache_status.get(i);
                } else {
                    textUp = role.getMessageFromSlot(i, mc.thePlayer);
                    cache_status.put(i, textUp);
                }
                boolean hover = (x >= xc && x <= (xc + ScaleManager.get(208D)) && y >= yc && y <= (yc + ScaleManager.get(48D)));
                RoleAdvanceTrader.CanTradeEnum canLocal = RoleAdvanceTrader.CanTradeEnum.getEnumByMessage(textUp);
                if (hover) {
                    canHover = canLocal;
                }
                if ((slot.getSlotIn(0) == null && slot.getSlotIn(1) == null) || (slot.getSlotOut(0) == null && slot.getSlotOut(1) == null)) {
                    RenderHelper.drawGradientRect(xc, yc, 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(48D)), new Color(0x4D4636).getRGB(), new Color(0x4D4636).getRGB());
                    RenderHelper.drawGradientRect(xc, yc, 1, (xc + ScaleManager.get(205D)), (yc + ScaleManager.get(3D)), new Color(0x6C624C).getRGB(), new Color(0x6C624C).getRGB());
                    RenderHelper.drawGradientRect(xc, (yc + ScaleManager.get(3D)), 1, (xc + ScaleManager.get(3D)), (yc + ScaleManager.get(45D)), new Color(0x6C624C).getRGB(), new Color(0x6C624C).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(3D)), (yc + ScaleManager.get(45D)), 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(48D)), new Color(0x28251C).getRGB(), new Color(0x28251C).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(205D)), (yc + ScaleManager.get(3D)), 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(45D)), new Color(0x28251C).getRGB(), new Color(0x28251C).getRGB());
                } else {
                    // has
                    Color[] colorSel = hover ? new Color[]{new Color(0xB688AE), new Color(0xFAB9EF), new Color(0x654C61)} : new Color[]{new Color(0x9E8F71), new Color(0xDDC89D), new Color(0x534B3A)};
                    RenderHelper.drawGradientRect(xc, yc, 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(48D)), colorSel[0].getRGB(), colorSel[0].getRGB());
                    RenderHelper.drawGradientRect(xc, yc, 1, (xc + ScaleManager.get(205D)), (yc + ScaleManager.get(3D)), colorSel[1].getRGB(), colorSel[1].getRGB());
                    RenderHelper.drawGradientRect(xc, (yc + ScaleManager.get(3D)), 1, (xc + ScaleManager.get(3D)), (yc + ScaleManager.get(45D)), colorSel[1].getRGB(), colorSel[1].getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(3D)), (yc + ScaleManager.get(45D)), 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(48D)), colorSel[2].getRGB(), colorSel[2].getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(205D)), (yc + ScaleManager.get(3D)), 1, (xc + ScaleManager.get(208D)), (yc + ScaleManager.get(45D)), colorSel[2].getRGB(), colorSel[2].getRGB());


                    if (slot.getSlotIn(0) != null && slot.getSlotIn(1) != null) {
                        RoleAdvanceTrader.TradeSlotIn in0 = slot.getSlotIn(0);
                        RoleAdvanceTrader.TradeSlotIn in1 = slot.getSlotIn(1);
                        GL11.glPushMatrix();
                        GL11.glTranslated(xc + ScaleManager.get(8D), yc + ScaleManager.get(8), 0);
                        switch (in0.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                if (in1.getType() == RoleAdvanceTrader.TradeType.COINS)
                                    GL11.glTranslatef(0, ScaleManager.get(-1F), 0);
                                if (in1.getType() != RoleAdvanceTrader.TradeType.STACK)
                                    GL11.glTranslatef(ScaleManager.get(1F), 0, 0);
                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in0.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            default: {
                                ResourceLocation rl = in0.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in0.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                GL11.glPushMatrix();
                                if (in0.getType() == RoleAdvanceTrader.TradeType.RUB)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                if (in0.getType() == RoleAdvanceTrader.TradeType.GEMS)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                GL11.glTranslatef(ScaleManager.get(2F), 0, 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                GL11.glPopMatrix();
                                break;
                            }
                        }

                        GL11.glTranslated(ScaleManager.get(38D), 0F, 0F);
                        switch (in1.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                if (in0.getType() == RoleAdvanceTrader.TradeType.COINS)
                                    GL11.glTranslatef(0, ScaleManager.get(-1F), 0);

                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in1.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            case COINS:
                            case RUB:
                            default: {
                                ResourceLocation rl = in1.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in1.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                if (in1.getType() == RoleAdvanceTrader.TradeType.RUB)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                if (in1.getType() == RoleAdvanceTrader.TradeType.GEMS)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                GL11.glTranslatef(ScaleManager.get(3F), 0, 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                break;
                            }
                        }
                        GL11.glPopMatrix();
                    } else {
                        RoleAdvanceTrader.TradeSlotIn in;
                        if (slot.getSlotIn(0) == null)
                            in = slot.getSlotIn(1);
                        else
                            in = slot.getSlotIn(0);
                        switch (in.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                GL11.glTranslated(xc + ScaleManager.get(28D), yc + (ScaleManager.get(7D)), 0);
                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            default: {
                                ResourceLocation rl = in.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                GL11.glPushMatrix();
                                GL11.glTranslated(xc + ScaleManager.get(30D), yc + (ScaleManager.get(9D)), 3);
                                if (in.getType() == RoleAdvanceTrader.TradeType.COINS)
                                    GL11.glTranslatef(0, ScaleManager.get(-1F), 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                GL11.glPopMatrix();
                                break;
                            }
                        }
                    }

                    // C5C5C5, 3.5, 30.7
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(87)), (yc + ScaleManager.get(19)), 1, (xc + ScaleManager.get(117)), (yc + ScaleManager.get(28)), new Color(0xC5C5C5).getRGB(), new Color(0xC5C5C5).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(117)), (yc + ScaleManager.get(22)), 1, (xc + ScaleManager.get(120)), (yc + ScaleManager.get(25)), new Color(0xC5C5C5).getRGB(), new Color(0xC5C5C5).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(106)), (yc + ScaleManager.get(10)), 1, (xc + ScaleManager.get(109)), (yc + ScaleManager.get(37)), new Color(0xC5C5C5).getRGB(), new Color(0xC5C5C5).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(109)), (yc + ScaleManager.get(13)), 1, (xc + ScaleManager.get(112)), (yc + ScaleManager.get(34)), new Color(0xC5C5C5).getRGB(), new Color(0xC5C5C5).getRGB());
                    RenderHelper.drawGradientRect((xc + ScaleManager.get(112)), (yc + ScaleManager.get(16)), 1, (xc + ScaleManager.get(115)), (yc + ScaleManager.get(31)), new Color(0xC5C5C5).getRGB(), new Color(0xC5C5C5).getRGB());
                    if (canLocal != RoleAdvanceTrader.CanTradeEnum.YES) {// TODO cannot AF1E00
                        GL11.glPushMatrix();
                        GL11.glTranslatef(0, 0, 3);
                        RenderUtils.drawRect((float) (xc + ScaleManager.get(87)), (float) (yc + ScaleManager.get(10)), ScaleManager.get(33), ScaleManager.get(27), new ResourceLocation(MetaNPC.MODID.toLowerCase() + ":textures/dialog-message3.png"));
                        GL11.glPopMatrix();
                    }
                    if (slot.getSlotOut(0) != null && slot.getSlotOut(1) != null) {
                        RoleAdvanceTrader.TradeSlotOut in0 = slot.getSlotOut(0);
                        RoleAdvanceTrader.TradeSlotOut in1 = slot.getSlotOut(1);
                        GL11.glPushMatrix();
                        GL11.glTranslated(xc + ScaleManager.get(132), (yc + ScaleManager.get(6)), 0);
                        switch (in0.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                if (in1.getType() == RoleAdvanceTrader.TradeType.STACK)
                                    GL11.glTranslatef(ScaleManager.get(-7F), ScaleManager.get(2F), 0);
                                else
                                    GL11.glTranslatef(ScaleManager.get(-6F), ScaleManager.get(1F), 0);
                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in0.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            default: {
                                ResourceLocation rl = in0.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in0.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                GL11.glPushMatrix();
                                if (in0.getType() == RoleAdvanceTrader.TradeType.RUB)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                if (in1.getType() == RoleAdvanceTrader.TradeType.STACK)
                                    GL11.glTranslatef(ScaleManager.get(-5F), ScaleManager.get(3F), 0);
                                else
                                    GL11.glTranslatef(ScaleManager.get(-3F), ScaleManager.get(3F), 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                GL11.glPopMatrix();
                                break;
                            }
                        }
                        GL11.glTranslated(ScaleManager.get(34D), 0F, 0F);

                        switch (in1.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                if (in0.getType() == RoleAdvanceTrader.TradeType.STACK)
                                    GL11.glTranslatef(ScaleManager.get(-1F), ScaleManager.get(2F), 0);
                                else
                                    GL11.glTranslatef(ScaleManager.get(-2F), ScaleManager.get(1F), 0);
                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in1.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            default: {
                                ResourceLocation rl = in1.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in1.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                GL11.glPushMatrix();
                                if (in1.getType() == RoleAdvanceTrader.TradeType.RUB)
                                    GL11.glTranslatef(0, ScaleManager.get(1F), 0);
                                if (in0.getType() == RoleAdvanceTrader.TradeType.STACK)
                                    GL11.glTranslatef(ScaleManager.get(1F), ScaleManager.get(3F), 0);
                                else
                                    GL11.glTranslatef(0, ScaleManager.get(3F), 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                GL11.glPopMatrix();
                                break;
                            }
                        }
                        GL11.glPopMatrix();
                    } else {
                        RoleAdvanceTrader.TradeSlotOut in;
                        if (slot.getSlotOut(0) == null)
                            in = slot.getSlotOut(1);
                        else
                            in = slot.getSlotOut(0);
                        GL11.glPushMatrix();
                        GL11.glTranslated(xc + ScaleManager.get(144), yc + ScaleManager.get(8), 0);
                        switch (in.getType()) {
                            case STACK: {
                                GL11.glPushMatrix();
                                GL11.glTranslatef(ScaleManager.get(1F), ScaleManager.get(-1F), 0);
                                GL11.glScalef(ScaleManager.get(2F), ScaleManager.get(2F), 1F);
                                RenderHelper.renderItemStackIntoGUI((ItemStack) in.data, 0, 0, ri, 1F, 1);
                                GL11.glPopMatrix();
                                break;
                            }
                            default: {
                                ResourceLocation rl = in.getType() == RoleAdvanceTrader.TradeType.COINS ? CurrencyHandler.getGoldCurrency().getIcon() :
                                        in.getType() == RoleAdvanceTrader.TradeType.RUB ? CurrencyHandler.getRubCurrency().getIcon() : CurrencyHandler.getGemCurrency().getIcon();
                                GL11.glPushMatrix();
                                if (in.getType() == RoleAdvanceTrader.TradeType.RUB)
                                    GL11.glTranslatef(ScaleManager.get(3F), ScaleManager.get(1F), 0);
                                if (in.getType() == RoleAdvanceTrader.TradeType.COINS)
                                    GL11.glTranslatef(ScaleManager.get(3F), 0, 0);
                                if (in.getType() == RoleAdvanceTrader.TradeType.GEMS)
                                    GL11.glTranslatef(ScaleManager.get(3F), ScaleManager.get(1F), 0);
                                GL11.glScaled(ScaleManager.get(0.12), ScaleManager.get(0.12), 1F);
                                mc.getTextureManager().bindTexture(rl);
                                RenderHelper.drawTexturedModalRect(0, 0, 3, 0, 0, 256, 256);
                                GL11.glPopMatrix();
                                break;
                            }
                        }
                        GL11.glPopMatrix();
                    }
                }

            }

            for (int i = 0; i < 24; ++i) {
                RoleAdvanceTrader.TradeSlot slot = role.getSlot(i);
                double xc = i < 8 ? ScaleManager.get(708.0D) : i < 16 ? ScaleManager.get(916.0D) : ScaleManager.get(1124.0D);
                int yc$ = i % 8;
                double yc = ScaleManager.get(322.0D) + ScaleManager.get(yc$ * 48D);
                boolean hover = (x >= xc && x <= (xc + ScaleManager.get(208D)) && y >= yc && y <= (yc + ScaleManager.get(48D)));
                if ((slot.getSlotIn(0) == null && slot.getSlotIn(1) == null) || (slot.getSlotOut(0) == null && slot.getSlotOut(1) == null)) {
                    if (hover) {
                        textUp = null;
                    }
                    continue;
                }
                if (hover) {
                    if (canHover != null) {
                        textUp = canHover.message;
                    } else {
                        textUp = null;
                    }

                    List<String> ttp = new ArrayList<>();
                    if (cache_tooltip.containsKey(i)) {
                        ttp = cache_tooltip.get(i);
                    } else {
                        ttp = slot.genTooltip(canHover);
                        cache_tooltip.put(i, ttp);
                    }
                    GL11.glPushMatrix();
                    GL11.glTranslatef(x, y, 0);
                    GL11.glScalef(2F, 2F, 1);
                    RenderHelper.renderTooltip(0, 0, ttp);
                    GL11.glPopMatrix();
                }
            }

            float scr = (mc.displayWidth / 1920F);
            float scy = (mc.displayHeight / 1080F);
            if (this.textUp != null && !this.textUp.isEmpty()) { //
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                int show = MathHelper.floor_float(Math.min(260F, (float) ((System.currentTimeMillis() - cd) / 5L) * 3.9F));
                GL11.glScissor(ScaleManager.get(120), ScaleManager.get(1080 - 348), ScaleManager.get(600), ScaleManager.get(show));
                //RenderHelper.drawGradientRect(0, 0, 50, ScaleManager.get(1920), ScaleManager.get(1080), new Color(0x000000).getRGB(), new Color(0x000000).getRGB());
                mc.getTextureManager().bindTexture(location);
                RenderUtils.drawRect(ScaleManager.get(123F), ScaleManager.get(88F), ScaleManager.get(600), ScaleManager.get(260), location);
                String[] lines = StatCollector.translateToLocal(this.textUp).split(";");
                int index = 0;
                for (String s : lines) {
                    String str = s.replaceAll("%A%", CurrencyHandler.getGoldCurrency().getCurrencyName());
                    str = str.replaceAll("%B%", CurrencyHandler.getGemCurrency().getCurrencyName());
                    str = str.replaceAll("%C%", CurrencyHandler.getRubCurrency().getCurrencyName());
                    GL11.glPushMatrix();
                    GL11.glTranslated((392 * scr), ((142 + (index * 36)) * scy), 306D);
                    GL11.glScalef(scr, scy, 1F);
                    CustomFontRenderer.drawString(FontTypes.minecraftRus, str, 0, 0, 24F, Color.decode("#000000").getRGB(), PlaceType.CENTERED);
                    GL11.glPopMatrix();
                    ++index;
                }
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                GL11.glPopMatrix();
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((1325 * scr), 0F, 0F);
            RenderHelper.drawGradientRect(0, (25 * scy), 0, (530 * scr), (75 * scy), new Color(0xA8000000, true).getRGB(), new Color(0xA8000000, true).getRGB());
            RenderHelper.drawGradientRect(2, (23 * scy), 0, (528 * scr), (25 * scy), new Color(0xA8000000, true).getRGB(), new Color(0xA8000000, true).getRGB());
            RenderHelper.drawGradientRect(2, (75 * scy), 0, (528 * scr), (77 * scy), new Color(0xA8000000, true).getRGB(), new Color(0xA8000000, true).getRGB());

            boolean uni = mc.fontRenderer.getUnicodeFlag();
            mc.fontRenderer.setUnicodeFlag(false);
            GL11.glPushMatrix();
            GL11.glTranslatef((417 * scr), (37 * scy), 0);
            GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
            mc.fontRenderer.drawString(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getRubCurrency().getCurrencyId())), 0, 0, new Color(0xFF5555).getRGB(), false);
            GL11.glPopMatrix();
            if (Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getRubCurrency().getCurrencyId())).length() < 5) {
                int need = 5 - Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getRubCurrency().getCurrencyId())).length();
                int ws = mc.fontRenderer.getStringWidth(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getRubCurrency().getCurrencyId())));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < need; ++i)
                    sb.append("0");
                GL11.glPushMatrix();
                GL11.glTranslatef(((417 + (ws * 3.5F)) * scr), (37 * scy), 0);
                GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
                mc.fontRenderer.drawString(sb.toString(), 0, 0, new Color(0x333333).getRGB(), false);
                GL11.glPopMatrix();
            }
            mc.getTextureManager().bindTexture(CurrencyHandler.getRubCurrency().getIcon());
            GL11.glPushMatrix();
            GL11.glTranslated((381 * scr), (34 * scy), 0);
            GL11.glScaled((0.12 * scr), (0.12 * scy), 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            RenderHelper.drawTexturedModalRect(0, 0, 0, 0, 0, 256, 256);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef((262 * scr), (37 * scy), 0);
            GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
            mc.fontRenderer.drawString(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGemCurrency().getCurrencyId())), 0, 0, new Color(0x55FF55).getRGB(), false);
            GL11.glPopMatrix();
            if (Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGemCurrency().getCurrencyId())).length() < 5) {
                int need = 5 - Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGemCurrency().getCurrencyId())).length();
                int ws = mc.fontRenderer.getStringWidth(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGemCurrency().getCurrencyId())));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < need; ++i)
                    sb.append("0");
                GL11.glPushMatrix();
                GL11.glTranslatef(((262 + (ws * 3.5F)) * scr), (37 * scy), 0);
                GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
                mc.fontRenderer.drawString(sb.toString(), 0, 0, new Color(0x333333).getRGB(), false);
                GL11.glPopMatrix();
            }
            mc.getTextureManager().bindTexture(CurrencyHandler.getGemCurrency().getIcon());
            GL11.glPushMatrix();
            GL11.glTranslated((230 * scr), (34 * scy), 0);
            GL11.glScaled((0.12 * scr), (0.12 * scy), 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            RenderHelper.drawTexturedModalRect(0, 0, 0, 0, 0, 256, 256);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef((50 * scr), (37 * scy), 0);
            GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
            mc.fontRenderer.drawString(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGoldCurrency().getCurrencyId())), 0, 0, new Color(0xFFAA00).getRGB(), false);
            GL11.glPopMatrix();
            if (Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGoldCurrency().getCurrencyId())).length() < 5) {
                int need = 8 - Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGoldCurrency().getCurrencyId())).length();
                int ws = mc.fontRenderer.getStringWidth(Integer.toString(ClientDataHandler.getIntDataValue(CurrencyHandler.getGoldCurrency().getCurrencyId())));
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < need; ++i)
                    sb.append("0");
                GL11.glPushMatrix();
                GL11.glTranslatef(((50 + (ws * 3.5F)) * scr), (37 * scy), 0);
                GL11.glScalef((3.5F * scr), (3.5F * scy), 1);
                mc.fontRenderer.drawString(sb.toString(), 0, 0, new Color(0x333333).getRGB(), false);
                GL11.glPopMatrix();
            }
            mc.getTextureManager().bindTexture(CurrencyHandler.getGoldCurrency().getIcon());
            GL11.glPushMatrix();
            GL11.glTranslated((15 * scr), (34 * scy), 0);
            GL11.glScaled((0.12 * scr), (0.12 * scy), 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            RenderHelper.drawTexturedModalRect(0, 0, 0, 0, 0, 256, 256);
            GL11.glPopMatrix();

            mc.fontRenderer.setUnicodeFlag(uni);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glPopMatrix();

        }
        GL11.glPopMatrix();
    }

    @Override
    protected void mouseClicked(int x, int y, int p_73864_3_) {
        ScaledResolution rs = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
//        int x = _x * rs.getScaleFactor();
//        int y = _y * rs.getScaleFactor();
        RoleAdvanceTrader role = (RoleAdvanceTrader) this.npc.roleInterface;
        boolean click = false;
        for (int i = 0; i < 24; ++i) {
            RoleAdvanceTrader.TradeSlot slot = role.getSlot(i);
            double xc = i < 8 ? ScaleManager.get(708.0D) : i < 16 ? ScaleManager.get(916.0D) : ScaleManager.get(1124.0D);
            int yc$ = i % 8;
            double yc = ScaleManager.get(322.0D) + ScaleManager.get(yc$ * 48D);
            boolean hover = (x >= xc && x <= (xc + ScaleManager.get(208D)) && y >= yc && y <= (yc + ScaleManager.get(48D)));

            if (hover && canHover != null) {
                textUp = canHover.message;
                click = true;
                cd = System.currentTimeMillis();
                if (canHover == RoleAdvanceTrader.CanTradeEnum.YES) {
                    mc.thePlayer.playSound(MetaNPC.MODID + ":a0", 0.1F, 0.6F);
                    check_cache = 0L;
                    role.onTryBuy(i, mc.thePlayer);
                } else {
                    mc.thePlayer.playSound(MetaNPC.MODID + ":a1", 4.0F, 0.6F);
                }
                break;
            }
        }
        if (!click)
            textUp = null;
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
        this.npc.roleInterface.readFromNBT(compound);
    }
}
