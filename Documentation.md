# GuiManager Documentation

GuiManager is a powerful Bukkit/Spigot plugin that simplifies the creation and management of GUI menus in Minecraft. It provides a robust framework for creating interactive inventory-based GUIs with features like pagination, confirmation dialogs, and chat input handling.

## Table of Contents
- [Setup](#setup)
- [Basic Usage](#basic-usage)
- [Menu Types](#menu-types)
  - [Basic Menu](#basic-menu)
  - [Paginated Menu](#paginated-menu)
  - [Confirmation Menu](#confirmation-menu)
- [Player Menu Utility](#player-menu-utility)
- [Item Management](#item-management)
  - [Custom Items](#custom-items)
  - [Custom Skull Items](#custom-skull-items)
  - [Item Mapping](#item-mapping)
- [Chat Input](#chat-input)
- [Examples](#examples)

## Setup

To use GuiManager in your plugin, you need to set up the MenuManager in your plugin's `onEnable()` method:

```java
@Override
public void onEnable() {
    MenuManager.setup(getServer(), this);
}
```

## Basic Usage
### Creating a Menu
To create a menu, extend the Menu class and implement the required methods:

```java
public class MyMenu extends Menu {

    public MyMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "My Custom Menu";
    }

    @Override
    public int getSlots() {
        return 27; // 3 rows
    }

    @Override
    public void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException {
        // Handle click events here
    }

    @Override
    public void setMenuItems() {
        // Set up your menu items here
        inventory.setItem(13, makeItem(Material.DIAMOND, "§bDiamond Item", "§7Click me!"));
    }
}
```

### Opening a Menu
To open a menu for a player:

```java
try {
    MenuManager.openMenu(MyMenu.class, player);
} catch (MenuManagerException | MenuManagerNotSetupException e) {
    e.printStackTrace();
}
```

## Menu Types
### Basic Menu
The basic Menu class provides the foundation for all menus. It handles inventory creation, item placement, and click events.

Key features:

- Custom menu name
- Configurable size
- Click event handling
- Item creation utilities
### Paginated Menu
The PaginatedMenu class extends Menu to provide pagination for large collections of items.

Key features:

- Navigation buttons (previous/next page)
- Customizable border
- Item pagination
- Player head utilities
Example implementation:

```java
public class MyPaginatedMenu extends PaginatedMenu {

    public MyPaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Paginated Menu";
    }

    @Override
    public List<?> getData() {
        // Return the list of items to paginate
        return Arrays.asList(/* your items */);
    }

    @Override
    public void loopCode(Object object) {
        // Handle each item in the pagination
        ItemStack item = (ItemStack) object;
        addObjectToNextFreeSlot(item);
    }

    @Override
    public HashMap<Integer, ItemStack> getCustomMenuBorderItems() {
        // Return custom border items or null for default
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException {
        // Handle click events
        if (event.getCurrentItem().equals(leftButton)) {
            prevPage();
        } else if (event.getCurrentItem().equals(rightButton)) {
            nextPage();
        } else if (event.getCurrentItem().equals(closeButton)) {
            event.getWhoClicked().closeInventory();
        }
    }
}
```

### Confirmation Menu
The ConfirmationMenu class provides a simple yes/no dialog for confirming actions.

Key features:

- Confirm and cancel buttons
- Customizable message
- Action callbacks
Example implementation:

```java
public class MyConfirmationMenu extends ConfirmationMenu {

    public MyConfirmationMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Confirm Action";
    }

    @Override
    public ItemStack getMessageItem() {
        return makeItem(Material.PAPER, "§eAre you sure?", "§7This action cannot be undone.");
    }

    @Override
    public ItemStack getConfirmItem() {
        return makeItem(Material.GREEN_WOOL, "§aConfirm", "§7Click to confirm");
    }

    @Override
    public ItemStack getCancelItem() {
        return makeItem(Material.RED_WOOL, "§cCancel", "§7Click to cancel");
    }

    @Override
    public void handleConfirm() {
        // Handle confirmation action
        Player player = playerMenuUtility.getOwner();
        player.sendMessage("§aAction confirmed!");
        player.closeInventory();
    }

    @Override
    public void handleCancel() {
        // Handle cancellation action
        Player player = playerMenuUtility.getOwner();
        player.sendMessage("§cAction cancelled.");
        player.closeInventory();
    }
}
```
## Player Menu Utility
The PlayerMenuUtility class manages player-specific data for menus, allowing you to store and retrieve data between menu interactions.

Key features:

- Data storage with string or enum keys
- Menu history tracking
- Player reference
Example usage:

```java
// Store data
playerMenuUtility.setData("selected_item", item);
playerMenuUtility.setData(MyEnum.SELECTED_PLAYER, player);

// Retrieve data
ItemStack item = playerMenuUtility.getData("selected_item", ItemStack.class);
Player selectedPlayer = playerMenuUtility.getData(MyEnum.SELECTED_PLAYER, Player.class);

// Menu history
Menu previousMenu = playerMenuUtility.lastMenu();
playerMenuUtility.pushMenu(currentMenu);
```

## Item Management
### Custom Items
The CustomItem abstract class provides a foundation for creating custom items with persistent data.

Key features:

- Custom name and lore
- Persistent data storage
- Item behavior flags (moveable, placeable, etc.)
Example implementation:

```java
public class MyCustomItem extends CustomItem {

    @Override
    public @NotNull String getCustomItemName() {
        return "§6Special Item";
    }

    @Override
    public List<String> getCustomLore() {
        return Arrays.asList("§7This is a special item", "§7with custom properties");
    }

    @Override
    public @NotNull Material getCustomItemMaterial() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    protected boolean isMoveable() {
        return false;
    }

    @Override
    protected boolean isPlaceable() {
        return false;
    }

    @Override
    protected boolean isDroppable() {
        return false;
    }

    @Override
    protected boolean isRenameable() {
        return false;
    }
}
```

### Custom Skull Items
The CustomSkullItem abstract class extends CustomItem to create custom player heads with custom textures.

Key features:

- Custom texture URL
- Custom UUID and name
- All features of CustomItem
Example implementation:

```java
public class MySkullItem extends CustomSkullItem {

    @Override
    protected @NotNull String getCustomUUID() {
        return "00000000-0000-0000-0000-000000000000"; // Random UUID
    }

    @Override
    protected @NotNull String getCustomName() {
        return "Custom Skull";
    }

    @Override
    protected @NotNull String getTextureUrl() {
        return "http://textures.minecraft.net/texture/..."; // Texture URL
    }

    @Override
    public @NotNull String getCustomItemName() {
        return "§dSpecial Skull";
    }

    @Override
    public List<String> getCustomLore() {
        return Arrays.asList("§7A custom skull item", "§7with a unique texture");
    }

    @Override
    protected boolean isMoveable() {
        return true;
    }

    @Override
    protected boolean isPlaceable() {
        return false;
    }

    @Override
    protected boolean isDroppable() {
        return true;
    }

    @Override
    protected boolean isRenameable() {
        return false;
    }
}
```

### Item Mapping
The ItemMapping class provides utilities for adding and retrieving persistent data from items.

Key features:

- Clickable attribute
- Unique ID storage
- Menu function assignment
Example usage:

```java
// Make an item clickable in menus
ItemStack item = new ItemStack(Material.DIAMOND);
ItemMapping.addClickableAttribut(item, true);

// Check if an item is clickable
boolean isClickable = ItemMapping.isClickable(item);

// Add a unique ID to an item
ItemMapping.addUniqueId(item, player.getUniqueId().toString());

// Retrieve the unique ID
String uniqueId = ItemMapping.getUniqueId(item);

// Add a menu function to an item
ItemMapping.addMenuFunction(item, "TELEPORT");

// Get the menu function
String function = ItemMapping.getMenuFunction(item);
```

## Chat Input
The ChatInput class provides utilities for requesting text input from players via chat.

Key features:

- Text input with validation
- Numeric input
- Callback handling
- Cancellation support
Example usage:

```java
// Simple text input
ChatInput.requestInput(plugin, player, "Enter your name:", input -> {
    player.sendMessage("You entered: " + input);
});

// Validated input
ChatInput.requestInput(plugin, player, "Enter a color (red, green, or blue):",
    input -> input.equalsIgnoreCase("red") || 
             input.equalsIgnoreCase("green") || 
             input.equalsIgnoreCase("blue"),
    input -> player.sendMessage("You selected: " + input),
    () -> player.sendMessage("Input cancelled.")
);

// Numeric input
ChatInput.requestNumericInput(plugin, player, "Enter a number:", number -> {
    player.sendMessage("You entered: " + number);
});
```

## Examples
### Creating a Player Selector Menu
This example demonstrates how to create a menu for selecting players:

```java
public class PlayerSelectorMenu extends PaginatedMenu {

    public PlayerSelectorMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select a Player";
    }

    @Override
    public List<?> getData() {
        return turnPlayerListToSkullItemList(
            new ArrayList<>(Bukkit.getOnlinePlayers()),
            Arrays.asList("§7Click to select this player")
        );
    }

    @Override
    public void loopCode(Object object) {
        ItemStack playerHead = (ItemStack) object;
        addObjectToNextFreeSlot(playerHead);
    }

    @Override
    public HashMap<Integer, ItemStack> getCustomMenuBorderItems() {
        return null;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) throws MenuManagerNotSetupException, MenuManagerException {
        if (event.getCurrentItem().equals(leftButton)) {
            prevPage();
        } else if (event.getCurrentItem().equals(rightButton)) {
            nextPage();
        } else if (event.getCurrentItem().equals(closeButton)) {
            event.getWhoClicked().closeInventory();
        } else if (event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            String uuid = ItemMapping.getUniqueId(event.getCurrentItem());
            Player selectedPlayer = Bukkit.getPlayer(UUID.fromString(uuid));
            
            if (selectedPlayer != null) {
                playerMenuUtility.setData("selected_player", selectedPlayer);
                // Do something with the selected player
                event.getWhoClicked().sendMessage("§aYou selected: §f" + selectedPlayer.getName());
                event.getWhoClicked().closeInventory();
            }
        }
    }
}
```

Creating a Confirmation Dialog with Action
```java
public class DeleteConfirmationMenu extends ConfirmationMenu {

    public DeleteConfirmationMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Confirm Deletion";
    }

    @Override
    public ItemStack getMessageItem() {
        return makeItem(Material.BARRIER, "§cDelete Item?", 
            "§7Are you sure you want to delete this item?",
            "§7This action cannot be undone.");
    }

    @Override
    public ItemStack getConfirmItem() {
        return makeItem(Material.GREEN_WOOL, "§aYes, Delete It", "§7Click to confirm deletion");
    }

    @Override
    public ItemStack getCancelItem() {
        return makeItem(Material.RED_WOOL, "§cNo, Keep It", "§7Click to cancel deletion");
    }

    @Override
    public void handleConfirm() {
        Player player = playerMenuUtility.getOwner();
        ItemStack itemToDelete = playerMenuUtility.getData("item_to_delete", ItemStack.class);
        
        if (itemToDelete != null) {
            // Perform deletion logic
            player.sendMessage("§aItem has been deleted.");
        }
        
        player.closeInventory();
    }

    @Override
    public void handleCancel() {
        Player player = playerMenuUtility.getOwner();
        player.sendMessage("§cDeletion cancelled.");
        
        try {
            // Return to previous menu
            MenuManager.openMenu(playerMenuUtility.lastMenu().getClass(), player);
        } catch (Exception e) {
            player.closeInventory();
        }
    }
}
```
