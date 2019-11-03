package me.sewer.guilds.module.impl;

import me.sewer.guilds.options.CreateOptions;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "CreationBanModule")
public class CreationBanModule extends Module {

    private final CreateOptions options;

    public CreationBanModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreate(GuildCreateEvent event) {
        if (!this.options.creatingEnabled()) {
            event.getCreator().sendMessage("guildCreatingIsDisabled");
            event.setCancelled(true);
        }
    }
}
