package me.sewer.guilds.module.impl;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "WorldModule")
public class WorldModule extends Module {

    private final CreateOptions options;

    public WorldModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();
        User user = guild.getMemebers().getOwner();
        Player player = user.getBukkit().get();
        if (!this.options.allowedWorlds().contains(player.getLocation().getWorld().getName())) {
            user.sendMessage("worldCreationBlocked");
            event.setCancelled(true);
        }
    }
}
