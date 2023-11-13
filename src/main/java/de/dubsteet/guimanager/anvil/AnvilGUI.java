package de.dubsteet.guimanager.anvil;

import de.dubsteet.guimanager.menu.Menu;
import de.dubsteet.guimanager.menu.PlayerMenuUtility;
import exceptions.MenuManagerException;
import exceptions.MenuManagerNotSetupException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class AnvilGUI extends Menu {

    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected Player player;
    public AnvilGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
        this.playerMenuUtility = playerMenuUtility;
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(0, getFirstItem());
        inventory.setItem(1, getSecondItem());
        inventory.setItem(2, getResultItem());
    }
    @Override
    public void open() {
        inventory = Bukkit.createInventory(this, InventoryType.ANVIL, getMenuName());
        setMenuItems();
        player.openInventory(inventory);
        playerMenuUtility.pushMenu(this);
    }

    public abstract ItemStack getFirstItem();

    public abstract ItemStack getSecondItem();

    public abstract ItemStack getResultItem();

}
