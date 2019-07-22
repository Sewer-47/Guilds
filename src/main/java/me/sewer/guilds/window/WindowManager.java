package me.sewer.guilds.window;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.Inventory;

public class WindowManager {

    private final Map<Inventory, Window> byInventory = new HashMap<>();

    public void registerWindow(Window window) {
        this.byInventory.put(window.getInventory(), window);
    }

    public void unregisterWindow(Window window) {
        this.byInventory.remove(window);
    }

    public Window get(Inventory inventory) {
        return this.byInventory.get(inventory);
    }
}


