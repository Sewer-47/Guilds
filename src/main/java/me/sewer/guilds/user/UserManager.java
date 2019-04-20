package me.sewer.guilds.user;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private final Map<UUID, User> byUniqueId = new ConcurrentHashMap<>(); //Async
    private final Map<String, User> byUsername = new ConcurrentHashMap<>();

    public Optional<User> getUser(CommandSender sender) {
        if (sender instanceof Player) {
            return this.getUser((Player) sender);
        }

        return null;
    }

    public Optional<User> getUser(Entity entity) {
        if (entity instanceof Player) {
            return this.getUser(entity.getUniqueId());
        }

        return null;
    }

    public Optional<User> getUser(Player player) {
        return this.getUser(player.getUniqueId());
    }

    public Optional<User> getUser(UUID uniqueId) {
        return Optional.ofNullable(this.byUniqueId.get(uniqueId));
    }

    public Optional<User> getUser(String username) {
        return Optional.ofNullable(this.byUsername.get(username));
    }

    public void registerUser(User user) {
        this.byUniqueId.put(user.getUniqueId(), user);
        this.byUsername.put(user.getName(), user);
    }

    public void unregisterUser(User user) {
        this.byUniqueId.remove(user.getUniqueId());
        this.byUsername.remove(user.getName());
    }

    public Collection<User> getOnline() {
        return this.byUniqueId.values();
    }
}