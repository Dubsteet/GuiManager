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
            System.out.println("§aMenu clicked");
            if (event.getCurrentItem() == null) return;
            System.out.println("§aItem not null");
            //Checks if clicking is allowed
            if (holder.cancelAllClicks()) {
                System.out.println("§aClicking not allowed");
                event.setCancelled(true);
            }
            try {
                System.out.println("§aHandling menu");
                holder.handleMenu(event);
                System.out.println("§aHandled menu");
            } catch (MenuManagerNotSetupException menuManagerNotSetupException) {
                System.out.println("§4THE MENU MANAGER HAS NOT BEEN CONFIGURED. CALL MENUMANAGER.SETUP()");
            } catch (MenuManagerException menuManagerException) {
                menuManagerException.printStackTrace();
            }
        }
    }
}
