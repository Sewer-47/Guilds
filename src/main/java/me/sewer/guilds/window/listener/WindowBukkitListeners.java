package me.sewer.guilds.window.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.window.Window;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class WindowBukkitListeners implements Listener {

    private final GuildsPlugin plugin;

    public WindowBukkitListeners(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory() != null) {
            if (this.plugin.getWindowManager().get(event.getInventory()) != null) {
                Window window = this.plugin.getWindowManager().get(event.getInventory());
                this.plugin.getUserManager().getUser(event.getPlayer()).ifPresent(user -> window.onOpen(user));
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory() != null) {
            if (this.plugin.getWindowManager().get(event.getInventory()) != null) {
                Window window = this.plugin.getWindowManager().get(event.getInventory());
                this.plugin.getUserManager().getUser(event.getPlayer()).ifPresent(user -> window.onClose(user));
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getCurrentItem() != null) {
            if (this.plugin.getWindowManager().get(event.getInventory()) != null) {
                Window window = this.plugin.getWindowManager().get(event.getInventory());
                this.plugin.getUserManager().getUser(event.getWhoClicked()).ifPresent(user -> {
                    if (!window.onClick(user, event.getClick(), event.getSlot(), event.getCurrentItem(), event)) {
                        event.setCancelled(true);
                    }
                });
            }
        }
    }
}