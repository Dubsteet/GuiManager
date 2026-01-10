# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

GuiManager is a Spigot/Bukkit plugin library for creating inventory-based GUI menus in Minecraft 1.20.2. It provides a framework for building interactive menus with pagination, confirmation dialogs, custom items, and chat input handling. Based on SimpApi by Kody Simpson.

## Build Commands

### Building the project
```bash
mvn clean install
```

This uses Maven with Java 16 as the compiler target (despite Java 1.8 property declaration). The build produces a shaded JAR with all dependencies included.

### JitPack builds
The project is configured for JitPack distribution. The `jitpack.yml` and `ensure-java-16` script ensure Java 16 is used during CI builds.

## Architecture

### Core Design Pattern

The library uses a **singleton MenuManager** pattern with per-player state management:

1. **MenuManager** (src/main/java/de/dubsteet/guimanager/menu/MenuManager.java:14)
   - Must be initialized once via `MenuManager.setup(server, plugin)` during plugin onEnable
   - Maintains a HashMap of PlayerMenuUtility objects keyed by Player
   - Opens menus via reflection: `MenuManager.openMenu(MenuClass.class, player)`
   - Registers a global MenuListener for inventory click events

2. **PlayerMenuUtility** (src/main/java/de/dubsteet/guimanager/menu/PlayerMenuUtility.java:8)
   - Stores per-player session data in a HashMap (supports String or Enum keys)
   - Maintains menu navigation history using a Stack
   - Retrieved via `MenuManager.getPlayerMenuUtility(player)`
   - Critical for passing data between menus

3. **Menu Hierarchy** (src/main/java/de/dubsteet/guimanager/menu/Menu.java:19)
   ```
   Menu (abstract base class)
   ├── PaginatedMenu (abstract, 54-slot with navigation)
   └── ConfirmationMenu (abstract, yes/no dialog)
   ```

### Menu Lifecycle

1. Constructor receives PlayerMenuUtility
2. `open()` creates Bukkit inventory, calls `setMenuItems()`, opens for player, pushes to history
3. Clicks handled by MenuListener, delegated to menu's `handleMenu()`
4. Menu can call `back()` to return to previous menu in history stack
5. `handleMenuClose()` called when inventory closes

### Item Management System

**ItemMapping** (src/main/java/de/dubsteet/guimanager/menu/items/ItemMapping.java) uses Bukkit's PersistentDataContainer to store metadata on ItemStacks:
- Clickable attribute (determines if click events should be processed)
- Unique IDs (e.g., player UUIDs for skull items)
- Menu functions (string identifiers for button actions)

**CustomItem** and **CustomSkullItem** (src/main/java/de/dubsteet/guimanager/menu/items/) provide abstract base classes for items with:
- Persistent data storage via ItemKeys
- Behavior flags (moveable, placeable, droppable, renameable)
- Custom skull textures via base64-encoded URLs

### Key Implementation Details

**PaginatedMenu** (src/main/java/de/dubsteet/guimanager/menu/PaginatedMenu.java:14):
- Fixed 54-slot inventory (6 rows)
- 28 usable slots per page (rest are border/navigation)
- Border slots: 0-9, 17-18, 26-27, 35-36, 44-53
- Navigation buttons at slots 48 (left), 49 (close), 50 (right)
- Override `getData()` to return items and `loopCode(Object)` to handle each item
- Use `turnPlayerListToSkullItemList()` to convert Player list to skull items with UUIDs

**ConfirmationMenu** (src/main/java/de/dubsteet/guimanager/menu/ConfirmationMenu.java):
- Fixed layout with confirm (slot 11), cancel (slot 15), and message (slot 13) items
- Override `handleConfirm()` and `handleCancel()` for action logic
- Common pattern: store data in PlayerMenuUtility before opening confirmation menu

**ChatInput** (src/main/java/de/dubsteet/guimanager/chatinput/ChatInput.java):
- Temporary chat listener for text input from players
- Supports validation predicates and cancellation callbacks
- Use `requestInput()` for text, `requestNumericInput()` for numbers
- Player can type "cancel" to abort input

## Common Patterns

### Opening a menu with data
```java
PlayerMenuUtility pmu = MenuManager.getPlayerMenuUtility(player);
pmu.setData("item_to_edit", itemStack);
MenuManager.openMenu(EditItemMenu.class, player);
```

### Implementing handleMenu in paginated menu
```java
if (event.getCurrentItem().equals(leftButton)) {
    prevPage();
} else if (event.getCurrentItem().equals(rightButton)) {
    nextPage();
} else if (event.getCurrentItem().equals(closeButton)) {
    player.closeInventory();
} else if (ItemMapping.isClickable(event.getCurrentItem())) {
    // Handle item click
}
```

### Returning to previous menu
```java
MenuManager.openMenu(playerMenuUtility.lastMenu().getClass(), player);
```

## Exception Handling

- **MenuManagerNotSetupException**: Thrown if `MenuManager.setup()` not called
- **MenuManagerException**: Generic exception for reflection failures or other errors
- Both must be caught when calling `MenuManager.openMenu()` or `getPlayerMenuUtility()`

## Important Notes

- All Menu subclasses MUST have a constructor taking `PlayerMenuUtility` as the only parameter
- The `allowAllClicks()` method (src/main/java/de/dubsteet/guimanager/menu/enums/InventoryClickStatus.java) controls whether clicks are cancelled by default
- The `lastMenu()` implementation (src/main/java/de/dubsteet/guimanager/menu/PlayerMenuUtility.java:76) pops the stack twice by design
- Item comparison in `handleMenu()` should use `.equals()` for navigation buttons
- Use `makeItem()` for display items, `makeClickableItem()` for items that need click detection
