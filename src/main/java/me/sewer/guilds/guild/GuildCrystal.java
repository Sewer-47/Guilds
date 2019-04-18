package me.sewer.guilds.guild;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.util.Vector;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public class GuildCrystal implements Listener {

    private final Vector location;
    private final Reference<World> world;

    public GuildCrystal(Location location) {
        this.location = location.toVector();
        this.world = new WeakReference<>(location.getWorld());
    }

    public void create() {
        Location location = new Location(world.get(), this.location.getX(), this.location.getY(), this.location.getZ());
        this.world.get().spawnEntity(location, EntityType.ENDER_CRYSTAL);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.ENDER_CRYSTAL) {
            if (entity.getLocation().toVector().distance(this.location) <= 1) {
                this.create();
            }
        }
    }

    @EventHandler
    public void onInteract(EntityInteractEvent event) {
    }
}
