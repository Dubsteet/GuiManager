package de.dubsteet.guimanager.menu;

import de.dubsteet.guimanager.exceptions.MenuManagerException;
import de.dubsteet.guimanager.exceptions.MenuManagerNotSetupException;
import de.dubsteet.guimanager.menu.enums.InventoryClickStatus;
import de.dubsteet.guimanager.menu.items.ItemMapping;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public abstract class ConfirmationMenu extends Menu {

    public ConfirmationMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public void setMenuItems() {
        ItemStack confirmationItem = getConfirmItem();
        ItemMapping.addMenuFunction(confirmationItem, "CONFIRM");
        ItemStack cancelItem = getCancelItem();
        ItemMapping.addMenuFunction(cancelItem, "CANCEL");
        inventory.setItem(getMessageSlot(), getMessageItem());
        inventory.setItem(getConfirmationSlot(), getConfirmItem());
        inventory.setItem(getCancelSlot(), getCancelItem());
        if(useFillerGlass()) {
            setFillerGlass();
        }
    }

    @Override
    public InventoryClickStatus allowAllClicks() {
        return InventoryClickStatus.DENIED;
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

    /**
     * @return the slot for the confirmation button
     */
    public int getConfirmationSlot() {
        return 30;
    }

    /**
     * @return the slot for the cancel button
     */
    public int getCancelSlot() {
        return 32;
    }

    /**
     * @return the slot for the message
     */
    public int getMessageSlot() {
        return 13;
    }

    /**
     * @return the ItemStack for the message with the message
     */
    public abstract ItemStack getMessageItem();

    /**
     * @return the ItemStack for the confirm button
     */
    public abstract ItemStack getConfirmItem();

    /**
     * @return the ItemStack for the cancel button
     */
    public abstract ItemStack getCancelItem();

    public abstract void handleConfirm();

    public abstract void handleCancel();

    public boolean useFillerGlass() {
        return true;
    }
}
