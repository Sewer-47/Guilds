package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.member.GuildMember;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.window.Window;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuildSafe extends Window {

    private final GuildMembers members;
    private final MessageManager messageManager;

    public GuildSafe(GuildMembers members, int size, String name, GuildsPlugin plugin) {
        super(size, name);
        this.members = members;
        this.messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onClick(User user, ClickType click, int slot, ItemStack item, InventoryClickEvent event) {
        GuildMember member = this.members.getMember(user.getUniqueId());
        if (member != null) {
            if (!member.hasPermission(GuildPermission.MANAGE_SAFE)) {
                String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.MANAGE_SAFE.getDisplayName());
                user.sendMessage("noPerm", perm);
                return false;
            }
        }
        return true;
    }

    @Override
    public void open(User user) {
        GuildMember member = this.members.getMember(user.getUniqueId());
        if (member != null) {
            if (member.hasPermission(GuildPermission.OPEN_SAFE)) {
                super.open(user);
            } else {
                String perm = this.messageManager.getMessage(user.getLocale(), GuildPermission.OPEN_SAFE.getDisplayName());
                user.sendMessage("noPerm", perm);
            }
        }
    }
}