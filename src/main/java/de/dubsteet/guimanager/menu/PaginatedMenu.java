package de.dubsteet.guimanager.menu;

import de.dubsteet.guimanager.menu.items.ItemMapping;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PaginatedMenu extends Menu {

    //The items being paginated
    protected List<Object> data;

    //Keep track of what page the menu is on
    protected int page = 0;
    //28 is max items because with the border set below,
    //28 empty slots are remaining.
    protected int maxItemsPerPage = 28;
    //the index represents the index of the slot
    //that the loop is on
    protected int index = 0;

    protected ItemStack leftButton = makeItem(Material.DARK_OAK_BUTTON, "§eLeft");
    protected ItemStack rightButton = makeItem(Material.DARK_OAK_BUTTON, "§eRight");
    protected ItemStack closeButton = makeItem(Material.BARRIER, "§cClose");

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public int getSlots() {
        return 54;
    }

    /**
     * @return A list of the data being paginated. usually this is a list of items, but it can be anything
     */
    public abstract List<?> getData();

    /**
     * @param object A single element of the data list that you do something with. It is recommended that you turn this into an item if it is not already and then put it into the inventory as you would with a normal Menu. You can execute any other logic in here as well.
     */
    public abstract void loopCode(Object object);

    /**
     * @return A hashmap of items you want to be placed in the paginated menu border. This will override any items already placed by default. Key = slot, Value = Item
     */
    @Nullable
    public abstract HashMap<Integer, ItemStack> getCustomMenuBorderItems();

    /**
     * Set the border and menu buttons for the menu. Override this method to provide a custom menu border or specify custom items in customMenuBorderItems()
     */
    protected void addMenuBorder() {

        inventory.setItem(48, leftButton);
        inventory.setItem(49, closeButton);
        inventory.setItem(50, rightButton);

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        //place the custom items if they exist
        if (getCustomMenuBorderItems() != null) {
            getCustomMenuBorderItems().forEach((integer, itemStack) -> inventory.setItem(integer, itemStack));
        }

    }

    /**
     * Place each item in the paginated menu, automatically coded by default but override if you want custom functionality. Calls the loopCode() method you define for each item returned in the getData() method
     */
    @Override
    public void setMenuItems() {

        addMenuBorder();

        List<Object> data = (List<Object>) getData();

        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= data.size()) break;
                if (data.get(index) != null) {
                    loopCode(data.get(index)); //run the code defined by the user
                }
            }
        }


    }

    /**
     * @return true if successful, false if already on the first page
     */
    public boolean prevPage() {
        if (page == 0) {
            return false;
        } else {
            page = page - 1;
            reloadItems();
            return true;
        }
    }

    /**
     * @return true if successful, false if already on the last page
     */
    public boolean nextPage() {
        if (!((index + 1) >= getData().size())) {
            page = page + 1;
            reloadItems();
            return true;
        } else {
            return false;
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    /**
     * Change all the buttons in the paginated menu
     * @param leftButton Button to go to the previous page
     * @param rightButton Button to go to the next page
     * @param closeButton Button to close the menu
     */
    public void changeButtons(ItemStack leftButton, ItemStack rightButton, ItemStack closeButton) {
        changeLeftButton(leftButton);
        changeRightButton(rightButton);
        changeCloseButton(closeButton);
    }

    /**
     * Change the left button in the paginated menu
     * @param leftButton Button to go to the previous page
     */
    public void changeLeftButton(ItemStack leftButton) {
        this.leftButton = leftButton;
    }

    /**
     * Change the right button in the paginated menu
     * @param rightButton Button to go to the next page
     */
    public void changeRightButton(ItemStack rightButton) {
        this.rightButton = rightButton;
    }

    /**
     * Change the close button in the paginated menu
     * @param closeButton Button to close the menu
     */
    public void changeCloseButton(ItemStack closeButton) {
        this.closeButton = closeButton;
    }

    /**
     * Add an object to the next free slot in the inventory
     * @param object The object (ItemStack) to add to the inventory
     */
    public void addObjectToNextFreeSlot(Object object) {
        for (int i = 0; i < inventory.getSize()-1; i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) {
                inventory.setItem(i, (ItemStack) object);
                break;
            }
        }
    }

    public List<ItemStack> turnPlayerListToSkullItemList(List<Player> players) {
        List<ItemStack> itemStacks = new ArrayList<>();
        if(players == null || players.isEmpty()) return itemStacks;
        for (Player player : players) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwningPlayer(player);
            itemStack.setItemMeta(skullMeta);
            ItemMapping.addUniqueId(itemStack, player.getUniqueId().toString());
            itemStacks.add(itemStack);
        }
        return itemStacks;
    }

    public List<ItemStack> turnPlayerListToSkullItemList(List<Player> players, List<String> lore) {
        List<ItemStack> itemStacks = new ArrayList<>();
        if(players == null || players.isEmpty()) return itemStacks;
        for (Player player : players) {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setOwningPlayer(player);
            skullMeta.setLore(lore);
            itemStack.setItemMeta(skullMeta);
            itemStacks.add(itemStack);
        }
        return itemStacks;
    }
}
