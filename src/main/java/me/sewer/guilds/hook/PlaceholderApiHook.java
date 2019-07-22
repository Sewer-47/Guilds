package me.sewer.guilds.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.ToStringFunction;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderApiHook extends PlaceholderExpansion {

    private final GuildsPlugin plugin;
    private final UserManager userManager;
    private final PluginDescriptionFile description;
    private final Map<String, ToStringFunction<User>> placeholders;
    private final MessageManager messageManager;

    public PlaceholderApiHook(GuildsPlugin plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
        this.description = plugin.getDescription();
        this.placeholders = new HashMap<>();
        this.messageManager = plugin.getMessageManager();

        this.placeholders.put("rank", user -> user.getElo().getPoints() + "");
        this.placeholders.put("tag", user -> {
            String lack = this.messageManager.getMessage(user.getLocale(), "lack");
            if (user.getGuild().isPresent()) {
                return user.getGuild().get().getRender().getTag();
            }
            return lack;
        });
        this.placeholders.put("name", user -> {
            String lack = this.messageManager.getMessage(user.getLocale(), "lack");
            if (user.getGuild().isPresent()) {
                return user.getGuild().get().getRender().getName();
            }
            return lack;
        });
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null || !this.userManager.getUser(player).isPresent()) {
            return null;
        }

        User user = this.userManager.getUser(player).get();

        ToStringFunction<User> function = this.placeholders.get(identifier);
        return function != null ? function.applyAsString(user) : null;
    }

    @Override
    public String getIdentifier() {
        return this.description.getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        StringBuilder sb = new StringBuilder();
        this.description.getAuthors().forEach(author -> {
            sb.append(author + ", ");
        });
        return sb.toString();
    }

    @Override
    public String getVersion() {
        return this.description.getVersion();
    }
}
