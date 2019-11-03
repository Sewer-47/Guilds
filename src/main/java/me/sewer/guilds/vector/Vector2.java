package me.sewer.guilds.vector;

import org.bukkit.util.Vector;

public class Vector2 {

    private int x;
    private int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int distance(Vector2 vector) {
        int x = vector.getX() - this.getX();
        int y = vector.getY() - this.getY();
        return (int) Math.sqrt(x * x + y * y);
    }

    public static Vector2 fromBukkit(Vector bukkit) {
        return new Vector2(bukkit.getBlockX(), bukkit.getBlockZ());
    }

    public static Vector toBukkit(Vector2 vector2) {
        return new Vector(vector2.x, 0, vector2.y);
    }
}
