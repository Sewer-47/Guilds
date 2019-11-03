package me.sewer.guilds.vector;

import org.bukkit.util.Vector;

public class Vector3 extends Vector2 {

    private int z;

    public Vector3(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return this.z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int distance(Vector3 vector) {
        int x = vector.getX() - this.getX();
        int y = vector.getY() - this.getY();
        int z = vector.getZ() - this.getZ();
        return (int) Math.sqrt(x * x + y * y + z * z);
    }

    public static Vector3 fromBukkit(Vector bukkit) {
        return new Vector3(bukkit.getBlockX(), bukkit.getBlockZ(), bukkit.getBlockY());
    }

    public static Vector toBukkit(Vector3 vector3) {
        return new Vector(vector3.getX(), vector3.getZ(), vector3.getY());
    }
}//getMax, getMin, plus, minus i tostring i w ogole zrobic trzeba
