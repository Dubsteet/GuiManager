package de.dubsteet.guimanager.menu.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ItemMapping {

    public static void addClickableAttribut(ItemStack stack, boolean clickable) {
        ItemMeta meta = stack.getItemMeta();
        meta.getPersistentDataContainer().set(ItemKeys.IS_CLICKABLE, PersistentDataType.BOOLEAN, clickable);
        stack.setItemMeta(meta);
    }

    public static boolean isClickable(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemKeys.IS_CLICKABLE, PersistentDataType.BOOLEAN)) {
            return false;
        }
        return Boolean.TRUE.equals(container.get(ItemKeys.IS_CLICKABLE, PersistentDataType.BOOLEAN));
    }


}