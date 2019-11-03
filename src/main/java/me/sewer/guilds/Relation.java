package me.sewer.guilds;

public class Relation {

    private final String name;

    public Relation(String name) {
        this.name = name;
    }

    public static final Relation ALLY = new Relation("ally");

    public static final Relation ENEMY = new Relation("enemy");

    public String getName() {
        return this.name;
    }
}
