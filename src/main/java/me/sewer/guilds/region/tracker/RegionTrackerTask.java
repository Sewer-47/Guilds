package me.sewer.guilds.region.tracker;

import java.util.Collection;
import java.util.UUID;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.region.RegionRegistry;
import me.sewer.guilds.region.event.AsyncPlayerRegionEnterEvent;
import me.sewer.guilds.region.event.AsyncPlayerRegionQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RegionTrackerTask implements Runnable, RegionTrackerListener {

    private final GuildsPlugin plugin;

    public RegionTrackerTask(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
           RegionRegistry regionRegistry = this.plugin.getRegionManager().getRegistryByWorldId().get(player.getLocation().getWorld().getUID());
            if (regionRegistry != null) {
                for (Region region : regionRegistry.getRegions()) {
                    //for (Region region : regionRegistry.getRegion(player.getLocation())) { petla sie nie wykona bo nie ma gracza w regionie
                   this.tickRegion(region, player);
                }
           }
        }
    }

    private void tickRegion(Region region, Player player) {
        UUID playerId = player.getUniqueId();
        if (region.contains(player) && !region.getPlayersUIDs().contains(playerId)) {
            this.onEnter(region, player);
        }
        if (!region.getPlayersUIDs().contains(playerId) || region.contains(player)) return;
        this.onQuit(region, player);
    }

    @Override
    public void onEnter(Region region, Player player) {
        UUID playerId = player.getUniqueId();
        region.getPlayersUIDs().add(playerId);
        Bukkit.getServer().getPluginManager().callEvent(new AsyncPlayerRegionEnterEvent(player, region));
    }

    @Override
    public void onQuit(Region region, Player player) {
        UUID playerId = player.getUniqueId();
        region.getPlayersUIDs().remove(playerId);
        Bukkit.getServer().getPluginManager().callEvent(new AsyncPlayerRegionQuitEvent(player, region));
    }
}

