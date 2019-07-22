package me.sewer.guilds.guild.member;

import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.window.Window;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PermissionsWindow extends Window {

    private final GuildMembers guildMembers;
    private final MessageManager messageManager;

    public PermissionsWindow(String name, GuildMembers guildMembers, MessageManager messageManager) {
        super(27, name);
        this.guildMembers = guildMembers;
        this.messageManager = messageManager;
    }

    @Override
    public boolean onClick(User user, ClickType click, int slot, ItemStack item, InventoryClickEvent event) {
        return false;
    }

    @Override
    public void onClose(User user) {
        this.getInventory().clear();
    }

    @Override
    public void onOpen(User user) {
        Locale locale = user.getLocale();
        ItemStack itemStack = new ItemStack(Material.LEGACY_SKULL_ITEM, 1, (short) 3);
        SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
        List<String> lore = new ArrayList<>();
        int i=0;
        for (UUID uuid : this.guildMembers.getAll()) {
            Player player = Bukkit.getPlayer(uuid);
            itemMeta.setDisplayName(player.getDisplayName());
            itemMeta.setOwningPlayer(player);
            GuildMember guildMember = this.guildMembers.getMember(uuid);
            lore.add(ChatColor.translateAlternateColorCodes('&', this.messageManager.getMessage(locale, "permissions")));
            for (GuildPermission guildPermission : guildMember.getGuildPermissions()) {
                lore.add(ChatColor.translateAlternateColorCodes('&', " - " + this.messageManager.getMessage(locale, guildPermission.getDisplayName())));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            lore.clear();
            this.getInventory().setItem(i, itemStack);
            i++;
        }
    }
}
