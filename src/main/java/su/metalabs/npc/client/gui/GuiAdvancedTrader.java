package su.metalabs.npc.client.gui;

import su.metalabs.npc.Reference;
import su.metalabs.npc.client.RenderHelper;
import su.metalabs.npc.common.IContentSlot;
import com.nikita23830.metanpc.common.RoleAdvanceTrader;
import su.metalabs.npc.common.containers.ContainerAdvancedTrader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.opengl.GL11;
import su.metalabs.lib.api.fonts.CustomFontRenderer;
import su.metalabs.lib.api.fonts.FontTypes;
import su.metalabs.lib.api.fonts.PlaceType;
import su.metalabs.lib.api.gui.GuiContainerMeta;
import su.metalabs.lib.api.gui.MetaAsset;
import su.metalabs.lib.api.gui.utils.ButtonRenderUtils;
import su.metalabs.lib.api.gui.utils.RenderUtils;
import su.metalabs.lib.api.gui.utils.ScaleManager;
import su.metalabs.lib.handlers.branding.BrandingHandler;
import su.metalabs.lib.handlers.currency.CurrencyHandler;
import su.metalabs.lib.handlers.data.FormatUtils;
import su.metalabs.npc.common.packets.RequestFastSellPermissions;
import su.metalabs.npc.proxy.ClientProxy;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import static su.metalabs.lib.api.gui.utils.ScaleManager.get;

public class GuiAdvancedTrader extends GuiContainerMeta implements IGuiData {
    private EntityNPCInterface npc;
    private RoleAdvanceTrader role;

    private long cd = 0L;

    private long check_cache;
    private HashMap<Integer, List<String>> cache_tooltip = new HashMap<>();
    private HashMap<Integer, String> cache_status = new HashMap<>();

    private RoleAdvanceTrader.CanTradeEnum canTrade = null;

    private int currentSlotHover = -1;

    private String textUp = null;

    private final InventoryPlayer inventoryPlayer;

    private  static final ResourceLocation BUBBLE_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/advanced_trader/text_bubble.png");

    public GuiAdvancedTrader(InventoryPlayer inventoryPlayer, EntityNPCInterface npc) {
        super(new ContainerAdvancedTrader(inventoryPlayer, npc));
        this.inventoryPlayer = inventoryPlayer;
        EntityCustomNpc n = new EntityCustomNpc(npc.worldObj);
        NBTTagCompound nb = new NBTTagCompound();
        npc.writeToNBT(nb);
        n.readFromNBT(nb);
        this.npc = n;
        this.npc.display.showName = 2;
        this.role = (RoleAdvanceTrader) this.npc.roleInterface;
        this.check_cache = System.currentTimeMillis();
    }


    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if ((System.currentTimeMillis() - 10000L) > check_cache) {
            cache_tooltip = new HashMap<>();
            cache_status = new HashMap<>();
            check_cache = System.currentTimeMillis();
        }

        currentSlotHover = -1;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float ticks, int mouseX, int mouseY) {
        if(getCurrentModalPane() != null) {
            this.layer = getCurrentModalPane().getLayer();
        } else {
            this.layer = 0;
        }

        //Draw background
        RenderUtils.drawColoredRect(0,0, width, height, Color.BLACK, 0.7F);
        RenderUtils.drawColoredRect(0, ScaleManager.get(199), width, get(829), Color.BLACK, 0.7F);

        GL11.glTranslatef(0,0,500);
        RenderUtils.drawRect(width / 2.0F, get(292), get(684), get(444), MetaAsset.of(Reference.MOD_ID, "textures/gui/advanced_trader/background.png"), PlaceType.CENTERED);
        RenderUtils.drawInventory(width / 2.0F, get(716), PlaceType.CENTERED);




        //Draw content
        drawTitle();
        drawFastSell();
        drawTextBubble();

        super.drawGuiContainerBackgroundLayer(ticks, mouseX, mouseY);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks) {
        super.drawScreen(mouseX, mouseY, ticks);
        //Draw NPC
        drawTrader(npc);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);


        //Draw modal and balance
        GL11.glPushMatrix();
        GL11.glTranslatef(0,0,500);
        drawTrades();

        if(this.getCurrentModalPane() != null) {
            this.getCurrentModalPane().draw(mouseX, mouseY, true);
        }

        RenderUtils.drawBalance(width - get(25) - get(510), get(25), 0.7F);
        GL11.glPopMatrix();
    }

    public void drawTitle() {
        CustomFontRenderer.drawString(FontTypes.minecraftSeven, npc.display.name.replace("&", "\u00a7"), width / 2.0F, get(152), get(80), Color.WHITE.getRGB(), PlaceType.CENTERED);
        CustomFontRenderer.drawString(FontTypes.minecraftSeven, npc.display.title.replace("&", "\u00a7"), width / 2.0F, get(236), get(36), Color.WHITE.getRGB(), PlaceType.CENTERED);
    }

    public void drawFastSell() {
        float centerX = width / 2.0F + get(360) + get(360) / 2.0F;
        float startY = get(292);
        if(ClientProxy.isFastSellAllowed) {
            RenderUtils.drawRect(width / 2.0F + get(360), startY, get(360), get(265), MetaAsset.of(Reference.MOD_ID, "textures/gui/advanced_trader/unlocked_fast_sell_background.png"));
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "Быстрая продажа", centerX, startY + get(52), get(24), 0xFFAA00, PlaceType.CENTERED);
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "§eУстал кликать? §aПродай все", centerX, startY + get(102), get(16), 0xFFFFFF, PlaceType.CENTERED);
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "§aпредметы за один раз с", centerX, startY + get(126), get(16), 0xFFFFFF, PlaceType.CENTERED);
            if(ButtonRenderUtils.drawGradientButton(this, centerX - get(219) / 2.0F, startY + get(161), get(219), get(59), 0, Color.decode("#C5EC41"), Color.decode("#00AA00")) && isClicked()) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/sell all");
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
            CustomFontRenderer.drawString(FontTypes.minecraftFive, "ПРОДАТЬ ВСЁ", centerX, startY + get(176), get(20), 0xFFFFFF, PlaceType.CENTERED);
        } else {
            RenderUtils.drawRect(width / 2.0F + get(360), startY, get(360), get(289), MetaAsset.of(Reference.MOD_ID, "textures/gui/advanced_trader/locked_fast_sell_background.png"));
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "Быстрая продажа", centerX, startY + get(52), get(24), 0xFFAA00, PlaceType.CENTERED);
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "§eУстал кликать? §aПродай все", centerX, startY + get(102), get(16), 0xFFFFFF, PlaceType.CENTERED);
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "§aпредметы за один раз с", centerX, startY + get(126), get(16), 0xFFFFFF, PlaceType.CENTERED);
            CustomFontRenderer.drawString(FontTypes.minecraftRus, "§aпомощью привилегии §e" + StatCollector.translateToLocal("group500.name"), centerX, startY + get(150), get(16), 0xFFFFFF, PlaceType.CENTERED);
            if(ButtonRenderUtils.drawGradientButton(this, centerX - get(302) / 2.0F, startY + get(185), get(302), get(59), 0, Color.decode("#C5EC41"), Color.decode("#00AA00")) && isClicked()) {
                BrandingHandler.getProject().buyGroup();
            }
            CustomFontRenderer.drawString(FontTypes.minecraftFive, "ПЕРЕЙТИ К ПОКУПКЕ", centerX, startY + get(200), get(20), 0xFFFFFF, PlaceType.CENTERED);
        }
    }

    public void drawTextBubble() {
        float scr = (mc.displayWidth / 1920F);
        float scy = (mc.displayHeight / 1080F);
        GL11.glPushMatrix();
        GL11.glTranslatef(0,0,500);
        if (this.textUp != null && !this.textUp.isEmpty()) { //
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            int show = MathHelper.floor_float(Math.min(260F, (float) ((System.currentTimeMillis() - cd) / (double)5L) * 3.9F));


            if(System.currentTimeMillis() > cd + 3000L) {
                show = 0;
            }

            GL11.glScissor((int) ScaleManager.get(120), (int) ScaleManager.get(1080 - 348), (int) ScaleManager.get(600), (int) ScaleManager.get(show));
            mc.getTextureManager().bindTexture(BUBBLE_BACKGROUND);
            RenderUtils.drawRect(ScaleManager.get(123F), ScaleManager.get(88F), ScaleManager.get(600), ScaleManager.get(260), BUBBLE_BACKGROUND);
            String[] lines = StatCollector.translateToLocal(this.textUp).split(";");
            int index = 0;
            for (String s : lines) {
                String str = s.replace("%A%", CurrencyHandler.getGoldCurrency().getCurrencyName());
                str = str.replace("%B%", CurrencyHandler.getGemCurrency().getCurrencyName());
                str = str.replace("%C%", CurrencyHandler.getRubCurrency().getCurrencyName());
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
        GL11.glPopMatrix();
    }

    public void drawTrades() {
        RenderUtils.drawColoredRectWidthHeight(width / 2.0F, get(317), get(634), get(394), Color.decode("#262626"), 1.0F, PlaceType.CENTERED);

        if (!(npc.roleInterface instanceof RoleAdvanceTrader)) {
            return;
        }

        int count = 0;
        for (int i = 0; i < 3; i++) {
            for(int j = 0; j < 8; j++) {
                drawTrade(width / 2.0F - (get(208) * (3)) / 2.0F + get(208) * i, get(322) + j * get(48), role.getSlot(count), count);
                count++;
            }
        }

        if(currentSlotHover != -1) {
            renderTooltip(role.getSlot(currentSlotHover), currentSlotHover);
        }
    }

    public void drawTrade(float x, float y, RoleAdvanceTrader.TradeSlot slot, int slotId) {
        boolean isSlotValid = !((slot.getSlotIn(0) == null && slot.getSlotIn(1) == null) || (slot.getSlotOut(0) == null && slot.getSlotOut(1) == null));
        boolean isHover = isHover(x, y, get(208), get(48), 0);

        drawTradeBackground(x, y, isSlotValid, isHover);

        String textUp = "trade.no_coins";
        if (cache_status.containsKey(slotId)) {
            textUp = cache_status.get(slotId);
        } else {
            textUp = role.getMessageFromSlot(slotId, mc.thePlayer);
            cache_status.put(slotId, textUp);
        }

        RoleAdvanceTrader.CanTradeEnum canTradeLocal = RoleAdvanceTrader.CanTradeEnum.getEnumByMessage(textUp);
        if(isSlotValid) {
            drawSlotContents(x, y, slot);
            RenderUtils.drawRect(x + get(87), y + get(10), get(33), get(27), MetaAsset.of(Reference.MOD_ID, "textures/gui/advanced_trader/" + ((canTradeLocal == RoleAdvanceTrader.CanTradeEnum.YES) ? "arrow" : "arrow_with_cross") + ".png"), PlaceType.DEFAULT);

            if(isHover) {
                canTrade = canTradeLocal;
                currentSlotHover = slotId;

                if(isClicked(true)) {
                    handleTradeRequest(slotId);
                }
            }

        }


    }

    public void renderTooltip(RoleAdvanceTrader.TradeSlot slot, int slotId) {
        if(!cache_tooltip.containsKey(slotId)) {
            cache_tooltip.put(slotId, slot.genTooltip(canTrade));
        }

        GL11.glPushMatrix();
        GL11.glTranslatef((int)(mouseX), (int)(mouseY), 0);
        GL11.glScalef(get(3F), get(3F), 1);
        RenderHelper.renderTooltip(0, 0, cache_tooltip.get(slotId));
        GL11.glPopMatrix();
    }

    public void handleTradeRequest(int slotId) {
        if(canTrade != null) {
            textUp = canTrade.message;
            cd = System.currentTimeMillis();
            if (canTrade == RoleAdvanceTrader.CanTradeEnum.YES) {
                mc.thePlayer.playSound(Reference.RESOURCE_PREFIX + "a0", 0.3F, 1.0F);
                check_cache = 0L;
                role.onTryBuy(slotId, mc.thePlayer);
            } else {
                mc.thePlayer.playSound(Reference.RESOURCE_PREFIX + "a1", 3.0F, 1.0F);
            }
        }
    }
    public void drawSlotContents(float x, float y, RoleAdvanceTrader.TradeSlot slot) {
        //In
        if(slot.getSlotIn(0) != null && slot.getSlotIn(1) != null) {
            for(int i = 0; i < 2; i++) {
                drawSlotInt(x + get(9) + get(36) * i, y + get(6), slot.getSlotIn(i));
            }
        } else if(slot.getSlotIn(0) != null || slot.getSlotIn(1) != null) {
            RoleAdvanceTrader.TradeSlotIn slotIn =  slot.getSlotIn(slot.getSlotIn(0) != null ? 0 : 1);
            drawSlotInt(x + get(29), y + get(6), slotIn);
        }

        //Out
        if(slot.getSlotOut(0) != null && slot.getSlotOut(1) != null) {
            for(int i = 0; i < 2; i++) {
                drawSlotInt(x + get(127) + get(36) * i, y + get(6), slot.getSlotOut(i));
            }
        } else if(slot.getSlotOut(0) != null || slot.getSlotOut(1) != null) {
            RoleAdvanceTrader.TradeSlotOut slotOut =  slot.getSlotOut(slot.getSlotOut(0) != null ? 0 : 1);
            drawSlotInt(x + get(142), y + get(6), slotOut);
        }
    }


    public void drawSlotInt(float x, float y, IContentSlot slot) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        switch (slot.getType()){
            case STACK: {
                RenderHelper.renderItemStackIntoGUI((ItemStack) slot.getData(), 0, 0, RenderUtils.itemRender, ScaleManager.get(36.0F) / 16.0F, 1);
                break;
            }
            case COINS: {
                GL11.glTranslatef(get(2), get(2), 0);
                RenderUtils.drawRect(0,0,get(32), get(32), CurrencyHandler.getGoldCurrency().getIcon());
                drawAmount((Integer) slot.getData());
                break;
            }
            case GEMS: {
                GL11.glTranslatef(get(2), get(2), 0);
                RenderUtils.drawRect(0,0,get(32), get(32), CurrencyHandler.getGemCurrency().getIcon());
                drawAmount((Integer) slot.getData());
                break;
            }
            case RUB: {
                GL11.glTranslatef(get(2), get(2), 0);
                RenderUtils.drawRect(0,0,get(32), get(32), CurrencyHandler.getRubCurrency().getIcon());
                drawAmount((Integer) slot.getData());
                break;
            }
        }

        GL11.glPopMatrix();
    }

    public void drawAmount(int amount) {
        String value = FormatUtils.intComressor((long) amount);
        if (amount > 1) {
            GL11.glPushMatrix();
            GL11.glScalef(ScaleManager.get(36.0F) / 16.0F, ScaleManager.get(36.0F) / 16.0F, ScaleManager.get(36.0F) / 16.0F);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(value, 19 - 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(value), 8, 16777215);
            GL11.glPopMatrix();
        }
    }

    public void drawTradeBackground(float x, float y, boolean hasContent, boolean isHover) {
        float shadowSize = get(4);
        float width = get(208);
        float height = get(48);



        Color baseColor;
        Color topShadowColor;
        Color bottomShadowColor;

        if(!hasContent) {
            //Doesn't have content
            baseColor = Color.decode("#4D4636");
            topShadowColor = Color.decode("#6C624C");
            bottomShadowColor = Color.decode("#28251C");
        } else if(isHover) {
            //Has content and hovered
            baseColor = Color.decode("#B688AE");
            topShadowColor = Color.decode("#FAB9EF");
            bottomShadowColor = Color.decode("#654C61");
        } else {
            //Has content and isn't hovered
            baseColor = Color.decode("#9E8F71");
            topShadowColor = Color.decode("#DDC89D");
            bottomShadowColor = Color.decode("#534B3A");
        }

        RenderUtils.drawColoredRectWidthHeight(x, y, width, height, baseColor, 1.0F);

        RenderUtils.drawColoredRectWidthHeight(x, y, width, shadowSize, topShadowColor, 1.0F);
        RenderUtils.drawColoredRectWidthHeight(x, y, shadowSize, height, topShadowColor, 1.0F);

        RenderUtils.drawColoredRectWidthHeight(x, y + height - shadowSize, width, shadowSize, bottomShadowColor, 1.0F);
        RenderUtils.drawColoredRectWidthHeight(x + width - shadowSize, y, shadowSize, height, bottomShadowColor, 1.0F);

        RenderUtils.drawColoredRectWidthHeight(x, y + height - shadowSize, shadowSize, shadowSize, baseColor, 1.0F);
        RenderUtils.drawColoredRectWidthHeight(x + width - shadowSize, y, shadowSize, shadowSize, baseColor, 1.0F);


    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
        this.npc.roleInterface.readFromNBT(compound);
    }

    public static void drawTrader(EntityNPCInterface entity) {
        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
        GL11.glScalef((1F / sr.getScaleFactor()), (1F / sr.getScaleFactor()), 1);
        GL11.glScalef((1F * sr.getScaleFactor()), (1F * sr.getScaleFactor()), 1F);
        ScaleManager.update();
        RenderHelper.drawGradientRect(0, 0, -500, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, new Color(0x5F000000, true).getRGB(), new Color(0x5F000000, true).getRGB());
        RenderHelper.drawGradientRect(0, ScaleManager.get(199), -500, Minecraft.getMinecraft().displayWidth, ScaleManager.get(199 + 630), new Color(0xA8000000, true).getRGB(), new Color(0xA8000000, true).getRGB());
        GL11.glPushMatrix();
        GL11.glTranslatef(ScaleManager.get(350F), ScaleManager.get(1197F), 401);
        GL11.glRotatef(20F, 0F, 1F, 0F);
        GuiInventory.func_147046_a(0, 0, (int) ScaleManager.get(500), -30, -1, entity);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }
}
