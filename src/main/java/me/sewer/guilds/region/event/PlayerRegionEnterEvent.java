package me.sewer.guilds.region.event;

import me.sewer.guilds.region.Region;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRegionEnterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Region region;

    public PlayerRegionEnterEvent(Player player, Region region) {
        super(true);
        this.player = player;
        this.region = region;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Region getRegion() {
        return this.region;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

