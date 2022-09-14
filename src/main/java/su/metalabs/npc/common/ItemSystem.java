package su.metalabs.npc.common;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.apache.commons.lang3.math.NumberUtils;
import su.metalabs.lib.handlers.currency.CurrencyHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemSystem extends Item {

    IIcon[] icons;
    public ItemSystem() {
        setMaxDamage(0);
        setMaxStackSize(1);
        setHasSubtypes(true);
        //setTextureName(CurrencyHandler.getGoldCurrency().getIcon().getResourceDomain() + ":" + CurrencyHandler.getGoldCurrency().getIcon().getResourcePath().replace(".png", ""));
        GameRegistry.registerItem(this, "itemSystem");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon(CurrencyHandler.getGoldCurrency().getIcon().getResourceDomain() + ":" + CurrencyHandler.getGoldCurrency().getIcon().getResourcePath().replace(".png", ""));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage) {
        return this.itemIcon;
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {
        int dmg = p_77667_1_.getItemDamage();
        switch (dmg) {
            case 0:
                return "currency." + CurrencyHandler.getRubCurrency().getCurrencyId();
            case 1:
                return "currency." + CurrencyHandler.getGemCurrency().getCurrencyId();
            case 2:
                return "currency." + CurrencyHandler.getGoldCurrency().getCurrencyId();
        }
        return "currency.unknown";
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        for (int i = 0; i < CurrencyHandler.currencyList.size(); ++i) {
            p_150895_3_.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        int dmg = p_77624_1_.getItemDamage();
        switch (dmg) {
            case 0:
                p_77624_3_.add("currency." + CurrencyHandler.getRubCurrency().getCurrencyId());
                break;
            case 1:
                p_77624_3_.add("currency." + CurrencyHandler.getGemCurrency().getCurrencyId());
                break;
            case 2:
                p_77624_3_.add("currency." + CurrencyHandler.getGoldCurrency().getCurrencyId());
        }
    }

    public static int getRub(ItemStack stack) {
        if (stack.getItemDamage() == 0)
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        return -1;
    }

    public static int getGem(ItemStack stack) {
        if (stack.getItemDamage() == 1)
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        return -1;
    }

    public static int getGold(ItemStack stack) {
        if (stack.getItemDamage() == 2)
            return NumberUtils.isNumber(stack.getDisplayName()) ? NumberUtils.toInt(stack.getDisplayName()) : 1;
        return -1;
    }
}
