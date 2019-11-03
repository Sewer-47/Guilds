package me.sewer.guilds.region;

import java.util.HashSet;
import java.util.Random;

import java.util.Set;
import java.util.UUID;

public abstract class Region implements IRegion {

    private final Set<UUID> playersUIDs = new HashSet<>();
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

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    public Set<UUID> getPlayersUIDs() {
        return this.playersUIDs;
    }
}