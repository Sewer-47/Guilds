package me.sewer.guilds.region;

import org.bukkit.util.Vector;

public class CuboidRegion extends Region {

    private final Vector vectorMax;
    private final Vector vectorMin;

    public CuboidRegion(Vector vectorMax, Vector vectorMin, String name) {
        super(name);
        this.vectorMax = Vector.getMaximum(vectorMax, vectorMin);
        this.vectorMin = Vector.getMinimum(vectorMax, vectorMin);
    }

    public Vector getVectorMax() {
        return this.vectorMax;
    }

    public Vector getVectorMin() {
        return this.vectorMin;
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return this.vectorMin.getX() <= x && this.vectorMax.getX() >= x && this.vectorMin.getY() <= y && this.vectorMax.getY() >= y && this.vectorMin.getZ() <= z && this.vectorMax.getZ() >= z;
    }
}

