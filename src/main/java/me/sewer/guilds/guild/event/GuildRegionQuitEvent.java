package me.sewer.guilds.guild.event;

import me.sewer.guilds.guild.Guild;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GuildRegionQuitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Guild guild;
    private final Player who;

    public GuildRegionQuitEvent(Guild guild, Player who) {
        this.guild = guild;
        this.who = who;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public Player getWho() {
        return this.who;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
