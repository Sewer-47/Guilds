package me.sewer.guilds.region;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class RegionRegistry {

    private final UUID worldId;
    private final Set<Region> regions = new HashSet<>();

    public RegionRegistry(UUID worldId) {
        this.worldId = worldId;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public UUID getWorldId() {
        return this.worldId;
    }

    public Optional<Region> getRegion(String name) {
        return this.regions.stream().filter(abstractRegion -> abstractRegion.getName().equals(name)).findFirst();
    }

    public List<Region> getRegion(Location location) {
        return this.getRegion(location.toVector());
    }

    public List<Region> getRegion(Vector vector) {
        return this.regions.stream().filter(abstractRegion -> abstractRegion.contains(vector)).collect(Collectors.toList());
    }

    public void add(Region region) {
        this.regions.add(region);
    }

    public void remove(Region region) {
        this.regions.remove(region);
    }
}

