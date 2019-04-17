package me.sewer.guilds.util;

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

    public static void sendAll(String message, boolean console) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(color(message)));
        if (console) {
            sendConsole(message);
        }
    }
}
