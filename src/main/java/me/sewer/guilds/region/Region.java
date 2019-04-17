package me.sewer.guilds.region;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;

public abstract class Region implements IRegion {

    private final Set<Player> players = new HashSet<>();
    private final UUID uuid;
    private final String name;

    protected Region(String name) {
        this.name = name;
        Random random = new Random();
        this.uuid = new UUID(random.nextLong(), random.nextLong());
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Set<Player> getPlayers() {
        return this.players;
    }
}

