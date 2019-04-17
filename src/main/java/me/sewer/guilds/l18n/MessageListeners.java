package me.sewer.guilds.l18n;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.user.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

public class MessageListeners implements Listener {

    private final UserManager userManager;

    public MessageListeners(GuildsPlugin plugin) {
        this(plugin.getUserManager());
    }

    public MessageListeners(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent event) {
        this.userManager.getUser(event.getPlayer()).ifPresent(user -> {

        });
    }
}
