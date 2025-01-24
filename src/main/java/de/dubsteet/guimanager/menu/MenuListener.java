package de.dubsteet.guimanager.menu;

import de.dubsteet.guimanager.exceptions.MenuManagerException;
import de.dubsteet.guimanager.exceptions.MenuManagerNotSetupException;
import de.dubsteet.guimanager.menu.items.ItemMapping;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof Menu holder) {
            if (event.getCurrentItem() == null) return;
            //Checks if clicking is allowed
            switch (holder.allowAllClicks()) {
                case ALLOWED:
                    break;
                case DENIED:
                    event.setCancelled(true);
                    break;
                case ITEM_SPECIFIC:
                    if (!ItemMapping.isClickable(event.getCurrentItem())) {
                        event.setCancelled(true);
                        break;
                    }
            }
            try {
                holder.handleMenu(event);
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                System.out.println("§4THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();

        if (holder instanceof Menu menu) {
            menu.handleMenuClose();
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        //DEBUG
        Player player = event.getPlayer();
        Bukkit.getLogger().info(player.getName() + " has left the server menu will be removed");
        PlayerMenuUtility playerMenuUtility = MenuManager.playerMenuUtilityMap.get(player);
        if (playerMenuUtility != null) {
            //DEBUG
            Bukkit.getLogger().info("Removing menu for " + player.getName());
            MenuManager.playerMenuUtilityMap.remove(player);
        }
    }
}
