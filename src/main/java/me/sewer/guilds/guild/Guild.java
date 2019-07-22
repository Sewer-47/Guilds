package me.sewer.guilds.guild;

import me.sewer.guilds.Unique;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.guild.member.PermissionsWindow;
import me.sewer.guilds.validity.Validity;

import java.util.UUID;

public class Guild implements Unique {

    private final UUID uniqueId;
    private final GuildRender render;
    private final GuildMembers members;
    private final GuildTerrain terrain;
    private final GuildRelations relations;
    private final GuildHeart heart;
    private final GuildSafe safe;
    private final PermissionsWindow permission;
    private final Validity validity;

    public Guild(UUID uniqueId, GuildRender render, GuildMembers members, GuildTerrain terrain, GuildRelations relations, GuildHeart heart, GuildSafe safe, PermissionsWindow permissions, Validity validity) {
        this.uniqueId = uniqueId;
        this.render = render;
        this.members = members;
        this.terrain = terrain;
        this.relations = relations;
        this.heart = heart;
        this.safe = safe;
        this.permission = permissions;
        this.validity = validity;
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
        return heart;
    }

    public GuildSafe getSafe() {
        return safe;
    }

    public PermissionsWindow getPermission() {
        return this.permission;
    }

    public Validity getValidity() {
        return validity;
    }
}
