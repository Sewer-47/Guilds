package me.sewer.guilds.guild;

import me.sewer.guilds.Messageable;
import me.sewer.guilds.Unique;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.PermissionsWindow;
import me.sewer.guilds.validity.Validity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.UUID;

public class Guild implements Unique, Messageable {

    private final UUID uniqueId;
    private final GuildRender render;
    private final GuildMembers members;
    private final GuildTerrain terrain;
    private final GuildRelations relations;
    private final GuildHeart heart;
    private final GuildSafe safe;
    private final PermissionsWindow permissionWindow;
    private final Validity validity;

    public Guild(UUID uniqueId, GuildRender render, GuildMembers members, GuildTerrain terrain, GuildRelations relations, GuildHeart heart, GuildSafe safe, PermissionsWindow permissionsWindow, Validity validity) {
        this.uniqueId = uniqueId;
        this.render = render;
        this.members = members;
        this.terrain = terrain;
        this.relations = relations;
        this.heart = heart;
        this.safe = safe;
        this.permissionWindow = permissionsWindow;
        this.validity = validity;
    }

    @Override
    public void send(ChatMessageType position, BaseComponent message) {
        for (UUID playerId : this.getMembers().getAll()) {
            Player player = Bukkit.getPlayer(playerId);
            player.spigot().sendMessage(position, message);
        }
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public GuildRender getRender() {
        return this.render;
    }

    public GuildMembers getMembers() {
        return this.members;
    }

    public GuildTerrain getTerrain() {
        return this.terrain;
    }

    public GuildRelations getRelations() {
        return this.relations;
    }

    public GuildHeart getHeart() {
        return this.heart;
    }

    public GuildSafe getSafe() {
        return this.safe;
    }

    public PermissionsWindow getPermissionWindow() {
        return this.permissionWindow;
    }

    public Validity getValidity() {
        return this.validity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Guild guild = (Guild) o;
        return Objects.equals(uniqueId, guild.uniqueId) &&
                Objects.equals(render, guild.render) &&
                Objects.equals(members, guild.members) &&
                Objects.equals(terrain, guild.terrain) &&
                Objects.equals(relations, guild.relations) &&
                Objects.equals(heart, guild.heart) &&
                Objects.equals(safe, guild.safe) &&
                Objects.equals(permissionWindow, guild.permissionWindow) &&
                Objects.equals(validity, guild.validity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId, render, members, terrain, relations, heart, safe, permissionWindow, validity);
    }
}
