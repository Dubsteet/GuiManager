package de.dubsteet.guimanager.menu;

import exceptions.MenuManagerException;
import exceptions.MenuManagerNotSetupException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;
    protected Player player;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(Material.WHITE_STAINED_GLASS_PANE, " ");

    public Menu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
        this.player = playerMenuUtility.getOwner();
    }

    public abstract String getMenuName();

    public abstract int getSlots();

    public abstract boolean cancelAllClicks();

    public abstract void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException;

    public abstract void setMenuItems();

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        player.openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    public void back() throws MenuManagerNotSetupException, MenuManagerException {
        MenuManager.openMenu(playerMenuUtility.lastMenu().getClass(), player);
    }

    protected void reloadItems() {
        inventory.clear();
        setMenuItems();
    }

    protected void reload() throws MenuManagerNotSetupException, MenuManagerException {
        player.closeInventory();
        MenuManager.openMenu(this.getClass(), player);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void setFillerGlass() {
        setFillerGlass(FILLER_GLASS);
    }

    public void setFillerGlass(ItemStack itemStack) {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        }
    }

    public void changeFillerGlass(ItemStack itemStack) {
        this.FILLER_GLASS = itemStack;
    }

    /**
     * @param material    The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore        The lore of the ItemStack
     * @return The constructed ItemStack object
     */
    public ItemStack makeItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }
}
