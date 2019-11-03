package me.sewer.guilds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public interface Messageable {

    ChatColor INFO_COLOR = ChatColor.WHITE;
    ChatColor SUCCESS_COLOR = ChatColor.GREEN;
    ChatColor ERROR_COLOR = ChatColor.RED;

    void send(ChatMessageType position, BaseComponent message);

    default void sendInfo(ChatMessageType position, BaseComponent message) {
        message.setColor(INFO_COLOR);
        this.send(position, message);
    }

    default void sendSuccess(ChatMessageType position, BaseComponent message) {
        message.setColor(SUCCESS_COLOR);
        this.send(position, message);
    }

    default void sendError(ChatMessageType position, BaseComponent message) {
        message.setColor(ERROR_COLOR);
        this.send(position, message);
    }
}
