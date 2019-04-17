package me.sewer.guilds.guild.event;

import me.sewer.guilds.guild.Guild;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;

    public GuildDeleteEvent(Guild guild) {
        this.guild = guild;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
