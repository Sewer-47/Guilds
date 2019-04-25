package me.sewer.guilds.module.modules;

import me.sewer.guilds.command.commands.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "SpawnModule")
public class SpawnModule extends Module {

    private final CreateOptions options;

    public SpawnModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();
        User user = guild.getMemebers().getOwner();
        Player player = user.getBukkit().get();
        Location location = player.getLocation();
        if (location.distance(location.getWorld().getSpawnLocation()) <= this.options.spawnDistance()) {
            user.sendMessage("tooNearSpawn");
            event.setCancelled(true);
        }
    }
}
