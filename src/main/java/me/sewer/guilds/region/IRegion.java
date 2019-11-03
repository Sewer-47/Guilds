package me.sewer.guilds.region;

import me.sewer.guilds.Unique;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.util.UUID;

public interface IRegion extends Unique {

    default boolean contains(Vector vector) {
        return this.contains(vector.getX(), vector.getY(), vector.getZ());
    }

    default boolean contains(Entity entity) {
        Location location = entity.getLocation();
        return this.contains(location.getX(), location.getY(), location.getZ());
    }

    default boolean contains(Location location) {
        return this.contains(location.getX(), location.getY(), location.getZ());
    }

    boolean contains(double x, double y, double z);

    RegionBounds getBounds();
}

