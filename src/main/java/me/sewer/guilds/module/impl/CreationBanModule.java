package me.sewer.guilds.module.impl;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "CreationBanModule")
public class CreationBanModule extends Module {

    private final CreateOptions options;
    private final UserManager userManager;

    public CreationBanModule(CreateOptions options, UserManager userManager) {
        this.options = options;
        this.userManager = userManager;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        if (!this.options.creatingEnabled()) {
            event.getWho().sendMessage("guildCreatingIsDisabled");
            event.setCancelled(true);
        }
    }
}
