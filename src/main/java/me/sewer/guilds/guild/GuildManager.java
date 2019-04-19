package me.sewer.guilds.guild;

import me.sewer.guilds.region.Region;
import org.bukkit.Location;

import java.util.*;
import java.util.stream.Collectors;

public class GuildManager {

    private final Map<String, Guild> byTag = new HashMap<>();
    private final Map<String, Guild> byName = new HashMap<>();

    public void registerGuild(Guild guild) {
        GuildRender render = guild.getRender();
        this.byTag.put(render.getTag(), guild);
        this.byName.put(render.getName(), guild);
    }

    public void unregisterGuild(Guild guild) {
        GuildRender render = guild.getRender();
        this.byTag.remove(render.getTag());
        this.byName.remove(render.getName());
    }

    public Optional<Guild> getGuild(String string) {
        if (this.byTag(string).isPresent()) {
            return this.byTag(string);
        } else if (this.byName(string).isPresent()) {
            return this.byTag(string);
        } else {
            return null;
        }
    }

    public List<Guild> getGuild(Location location) {
        return this.byTag.values()
                .stream()
                .filter(guild -> guild.getTerrain().getRegion().contains(location))
                .collect(Collectors.toList());
    }

    public Optional<Guild> getGuild(Region region) {
        return this.byTag.values()
                .stream()
                .filter(guild -> guild.getTerrain().getRegion().equals(region))
                .findFirst();
    }

    private Optional<Guild> byTag(String tag) {
        return Optional.ofNullable(this.byTag.get(tag));
    }

    private Optional<Guild> byName(String name) {
        return Optional.ofNullable(this.byName.get(name));
    }

    public Collection<Guild> getAll() {
        return this.byTag.values();
    }
}
