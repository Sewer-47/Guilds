package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.event.GuildRegionEnterEvent;
import me.sewer.guilds.guild.event.GuildRegionQuitEvent;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
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
        player.sendMessage("wszedles na teren gildii " + event.getGuild().getRender().getTag());
    }

    @EventHandler
    public void onGuildRegionQuit(GuildRegionQuitEvent event) {
        Player player = event.getWho();
        player.sendMessage("wyszedles z terenu gildii " + event.getGuild().getRender().getTag());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        this.userManager.getUser(player).ifPresent(user -> {
            this.guildManager.getGuild(event.getBlock().getLocation()).forEach(guild -> {
                if (!guild.getMemebers().getMembers().contains(user)) {
                    this.cancel(user, event, "&7Nie mozesz niszczyc blokow na terenie cudzej gildii");
                    return;
                }
            });
        });
    }


    private void cancel(User user, Cancellable cancellable, String message) {
        cancellable.setCancelled(true);
        user.sendMessage(ChatUtil.color(message));
    }
}
