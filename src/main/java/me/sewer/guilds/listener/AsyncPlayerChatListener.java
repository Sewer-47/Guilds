package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {

    private final GuildsPlugin plugin;

    public AsyncPlayerChatListener(GuildsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        User user = this.plugin.getUserManager().getUser(player).get();
        if (user.getGuild().isPresent()) {
            Guild guild = user.getGuild().get();
            event.setMessage(message.replace("{@TAG}", guild.getRender().getTag()));
        }
    }
}
