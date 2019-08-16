package me.sewer.guilds.command.impl.items;

import me.sewer.guilds.user.User;
import me.sewer.guilds.window.Window;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemsWindow extends Window {

    public ItemsWindow(int size, String name, List<ItemStack> items) {
        super(size, name, items);
    }

    @Override
    public boolean onClick(User user, ClickType click, int slot, ItemStack item, InventoryClickEvent event) {
        return false;
    }
}
