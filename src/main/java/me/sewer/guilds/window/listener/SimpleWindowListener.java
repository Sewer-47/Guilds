package me.sewer.guilds.window.listener;

import me.sewer.guilds.user.User;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SimpleWindowListener implements WindowListener {

    @Override
    public boolean onClick(User user, ClickType click, int slot, ItemStack item, InventoryClickEvent event) {
        return false;
    }

    @Override
    public void onClose(User user) {
    }

    @Override
    public void onOpen(User user) {
    }
}
