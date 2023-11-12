package de.dubsteet.guimanager.menu;

import exceptions.MenuManagerException;
import exceptions.MenuManagerNotSetupException;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public abstract class ConfirmationMenu extends Menu {

    protected String confirmationMessage;

    public ConfirmationMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, getMessageItem());
        inventory.setItem(30, getConfirmItem());
        inventory.setItem(32, getCancelItem());
        if(useFillerGlass()) {
            setFillerGlass();
        }
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public int getSlots() {
        return 5*9;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException {
        if(event.getCurrentItem() == null) return;
        if (event.getCurrentItem().equals(getConfirmItem())) {
            handleConfirm();
        } else if (event.getCurrentItem().equals(getCancelItem())) {
            handleCancel();
        }
    }

    //Get ItemStack for the confirmation message
    public abstract ItemStack getMessageItem();

    public abstract ItemStack getConfirmItem();

    public abstract ItemStack getCancelItem();

    public abstract void handleConfirm();

    public abstract void handleCancel();

    public boolean useFillerGlass() {
        return true;
    }
}
