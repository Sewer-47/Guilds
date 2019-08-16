package me.sewer.guilds.module.impl.world;

import me.sewer.guilds.command.impl.create.CreateOptions;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.module.Module;
import me.sewer.guilds.module.ModuleInfo;
import me.sewer.guilds.user.User;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ModuleInfo(name = "BorderDistanceModule")
public class BorderDistanceModule extends Module { //THANKS FOR HELP https://github.com/ark223

    private final int distance;

    public BorderDistanceModule(CreateOptions options) {
        this.distance = options.borderDistance();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCreate(GuildCreateEvent event) {
        User user = event.getCreator();
        user.getBukkit().ifPresent(player -> {
            Vector location = player.getLocation().toVector();
            World world = event.getGuild().getTerrain().getWorld().get();
            if (world == null) {
                return;
            }
            WorldBorder worldBorder = world.getWorldBorder();
            double borderSize = worldBorder.getSize() / 2;
            Vector borderCenter = worldBorder.getCenter().toVector();
            Vector edge0 = new Vector(borderCenter.getBlockX() + borderSize,0,borderCenter.getBlockZ() + borderSize);
            Vector edge1 = new Vector(borderCenter.getBlockX() - borderSize,0,borderCenter.getBlockZ() + borderSize);
            Vector edge2 = new Vector(borderCenter.getBlockX() - borderSize,0,borderCenter.getBlockZ() - borderSize);
            Vector edge3 = new Vector(borderCenter.getBlockX() + borderSize,0,borderCenter.getBlockZ() - borderSize);
            Vector[] rectangle =  {edge0, edge1, edge2, edge3};
            List<Double> result = new ArrayList<>();
            for (int i=0; i < rectangle.length; i++) {
                Vector segment0 = rectangle[i];
                Vector segment1 = i != (rectangle.length - 1) ? rectangle[i + 1] : rectangle[0];
                double distance = this.distance(location, closestPointOnSegment(segment0, segment1, location));
                result.add(distance);
            }
            Collections.sort(result);
            int distance = result.get(0).intValue();
            if (distance <= this.distance) {
                user.sendMessage("tooCloseBorder", this.distance, distance);
                event.setCancelled(true);
            }
        });
    }

    private Vector closestPointOnSegment(Vector segment0, Vector segment1, Vector point) {
        Vector ab = new Vector(segment1.getX() - segment0.getX(), 0, segment1.getZ() - segment0.getZ());
        double t = ((point.getX() - segment0.getX()) * ab.getX() + (point.getZ() - segment0.getZ()) * ab.getZ()) / (ab.getX() * ab.getX() + ab.getZ() * ab.getZ());
        if (t < 0) {
            return segment0;
        }
        if (t > 1) {
            return segment1;
        }
        return new Vector(ab.getX() * t + segment0.getX(), 0, ab.getZ() * t + segment0.getZ());
    }

    private double distance(Vector vector0, Vector vector1) {
        double x = vector1.getX() - vector0.getX();
        double z = vector1.getZ() - vector0.getZ();
        return Math.sqrt(x * x + z * z);
    }
}