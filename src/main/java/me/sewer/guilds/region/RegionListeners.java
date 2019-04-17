package me.sewer.guilds.region;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.UUID;

public class RegionListeners implements Listener {

    private final RegionManager regionManager;

    public RegionListeners(GuildsPlugin plugin) {
        this(plugin.getRegionManager());
    }

    public RegionListeners(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        UUID worldUniqueId = event.getWorld().getUID();
        if (!this.regionManager.getRegistryByWorldId().keySet().contains(worldUniqueId)) {
            this.regionManager.addRegionRegistry(worldUniqueId, new RegionRegistry(worldUniqueId));
        }
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        UUID worldUniqueId = event.getWorld().getUID();
        if (this.regionManager.getRegistryByWorldId().keySet().contains(worldUniqueId)) {
            this.regionManager.removeRegionRegistry(worldUniqueId);
        }
    }
}
