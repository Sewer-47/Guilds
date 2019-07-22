package me.sewer.guilds.guild.event;

import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;
    private final User who;
    private boolean cancelled;

    public GuildCreateEvent(Guild guild, User who) {
        this.guild = guild;
        this.who = who;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public User getWho() {
        return who;
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
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
