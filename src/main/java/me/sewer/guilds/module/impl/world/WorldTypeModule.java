package me.sewer.guilds.module.impl.world;

import me.sewer.guilds.options.CreateOptions;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "WorldTypeModule")
public class WorldTypeModule extends Module {

    private final CreateOptions options;

    public WorldTypeModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreate(GuildCreateEvent event) {
        User user = event.getCreator();
        Player player = user.getBukkit().get();
        if (!this.options.allowedWorlds().contains(player.getLocation().getWorld().getName())) {
            user.sendMessage("worldCreationBlocked");
            event.setCancelled(true);
        }
    }
}
