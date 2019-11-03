package me.sewer.guilds.region.impl;

import me.sewer.guilds.region.Region;
import me.sewer.guilds.region.RegionBounds;
import org.bukkit.util.Vector;

public class CuboidRegion extends Region {

    private final Vector vectorMax;
    private final Vector vectorMin;
    private final RegionBounds bounds;

    public CuboidRegion(Vector vectorMax, Vector vectorMin, String name) {
        super(name);
        this.vectorMax = Vector.getMaximum(vectorMax, vectorMin);
        this.vectorMin = Vector.getMinimum(vectorMax, vectorMin);

        this.bounds = this.createBounds();
    }

    public Vector getVectorMax() {
        return this.vectorMax;
    }

    public Vector getVectorMin() {
        return this.vectorMin;
    }

    protected RegionBounds createBounds() {
        return new RegionBounds(this.vectorMax, this.vectorMin, this.getName());
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return this.vectorMin.getX() <= x && this.vectorMax.getX() >= x && this.vectorMin.getY() <= y && this.vectorMax.getY() >= y && this.vectorMin.getZ() <= z && this.vectorMax.getZ() >= z;
    }

    @Override
    public RegionBounds getBounds() {
        return this.bounds;
    }
}

