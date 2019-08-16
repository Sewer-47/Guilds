package me.sewer.guilds.module.impl.render;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "NameLengthModule")
public class NameLengthModule extends Module {

    private final CreateOptions options;

    public NameLengthModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();
        GuildRender render = guild.getRender();
        String name = render.getName();
        if (name.length() < this.options.tagMinLength() || name.length() > this.options.tagMaxLength()) {
            event.getCreator().sendMessage("correctNameLength", this.options.tagMinLength(), this.options.tagMaxLength());
            event.setCancelled(true);
        }
    }
}