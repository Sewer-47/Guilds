package me.sewer.guilds.module.impl;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

@ModuleInfo(name = "TagLengthModule")
public class TagLengthModule extends Module {

    private final CreateOptions options;

    public TagLengthModule(CreateOptions options) {
        this.options = options;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();
        GuildRender render = guild.getRender();
        User user = event.getWho();
        String tag = render.getTag();
        if (tag.length() < this.options.tagMinLength() || tag.length() > this.options.tagMaxLength()) {
            user.sendMessage("correctTagLength", this.options.tagMinLength(), this.options.tagMaxLength());
            event.setCancelled(true);
        }
    }
}
