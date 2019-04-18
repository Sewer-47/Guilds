package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.l18n.MessageManager;
import me.sewer.guilds.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Locale;

public class PlayerInteractEntityListener implements Listener {

    private final UserManager userManager;
    private final MessageManager messageManager;

    public PlayerInteractEntityListener(GuildsPlugin plugin) {
       this.userManager = plugin.getUserManager();
       this.messageManager = plugin.getMessageManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            this.userManager.getUser(player).ifPresent(user -> {
                this.userManager.getUser(target).ifPresent(targetUser -> {
                    Locale locale = user.getLocale();
                    String tag;
                    String name;
                    if (targetUser.getGuild().isPresent()) {
                        Guild guild = targetUser.getGuild().get();
                        GuildRender render = guild.getRender();
                        tag = this.messageManager.getMessage(locale, "guildTag", render.getTag());
                        name = this.messageManager.getMessage(locale, "guildName", render.getName());
                    } else {
                        String lack = this.messageManager.getMessage(locale, "lack");
                        tag = this.messageManager.getMessage(locale, "guildTag", lack);
                        name = this.messageManager.getMessage(locale, "guildName", lack);
                    }
                    user.sendMessage("playerInfo", target.getDisplayName(), targetUser.getPoints(), tag, name);
                });
            });
        }
    }
}