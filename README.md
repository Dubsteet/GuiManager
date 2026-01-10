# GuiManager

A powerful and easy-to-use GUI menu library for Spigot/Bukkit 1.20.2 plugins. Simplify the creation of interactive inventory-based menus with support for pagination, confirmation dialogs, custom items, and chat input.

Based on [SimpApi by Kody Simpson](https://github.com/Cortex-MC/SimpAPI).

## Features

- **Easy Menu Creation**: Simple abstract classes for creating custom menus
- **Pagination Support**: Built-in paginated menus for large item lists
- **Confirmation Dialogs**: Ready-to-use confirmation menus for user actions
- **Player Session Management**: Per-player data storage across menu interactions
- **Custom Items**: Framework for creating items with persistent data
- **Chat Input**: Request and validate text input from players
- **Navigation History**: Automatic menu history tracking with back() functionality

## Installation

GuiManager is available through JitPack. Add it as a dependency to your project:

### Maven

Add the JitPack repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Add the dependency:
```xml
<dependency>
    <groupId>com.github.Dubsteet</groupId>
    <artifactId>GuiManager</artifactId>
    <version>2.0.1</version>
</dependency>
```

### Gradle

Add the JitPack repository:
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add the dependency:
```gradle
dependencies {
    implementation 'com.github.Dubsteet:GuiManager:2.0.1'
}
```

## Quick Start

### 1. Setup MenuManager

Initialize the MenuManager in your plugin's `onEnable()` method:

```java
@Override
public void onEnable() {
    MenuManager.setup(getServer(), this);
}
```

### 2. Create a Menu

Extend the `Menu` class and implement the required methods:

```java
public class ExampleMenu extends Menu {

    public ExampleMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Example Menu";
    }

    @Override
    public int getSlots() {
        return 27; // 3 rows
    }

    @Override
    public InventoryClickStatus allowAllClicks() {
        return InventoryClickStatus.CANCEL_ALL;
    }

    @Override
    public void handleMenu(InventoryClickEvent event) {
        // Handle click events
        if (event.getSlot() == 13) {
            player.sendMessage("You clicked the center item!");
        }
    }

    @Override
    public void setMenuItems() {
        inventory.setItem(13, makeItem(Material.DIAMOND, "§bClick Me!", "§7This is an example item"));
        setFillerGlass(); // Fill empty slots with glass panes
    }
}
```

### 3. Open the Menu

Open the menu for a player:

```java
try {
    MenuManager.openMenu(ExampleMenu.class, player);
} catch (MenuManagerException | MenuManagerNotSetupException e) {
    e.printStackTrace();
}
```

## Documentation

For comprehensive documentation including:
- Paginated menus
- Confirmation dialogs
- Custom items and skull items
- Chat input handling
- Advanced examples

See [Documentation.md](Documentation.md)

## Requirements

- Java 16+
- Spigot/Paper 1.20.2+

## Version

Current version: **2.0.1**