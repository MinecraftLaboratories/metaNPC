package com.nikita23830.metanpc.client;

import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.block.Block;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FixContainerCrash {

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static Slot getSlot(Container container, int index) {
        if (container.inventorySlots.size() > index)
            return (Slot) container.inventorySlots.get(index);
        return null;
    }

    @Hook(returnCondition = ReturnCondition.ALWAYS)
    public static void putStacksInSlots(Container container, ItemStack[] p_75131_1_) {
        if (p_75131_1_ == null)
            return;
        if (container.inventorySlots == null)
            return;
        for (int i = 0; i < p_75131_1_.length; ++i) {
            if (container.inventorySlots.size() > i) {
                Slot slot = container.getSlot(i);
                if (slot != null)
                    slot.putStack(p_75131_1_[i]);
            }
        }
    }
}
