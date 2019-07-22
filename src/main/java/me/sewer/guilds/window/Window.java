package me.sewer.guilds.window;

import java.util.List;


import me.sewer.guilds.user.User;
import me.sewer.guilds.window.listener.SimpleWindowListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Window extends SimpleWindowListener {
    private final Inventory inventory;

    public Window(int size, String name) {
        this(size, name, null);
    }

    public Window(int size, String name, List<ItemStack> items) {
        Inventory inventory = Bukkit.createInventory(null, size, name);
        if (items != null) {
            for (ItemStack item : items) {
                inventory.addItem(item);
            }
        }
        this.inventory = inventory;
    }

    public Window(Inventory inventory) {
        this.inventory = inventory;
    }

    public void open(User user) {
        this.onOpen(user);
        user.getBukkit().ifPresent(player -> player.openInventory(this.inventory));
    }

    public void close(User user) {
        this.onClose(user);
        user.getBukkit().ifPresent(player -> player.closeInventory());;
    }

    public void refresh(User user) {
        this.close(user);
        this.open(user);
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
