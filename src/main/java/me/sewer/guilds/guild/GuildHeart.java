package me.sewer.guilds.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.member.GuildMembers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;
import java.util.*;

public class GuildHeart implements Listener { //members variable

    private final Vector location;
    private final UUID worldId;
    private final GuildsPlugin plugin;
    private final GuildRender render;
    private final GuildMembers memebers;
    private EnderCrystal crystal;

    public GuildHeart(GuildRender render, GuildMembers memebers, GuildTerrain terrain, GuildsPlugin plugin) {
        this.location = terrain.getHome();
        this.worldId = terrain.getWorld().get().getUID(); //fix it
        this.plugin = plugin;
        this.render = render;
        this.memebers = memebers;
    }

    public void create() {
        World world = Bukkit.getWorld(this.worldId);
        if (world != null) {
            Location location = new Location(world, this.location.getX(), this.location.getY(), this.location.getZ());
            this.crystal = world.spawn(location, EnderCrystal.class);
            this.crystal.setInvulnerable(true);
        }
    }

    public void kill() {
        if (this.crystal != null) {
            this.crystal.remove();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().equals(this.crystal)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity != null && entity.equals(this.crystal)) {
            this.plugin.getUserManager().getUser(event.getPlayer()).ifPresent(user -> {
                user.sendMessage("guildInfo", this.render.getTag(), this.render.getName(), "members");
            });
        }
    }

    public EnderCrystal getCrystal() {
        return this.crystal;
    }
}
