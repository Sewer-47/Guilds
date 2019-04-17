package me.sewer.guilds.guild;

import me.sewer.guilds.Unique;

import java.util.UUID;

public class Guild implements Unique {

    private final UUID uniqueId;
    private final GuildRender render;
    private final GuildMemebers memebers;
    private final GuildTerrain terrain;
    private final GuildRelations relations;

    public Guild(UUID uniqueId, GuildRender render, GuildMemebers memebers, GuildTerrain terrain, GuildRelations relations) {
        this.uniqueId = uniqueId;
        this.render = render;
        this.memebers = memebers;
        this.terrain = terrain;
        this.relations = relations;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public GuildRender getRender() {
        return this.render;
    }

    public GuildMemebers getMemebers() {
        return this.memebers;
    }

    public GuildTerrain getTerrain() {
        return this.terrain;
    }

    public GuildRelations getRelations() {
        return this.relations;
    }
}
