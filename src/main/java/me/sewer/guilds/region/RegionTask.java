package me.sewer.guilds.region;

import java.util.Collection;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.region.event.PlayerRegionEnterEvent;
import me.sewer.guilds.region.event.PlayerRegionQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionTask extends BukkitRunnable {

    private final GuildsPlugin plugin;

    public RegionTask(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Collection<RegionRegistry> regionRegistries = this.plugin.getRegionManager().getRegistryByWorldId().values();
        for (RegionRegistry regionRegistry : regionRegistries) {
            for (Region region : regionRegistry.getRegions()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (region.contains(player) && !region.getPlayers().contains(player)) {
                        region.getPlayers().add(player);
                        Bukkit.getServer().getPluginManager().callEvent(new PlayerRegionEnterEvent(player, region));
                    }
                    if (!region.getPlayers().contains(player) || region.contains(player)) continue;
                    region.getPlayers().remove(player);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerRegionQuitEvent(player, region));
                }
            }
        }
    }
}

