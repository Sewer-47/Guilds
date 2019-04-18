package me.sewer.guilds.l18n;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.user.UserManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLocaleChangeEvent;

import java.util.Locale;

public class MessageListeners implements Listener {

    private final UserManager userManager;
    private final MessageManager messageManager;

    public MessageListeners(GuildsPlugin plugin) {
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
    }

    @EventHandler
    public void onLocaleChange(PlayerLocaleChangeEvent event) {
        this.userManager.getUser(event.getPlayer()).ifPresent(user -> {
            String name = event.getLocale();
            String language = StringUtils.substring(name, 0 ,2);
            String country = StringUtils.substring(name, 3, 5);
            Locale locale = new Locale(language, country);
            if (this.messageManager.getLocaleMap().keySet().contains(locale)) {
                user.setLocale(locale);
                user.sendMessage("localeChanged");
            } else {
                user.sendMessage("unkownLocale");
            }
        });
    }
}
