package me.sewer.guilds.region.impl;

import me.sewer.guilds.region.Region;
import me.sewer.guilds.region.RegionBounds;
import org.bukkit.util.Vector;

public class SphereRegion extends Region {

    private final Vector center;
    private final double radius;
    private final RegionBounds bounds;

    public SphereRegion(Vector center, double radius, String name){
        super(name);
        this.center = center;
        this.radius = radius;
        this.bounds = this.createBounds();
    }

    public Vector getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    protected RegionBounds createBounds() {
        return new RegionBounds(
                this.center.clone().subtract(new Vector(this.radius, this.radius, this.radius)),
                this.center.clone().add(new Vector(this.radius, this.radius, this.radius)),
                this.getName()
        );
    }

    @Override
    public boolean contains(double x, double y, double z) {
        return new Vector(x, y, z).isInSphere(this.getCenter(), this.getRadius());
    }

    @Override
    public RegionBounds getBounds() {
        return null;
    }
}
