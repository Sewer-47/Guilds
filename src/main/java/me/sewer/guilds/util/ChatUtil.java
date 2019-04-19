package me.sewer.guilds.util;

import me.sewer.guilds.user.UserManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.logging.Level;

public final class ChatUtil {

    private ChatUtil(){

    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(message));
    }

    public static void sendConsole(String message) {
        sendConsole(Level.INFO, message);
    }

    public static void sendConsole(Level level, String message) {
        Bukkit.getLogger().log(level, ChatColor.stripColor(Objects.requireNonNull(message)));
    }

    public static void sendAll(String message, UserManager userManager, Object... params) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            userManager.getUser(player).ifPresent(user -> user.sendMessage(message, params));
        });
    }
}
