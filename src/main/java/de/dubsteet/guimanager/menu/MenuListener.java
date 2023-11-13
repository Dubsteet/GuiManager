package de.dubsteet.guimanager.menu;

import exceptions.MenuManagerException;
import exceptions.MenuManagerNotSetupException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        System.out.println(event.getInventory().getHolder());
        if (event.getInventory().getHolder() instanceof Menu holder) {
            if (event.getCurrentItem() == null) return;
            //Checks if clicking is allowed
            if (holder.cancelAllClicks()) {
                event.setCancelled(true);
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
}
