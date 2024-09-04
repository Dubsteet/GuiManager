package de.dubsteet.guimanager.menu;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import de.dubsteet.guimanager.exceptions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuManager {

    //Each Player can have their PlayerMenuUtility object stored in this HashMap
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    private static boolean setup = false;

    public static void registerMenuListener(Server server, Plugin plugin) {
        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }
        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }
    }

    public static void setup(Server server, Plugin plugin) {

        registerMenuListener(server, plugin);
        setup = true;

    }

    /**
     * @param menuClass The class reference of the Menu you want to open for a player
     * @param player    The player to open the menu for
     * @throws MenuManagerNotSetupException Thrown if the setup() method has not been called and used properly
     */
    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            menuClass.getConstructor(PlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player)).open();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new MenuManagerException();
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player player) throws MenuManagerException, MenuManagerNotSetupException {

        if (!setup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(player))) {

            playerMenuUtility = new PlayerMenuUtility(player);
            playerMenuUtilityMap.put(player, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(player);
        }
    }

}
