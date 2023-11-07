package de.dubsteet.guimanager.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmationMenu extends Menu {

    protected String confirmationMessage;
    protected ItemStack confirmItem = makeItem(Material.GREEN_TERRACOTTA, "§2Confirm");
    protected ItemStack cancelItem = makeItem(Material.RED_TERRACOTTA, "§4Cancel");
    protected ItemStack messageItem;

    public ConfirmationMenu(PlayerMenuUtility playerMenuUtility, String confirmationMessage) {
        super(playerMenuUtility);
        this.confirmationMessage = confirmationMessage;
        this.messageItem = makeItem(Material.PAPER, confirmationMessage);
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, messageItem);
        inventory.setItem(30, confirmItem);
        inventory.setItem(32, cancelItem);
        setFillerGlass();
    }

    public void setMessageItem(Material material) {
        this.confirmItem = makeItem(material, confirmationMessage);
    }

    public void changeMessage(String message) {
        this.confirmationMessage = message;
        this.messageItem = makeItem(messageItem.getType(), confirmationMessage);
    }

    public void changeMessage(String message, Material material) {
        this.confirmationMessage = message;
        this.messageItem = makeItem(material, confirmationMessage);
    }

    public void setConfirmItem(ItemStack item) {
        this.confirmItem = item;
    }

    public void setCancelItem(ItemStack item) {
        this.cancelItem = item;
    }
}
