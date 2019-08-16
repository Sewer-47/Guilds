package me.sewer.guilds;

import org.bukkit.Material;

import java.util.Map;

public class Items {

    private final Map<String, Material> items;

    public Items(Map<String, Material> items) {
        this.items = items;
    }

    public Material getItem(String item) {
        Material material = this.items.get(item);
        return material != null ? material : Material.matchMaterial(item);
    }
}
