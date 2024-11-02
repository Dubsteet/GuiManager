package de.dubsteet.guimanager.menu.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CustomItem extends ItemStack {

    public CustomItem(Material material) {
        super(material);
        setCustomNames();
        assignCustomKeys();
    }

    protected void setCustomNames() {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(getCustomItemName());
        if (getCustomLore() != null) meta.setLore(getCustomLore());
        setItemMeta(meta);
    }

    protected void assignCustomKeys() {
        ItemMeta meta = getItemMeta();
        meta.getPersistentDataContainer().set(ItemKeys.MOVEABLE, PersistentDataType.STRING, isMoveable() ? "true" : "false");
        meta.getPersistentDataContainer().set(ItemKeys.PLACEABLE, PersistentDataType.STRING, isPlaceable() ? "true" : "false");
        meta.getPersistentDataContainer().set(ItemKeys.DROPPABLE, PersistentDataType.STRING, isDroppable() ? "true" : "false");
        meta.getPersistentDataContainer().set(ItemKeys.RENAMEABLE, PersistentDataType.STRING, isRenameable() ? "true" : "false");
        setItemMeta(meta);
    }

    private void setItemKey(NamespacedKey namespace, String value) {
        ItemMeta meta = getItemMeta();
        meta.getPersistentDataContainer().set(namespace, PersistentDataType.STRING, value);
        setItemMeta(meta);
    }

    @NotNull
    public abstract String getCustomItemName();

    public abstract List<String> getCustomLore();

    protected abstract boolean isMoveable();

    protected abstract boolean isPlaceable();

    protected abstract boolean isDroppable();

    protected abstract boolean isRenameable();

}
