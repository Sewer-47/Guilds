package me.sewer.guilds.module.impl.world;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "SpawnDistanceModule")
public class SpawnDistanceModule extends Module {

    private final CreateOptions options;

    public SpawnDistanceModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        User user = event.getCreator();
        user.getBukkit().ifPresent(player -> {
            Location location = player.getLocation();
            if (location.distance(location.getWorld().getSpawnLocation()) <= this.options.spawnDistance()) {
                user.sendMessage("tooNearSpawn");
                event.setCancelled(true);
            }
        });
    }
}
