package me.sewer.guilds.window.listener;


import me.sewer.guilds.user.User;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface WindowListener {

    boolean onClick(User user, ClickType clickType, int slot, ItemStack item, InventoryClickEvent event);

    void onClose(User user);

    void onOpen(User user);
}

