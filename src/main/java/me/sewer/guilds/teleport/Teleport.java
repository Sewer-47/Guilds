package me.sewer.guilds.teleport;

import me.sewer.guilds.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleport {

    private final Location location;
    private final Location playerLocation;
    private final User user;
    private final String name;
    private final int duration;
    private int attempt;

    public Teleport(Location location, User user, String name, int duration) {
        this.location = location;
        this.user = user;
        this.name = name;
        this.duration = duration;
        this.attempt = 0;
        this.playerLocation = user.getBukkit().get().getLocation().clone();
    }

    public boolean tryTeleport() {
        if (user == null || location == null) {
            return true;
        }
        if (this.attempt == 0) {
            user.sendMessage("waitingTeleportation", this.duration);
        }

        Player player = user.getBukkit().get();

        if (player.getLocation().distance(playerLocation) >= 0.2) {
            user.sendMessage("teleportCanceled");
            return true;
        }
        if (this.attempt >= this.duration) {
            player.teleport(this.location);
            user.sendMessage("teleported", this.name);
            return true;
        } else {
            this.attempt++;
            return false;
        }
    }
}