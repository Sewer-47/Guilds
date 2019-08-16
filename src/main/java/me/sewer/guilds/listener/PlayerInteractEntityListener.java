package me.sewer.guilds.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.validity.Validity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerInteractEntityListener implements Listener {

    private final UserManager userManager;
    private final MessageManager messageManager;
    private final Cache<UUID, Validity> cooldown;

    public PlayerInteractEntityListener(GuildsPlugin plugin) {
       this.userManager = plugin.getUserManager();
       this.messageManager = plugin.getMessageManager();
       this.cooldown = CacheBuilder.newBuilder()
               .expireAfterWrite(1, TimeUnit.SECONDS)
               .build();
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        if (event.getRightClicked() instanceof Player) {
            Player target = (Player) event.getRightClicked();
            Validity validity = this.cooldown.getIfPresent(playerId);
            if (validity != null && !validity.hasExpired()) {
                return;
            } else {
                this.cooldown.put(playerId, new Validity(LocalDateTime.now().plusSeconds(1)));
            }
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
                    user.sendMessage("playerInfo", target.getDisplayName(), targetUser.getElo().getPoints(), tag, name);
                });
            });
        }
    }
}