package me.sewer.guilds.user;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.l18n.MessageManager;
import me.sewer.guilds.l18n.MessageMap;
import me.sewer.guilds.util.ChatUtil;
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

    public User(Player bukkit, GuildsPlugin plugin) {
        this.name = bukkit.getName();
        this.uniqueId = bukkit.getUniqueId();
        this.bukkit = new WeakReference<>(bukkit);
        this.messageManager = plugin.getMessageManager();
        this.locale = plugin.getMessageManager().getFallback(); //use bukkit.getLocale()

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

    public void sendMessage(String message, Object... params) {
        String text;
        Optional<MessageMap> messageMap = this.messageManager.getMessage(this.locale);
        if (messageMap.isPresent()) {
            text = messageMap.get().get(message);
        } else {
            text = this.messageManager.getMessage(this.messageManager.getFallback()).get().get(message);
        }
        this.bukkit.get().sendMessage(ChatUtil.color(MessageFormat.format(text, params)));
    }
}
