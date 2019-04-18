package me.sewer.guilds.guild;

import me.sewer.guilds.region.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class GuildTerrain {

    private final Region region;
    private final Vector home;
    private final Reference<World> world;

    public GuildTerrain(Region region, Location home) {
        this.region = region;
        this.home = home.toVector();
        this.world = new WeakReference<>(home.getWorld());
    }

    public Region getRegion() {
        return region;
    }

    public Vector getHome() {
        return home;
    }

    public Reference<World> getWorld() {
        return world;
    }
}
