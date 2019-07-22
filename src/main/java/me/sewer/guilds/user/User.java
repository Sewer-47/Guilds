package me.sewer.guilds.user;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.elo.Elo;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.request.Request;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    private Optional<Request> lastRequest = Optional.empty();

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

    public boolean acceptLastRequest() {
        if (!this.lastRequest.isPresent()) {
            return false;
        }

        Request request = this.lastRequest.get();
        request.accept();

        this.lastRequest = Optional.empty();
        return true;
    }

    public boolean declineLastRequest() {
        if (!this.lastRequest.isPresent()) {
            return false;
        }

        Request request = this.lastRequest.get();
        request.deny();

        this.lastRequest = Optional.empty();
        return true;
    }

    public Request getLastRequest() {
        return !this.lastRequest.isPresent() ? this.lastRequest.get() : null;
    }

    public void request(Request request) {
        this.lastRequest = Optional.of(new LastRequest(request, Instant.now().plus(5, ChronoUnit.MINUTES)).get());
    }

    public void request(Request request, Instant timeout) {
        this.lastRequest = Optional.of(new LastRequest(request, timeout).get());
    }

    private class LastRequest {
        final Request request;
        final Instant timeout;

        LastRequest(Request request, Instant timeout) {
            this.request = request;
            this.timeout = timeout;
        }

        Request get() {
            return Instant.now().isBefore(this.timeout) ? this.request : null;
        }
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

    public Optional<Player> getBukkit() {
        return Optional.ofNullable(this.bukkit.get());
    }

    public Elo getElo() {
        return elo;
    }

    public void sendMessage(String message, Object... params) {
        this.bukkit.get().sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.messageManager.getMessage(this.locale, message), params)));
    }
}
