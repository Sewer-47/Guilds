package me.sewer.guilds.region;

import me.sewer.guilds.region.impl.CuboidRegion;
import org.bukkit.util.Vector;

public class RegionBounds extends CuboidRegion {

    public RegionBounds(Vector vectorMax, Vector vectorMin, String name) {
        super(vectorMax, vectorMin, name);
    }

    @Override
    protected RegionBounds createBounds() {
        return this;
    }

    @Override
    public RegionBounds getBounds() {
        return this;
    }
}
