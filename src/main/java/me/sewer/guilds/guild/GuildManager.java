package me.sewer.guilds.guild;

import me.sewer.guilds.region.Region;
import org.bukkit.Location;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GuildManager {

    private final Map<String, Guild> byTag = new ConcurrentHashMap<>(); //Async
    private final Map<String, Guild> byName = new ConcurrentHashMap<>();
    private final Map<UUID, Guild> byUniqueId = new ConcurrentHashMap<>();

    public void registerGuild(Guild guild) {
        GuildRender render = guild.getRender();
        this.byTag.put(render.getTag(), guild);
        this.byName.put(render.getName(), guild);
        this.byUniqueId.put(guild.getUniqueId(), guild);
    }

    public void unregisterGuild(Guild guild) {
        GuildRender render = guild.getRender();
        this.byTag.remove(render.getTag());
        this.byName.remove(render.getName());
        this.byUniqueId.remove(guild.getUniqueId());
    }

    public Optional<Guild> getGuild(String string) {
        if (this.byTag(string).isPresent()) {
            return this.byTag(string);
        } else if (this.byName(string).isPresent()) {
            return this.byName(string);
        } else {
            return Optional.empty();
        }
    }

    public List<Guild> getGuild(Location location) {
        List<Guild> guilds = new ArrayList<>();
        for (Guild guild : this.byTag.values()) {
            if (guild.getTerrain().getRegion().contains(location)) {
                guilds.add(guild);
            }
        }
        return guilds;
    }

    public Optional<Guild> getGuild(Region region) {
        for (Guild guild : this.byTag.values()) {
            if (guild.getTerrain().getRegion().equals(region)) {
                return Optional.of(guild);
            }
        }
        return Optional.empty();
    }

    public Optional<Guild> getGuild(UUID uniqueId) {
        return this.byUniqueId(uniqueId);
    }

    public Optional<Guild> byTag(String tag) {
        return Optional.ofNullable(this.byTag.get(tag));
    }

    public Optional<Guild> byName(String name) {
        return Optional.ofNullable(this.byName.get(name));
    }

    public Optional<Guild> byUniqueId(UUID uniqueId) {
        return Optional.ofNullable(this.byUniqueId.get(uniqueId));
    }

    public Collection<Guild> getAll() {
        return this.byTag.values();
    }
}
