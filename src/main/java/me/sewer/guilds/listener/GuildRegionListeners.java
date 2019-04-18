package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class GuildRegionListeners implements Listener {

    private final GuildManager guildManager;
    private final UserManager userManager;

    public GuildRegionListeners(GuildsPlugin plugin) {
        this.guildManager = plugin.getGuildManager();
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    public void onGuildRegionEnter(GuildRegionEnterEvent event) {
        Player player = event.getWho();
        this.userManager.getUser(player).ifPresent(user -> user.sendMessage("guildRegionEnter", event.getGuild().getRender().getTag()));
    }

    @EventHandler
    public void onGuildRegionQuit(GuildRegionQuitEvent event) {
        Player player = event.getWho();
        this.userManager.getUser(player).ifPresent(user -> user.sendMessage("guildRegionQuit", event.getGuild().getRender().getTag()));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlock().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getMembers().contains(user)) {
                    event.setCancelled(true);
                    user.sendMessage("cantBlockBreak");
                    return;
                }
            });
        });
    }
}
