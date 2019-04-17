package me.sewer.guilds.user;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListeners implements Listener {

    private final UserManager userManager;
    private final GuildsPlugin plugin;

    public UserListeners(GuildsPlugin plugin) {
        this.userManager = plugin.getUserManager();
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = new User(player, this.plugin);
        this.userManager.registerUser(user);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = this.userManager.getUser(player).get();
        this.userManager.unregisterUser(user);
    }
}
