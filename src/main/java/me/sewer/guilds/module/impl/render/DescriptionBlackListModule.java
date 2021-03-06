package me.sewer.guilds.module.impl.render;

import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.List;

@ModuleInfo(name = "DescriptionBlackListModule")
public class DescriptionBlackListModule extends Module implements WordBlackList {

    private final List<String> blackList;

    public DescriptionBlackListModule(List<String> blackList) {
        this.blackList = blackList;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onCreate(GuildCreateEvent event) {
        Guild guild = event.getGuild();
        if (this.contains(guild.getRender().getDescription(), this.blackList)) {
            event.getCreator().sendMessage("illegalDescription");
            event.setCancelled(true);
        }
    }
}
