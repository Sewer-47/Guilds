package me.sewer.guilds.module.impl;

import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

@ModuleInfo(name = "PrivateChatModule")
public class PrivateChatModule extends Module {

    private final UserManager userManager;
    private final String prefix;
    private final String format;

    public PrivateChatModule(UserManager userManager, String prefix, String format) {
        this.userManager = userManager;
        this.prefix = prefix;
        this.format = format;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true )
    public void onChat(AsyncPlayerChatEvent event) {
        Optional<User> user = this.userManager.getUser(event.getPlayer());
        if (event.getMessage().startsWith(this.prefix) && user.isPresent() && user.get().getGuild().isPresent()) {
            Guild guild = user.get().getGuild().get();
            event.getRecipients().clear();
            guild.getMemebers().getAll().forEach(member -> {
                Player player = member.getBukkit().get();
                if (player != null) {
                    event.getRecipients().add(player);
                }
            });
            String message = event.getMessage().substring(1);
            event.setFormat(ChatColor.translateAlternateColorCodes('&',
                    format.replace("{0}", user.get().getName())
                    .replace("{1}", message)));
        }
    }
}
