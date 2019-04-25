package me.sewer.guilds.module.modules;

import me.sewer.guilds.command.commands.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "CreationBanModule")
public class CreationBanModule extends Module {

    private final CreateOptions options;

    public CreationBanModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        if (!this.options.creatingEnabled()) {
            Guild guild = event.getGuild();
            User user = guild.getMemebers().getOwner();
            user.sendMessage("guildCreatingIsDisabled");
            event.setCancelled(true);
        }
    }
}
