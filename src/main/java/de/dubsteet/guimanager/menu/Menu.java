package de.dubsteet.guimanager.menu;

import de.dubsteet.guimanager.exceptions.MenuManagerException;
import de.dubsteet.guimanager.exceptions.MenuManagerNotSetupException;
import de.dubsteet.guimanager.menu.enums.InventoryClickStatus;
import de.dubsteet.guimanager.menu.items.ItemMapping;
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

    /**
     * @return The name of the menu
     */
    public abstract String getMenuName();

    /**
     * @return The amount of slots in the menu
     */
    public abstract int getSlots();

    /**
     * @return Should the menu allow all clicks?
     */
    public abstract InventoryClickStatus allowAllClicks();

    /**
     * @param event The InventoryClickEvent
     * @throws MenuManagerNotSetupException If the MenuManager is not setup
     * @throws MenuManagerException         If an error occurs while handling the menu
     */
    public abstract void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException;

    /**
     * Set the items in the menu
     */
    public abstract void setMenuItems();

    /**
     * Open the menu for the player
     */
    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        this.setMenuItems();
        player.openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    /**
     * Send the player back to the previous menu
     */
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

    /**
     * Fill all free slots in the inventory with the filler glass
     */
    public void setFillerGlass() {
        setFillerGlass(FILLER_GLASS);
    }

    /**
     * Fill all free slots in the inventory with the specified ItemStack
     * @param itemStack The ItemStack to fill the inventory with
     */
    public void setFillerGlass(ItemStack itemStack) {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        }
    }

    /**
     * Change the ItemStack used as the filler glass
     * @param itemStack The ItemStack to set as the filler glass
     */
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

    /**
     * @param material    The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore        The lore of the ItemStack
     * @return The constructed ItemStack object
     */
    public ItemStack makeClickableItem(Material material, String displayName, String... lore) {
        ItemStack item = makeItem(material, displayName, lore);
        ItemMapping.addClickableAttribut(item, true);
        return item;
    }

    /**
     * Called when a player closes this menu
     * Override this method to handle menu closing events
     */
    public void handleMenuClose() {
    }
}
