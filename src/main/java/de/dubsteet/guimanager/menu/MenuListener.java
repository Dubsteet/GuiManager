package de.dubsteet.guimanager.menu;

import de.dubsteet.guimanager.exceptions.MenuManagerException;
import de.dubsteet.guimanager.exceptions.MenuManagerNotSetupException;
import de.dubsteet.guimanager.menu.items.ItemMapping;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
}
