package me.sewer.guilds.region;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.World;

public class RegionManager {

    private final Map<UUID, RegionRegistry> registryByWorldId = new HashMap<UUID, RegionRegistry>();

    public RegionRegistry byWorldId(UUID uuid) {
        return this.registryByWorldId.get(uuid);
    }

    public void addRegionRegistry(UUID worldId, RegionRegistry regionRegistry) {
        this.registryByWorldId.put(worldId, regionRegistry);
    }

    public void removeRegionRegistry(UUID worldId) {
        this.registryByWorldId.remove(worldId);
    }

    public boolean cointains(World world) {
        return this.registryByWorldId.containsKey(world.getUID());
    }

    public Map<UUID, RegionRegistry> getRegistryByWorldId() {
        return this.registryByWorldId;
    }
}

