package me.sewer.guilds.user;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.elo.Elo;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.i18n.MessageManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public class User implements UserProfile {

    private final String name;
    private final UUID uniqueId;
    private final Reference<Player> bukkit;
    private final MessageManager messageManager;
    private Guild guild;
    private Locale locale;
    private final Elo elo;

    public User(Player bukkit, GuildsPlugin plugin) {
        this.name = bukkit.getName();
        this.uniqueId = bukkit.getUniqueId();
        this.bukkit = new WeakReference<>(bukkit);
        this.messageManager = plugin.getMessageManager();

        String name = bukkit.getLocale();
        String language = StringUtils.substring(name, 0 ,2);
        String country = StringUtils.substring(name, 3, 5);
        Locale locale = new Locale(language, country);
        if (this.messageManager.getLocaleMap().containsKey(locale)) {
            this.locale = locale;
        } else {
            this.locale = plugin.getMessageManager().getFallback();
        }
        this.elo = new Elo(plugin.getConfig().getInt("startPoints"));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Optional<Guild> getGuild() {
        return Optional.ofNullable(this.guild);
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Reference<Player> getBukkit() {
        return bukkit;
    }

    public Elo getElo() {
        return elo;
    }

    public void sendMessage(String message, Object... params) {
        this.bukkit.get().sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.messageManager.getMessage(this.locale, message), params)));
    }
}
