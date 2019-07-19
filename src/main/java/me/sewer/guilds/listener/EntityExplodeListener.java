package me.sewer.guilds.listener;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class EntityExplodeListener implements Listener {

    private final Map<Material, Integer> materialIntegerMap; //idk to name it
    private final Random random;
    private final GuildsPlugin plugin;

    public EntityExplodeListener(GuildsPlugin plugin) {
        this.materialIntegerMap = new HashMap<>();
        this.random = new Random();
        this.plugin = plugin;

        ConfigurationSection configurationSection = this.plugin.getConfig().getConfigurationSection("blocksCanBeDestroyedByTnt");
        for (String key : configurationSection.getKeys(false)) {

            Material material = Material.matchMaterial(key);
            if (material != null) {
                materialIntegerMap.put(material, configurationSection.getInt(key));
            } else {
                this.plugin.getLogger().log(Level.SEVERE, "Cannot parse material " + key);
            }
        }

    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();
        int radius = 5;
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (materialIntegerMap.keySet().contains(block.getType())) {
                        int chance = materialIntegerMap.get(block.getType());
                        if (chance > random.nextInt(100)) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
