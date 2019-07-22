package me.sewer.guilds.guild.event;

import me.sewer.guilds.guild.Guild;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildDeleteEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;
    private boolean cancelled;

    public GuildDeleteEvent(Guild guild, boolean async) {
        super(async);
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

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
