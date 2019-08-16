package me.sewer.guilds.module.impl.world;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.util.Vector;

@ModuleInfo(name = "OtherGuildDistanceModule")
public class OtherGuildDistanceModule extends Module {

    private final CreateOptions options;

    public OtherGuildDistanceModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        User user = event.getCreator();
        Player player = user.getBukkit().get();
        Location location = player.getLocation();
        this.getPlugin().getGuildManager().getAll().forEach(guild -> {
            Vector home = guild.getTerrain().getHome();
            if (home.distance(location.toVector()) <= this.options.guildDistance()) {
                user.sendMessage("tooNearOtherGuild");
                event.setCancelled(true);
            }
        });
    }
}
