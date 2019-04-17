package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.region.event.PlayerRegionEnterEvent;
import me.sewer.guilds.region.event.PlayerRegionQuitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GuildListeners implements Listener {

    private final GuildManager guildManager;

    public GuildListeners(GuildsPlugin plugin) {
        this(plugin.getGuildManager());
    }

    public GuildListeners(GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    @EventHandler
    public void onRegionEnter(PlayerRegionEnterEvent event) {
        Region region = event.getRegion();
        if (this.guildManager.getGuild(region).isPresent()) {
            Event guildRegionEnterEvent = new GuildRegionEnterEvent(this.guildManager.getGuild(region).get(), event.getPlayer());
            Bukkit.getPluginManager().callEvent(guildRegionEnterEvent);
        }
    }

    @EventHandler
    public void onRegionQuit(PlayerRegionQuitEvent event) {
        Region region = event.getRegion();
        if (this.guildManager.getGuild(event.getRegion()).isPresent()) {
            Event guildRegionQuitEvent = new GuildRegionQuitEvent(this.guildManager.getGuild(region).get(), event.getPlayer());
            Bukkit.getPluginManager().callEvent(guildRegionQuitEvent);
        }
    }
}
