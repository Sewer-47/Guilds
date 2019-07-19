package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.lang.ref.Reference;

public class GuildHeart implements Listener {  //THIS CLASS MUST BE REWRITED

    private final Vector location;
    private final Reference<World> world;
    private final GuildsPlugin plugin;
    private final GuildRender render;
    private final GuildMemebers memebers;
    private final GuildTerrain terrain;

    public GuildHeart(GuildRender render, GuildMemebers memebers, GuildTerrain terrain, GuildsPlugin plugin) { ;
        this.location = terrain.getHome();
        this.world = terrain.getWorld();
        this.plugin = plugin;
        this.render = render;
        this.memebers = memebers;
        this.terrain = terrain;
    }

    public void create() {
        if (this.world.get() != null) {
            World world = this.world.get();
            Location location = new Location(world, this.location.getX(), this.location.getY(), this.location.getZ());
            world.spawnEntity(location, EntityType.ENDER_CRYSTAL);
        }
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
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity != null && entity.getType() == EntityType.ENDER_CRYSTAL) {
            if (entity.getLocation().toVector().distance(this.terrain.getHome()) <= 1) {
                this.plugin.getUserManager().getUser(event.getPlayer()).ifPresent(user -> {
                    user.sendMessage("guildInfo", this.render.getTag(), this.render.getName(), "members", this.memebers.getOwner().getName());
                    return;
                });
            }
        }
    }
}
