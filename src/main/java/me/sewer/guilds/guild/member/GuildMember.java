package me.sewer.guilds.guild.member;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.user.User;

import java.util.ArrayList;
import java.util.List;

public class GuildMember {

    private final User user;
    private final List<GuildPermission> guildPermissions = new ArrayList<>();

    public GuildMember(User user, GuildsPlugin plugin) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public boolean hasPermission(GuildPermission guildPermission) {
        return this.guildPermissions.contains(guildPermission);
    }

    public boolean hasPermission(String permission) {
        for (GuildPermission guildPermission : this.guildPermissions) {
            if (guildPermission.getName().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean addPermission(GuildPermission guildPermission) {
        if (!this.guildPermissions.contains(guildPermission)) {
            this.guildPermissions.add(guildPermission);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePermission(GuildPermission guildPermission) {
        if (this.guildPermissions.contains(guildPermission)) {
            this.guildPermissions.remove(guildPermission);
            return true;
        } else {
            return false;
        }
    }

    public List<GuildPermission> getGuildPermissions() {
        return this.guildPermissions;
    }
}
