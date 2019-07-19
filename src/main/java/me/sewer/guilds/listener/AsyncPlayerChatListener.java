package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.i18n.MessageManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsyncPlayerChatListener implements Listener {

    private final GuildsPlugin plugin;
    private final Map<String, String> messages;
    private final MessageManager messageManager;

    public AsyncPlayerChatListener(GuildsPlugin plugin, Map<String, String> messages) {
        this.plugin = plugin;
        this.messages = new ConcurrentHashMap<>(messages); //Must be async
        this.messageManager = plugin.getMessageManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        this.plugin.getUserManager().getUser(player).ifPresent(user -> {
            Locale locale = user.getLocale();
            String tag;
            String name;
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                GuildRender render = guild.getRender();
                tag = this.messageManager.getMessage(locale, "guildTag", render.getTag());
                name = this.messageManager.getMessage(locale, "guildName", render.getName());
            } else {
                String lack = this.messageManager.getMessage(locale, "lack");
                tag = this.messageManager.getMessage(locale, "guildTag", lack);
                name = this.messageManager.getMessage(locale, "guildName", lack);
            }
                String replaced = message
                        .replace(this.messages.get("rank"), user.getElo().getPoints() + "")
                        .replace(this.messages.get("guildTag"), tag)
                        .replace(this.messages.get("guildName"), name);
                //Guild rank
                //Guild lives
            event.setMessage(ChatColor.translateAlternateColorCodes('&', replaced));
        });
    }
}