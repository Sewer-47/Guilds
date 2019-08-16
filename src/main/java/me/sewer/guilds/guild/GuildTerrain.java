package me.sewer.guilds.guild;

import me.sewer.guilds.region.Region;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Optional;

public class GuildTerrain {

    private final Region region;
    private final Vector center;
    private Vector home;
    private final Reference<World> world;

    public GuildTerrain(Region region, Location home) {
        this.region = region;
        this.center = home.toVector();
        this.home = center;
        this.world = new WeakReference<>(home.getWorld());
    }

    public Region getRegion() {
        return this.region;
    }

    public Vector getCenter() {
        return this.center;
    }

    public Vector getHome() {
        return this.home;
    }

    public Optional<World> getWorld() {
        return Optional.ofNullable(this.world.get());
    }

    public void setHome(Vector home) {
        this.home = home;
    }
}
