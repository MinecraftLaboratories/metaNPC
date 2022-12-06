package su.metalabs.npc.common;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.apache.commons.lang3.math.NumberUtils;
import su.metalabs.lib.handlers.currency.CurrencyHandler;
import su.metalabs.npc.Reference;
import su.metalabs.npc.proxy.CommonProxy;

import java.util.List;

public class ItemSetup extends Item {
    public static final CreativeTabs CURRENCY_TAB = new CreativeTabs("currencyTab") {
        @Override
        public Item getTabIconItem() {
            return CommonProxy.setupItem;
        }
    };
    IIcon[] icons = new IIcon[3];
    public ItemSetup() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        GameRegistry.registerItem(this, "itemSystem");
        this.setCreativeTab(CURRENCY_TAB);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons[0] = iconRegister.registerIcon(Reference.MOD_ID + ":" + CurrencyHandler.getRubCurrency().getCurrencyId());
        icons[1] = iconRegister.registerIcon(Reference.MOD_ID + ":" + CurrencyHandler.getGemCurrency().getCurrencyId());
        icons[2] = iconRegister.registerIcon(Reference.MOD_ID + ":" + CurrencyHandler.getGoldCurrency().getCurrencyId());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return icons[damage];
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        int dmg = is.getItemDamage();
        String base = "item.currency_";
        switch (dmg) {
            case 0:
                return base + "rub";
            case 1:
                return base + "gem";
            case 2:
                return base + "gold";
            default:
                return base + "unknown";
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        for (int i = 0; i < CurrencyHandler.currencyList.size(); ++i) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    public static int getRub(ItemStack stack) {
        if (stack.getItemDamage() == 0) {
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        }

        return -1;
    }

    public static int getGem(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        }

        return -1;
    }

    public static int getGold(ItemStack stack) {
        if (stack.getItemDamage() == 2) {
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        }

        return -1;
    }
}
