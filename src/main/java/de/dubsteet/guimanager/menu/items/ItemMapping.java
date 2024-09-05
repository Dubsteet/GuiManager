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

    public static void addUniqueId(ItemStack stack, String uniqueId) {
        ItemMeta meta = stack.getItemMeta();
        meta.getPersistentDataContainer().set(ItemKeys.UNIQUE_ID, PersistentDataType.STRING, uniqueId);
        stack.setItemMeta(meta);
    }

    public static String getUniqueId(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(ItemKeys.UNIQUE_ID, PersistentDataType.STRING)) {
            return null;
        }
        return container.get(ItemKeys.UNIQUE_ID, PersistentDataType.STRING);
    }


}
