package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildTerrain;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class HomeCommand extends Command {

    public static final String NAME = "home";
    public static final String DESCRIPTION = "come home";
    private final UserManager userManager;
    private final MessageManager messageManager;
    private final GuildsPlugin plugin;
    private final int cooldown;

    public HomeCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION, "dom", "baza");
        this.userManager = plugin.getUserManager();
        this.messageManager = plugin.getMessageManager();
        this.cooldown = plugin.getConfig().getInt("tpCooldown");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                user.sendMessage("waitingTeleportation", 5);
                if (user.getBukkit().isPresent()) {
                    Player player = user.getBukkit().get();
                    Location location = player.getLocation();
                    for (int i = 0; i < this.cooldown; i++) {
                        Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
                           @Override
                           public void run() {
                               if (player == null || location == null) {
                                   return;
                               }

                               if (player.getLocation().distance(location) > 0.2) {
                                   user.sendMessage("moved");
                                   return;
                               }
                           }
                        }, 20);
                    }
                    GuildTerrain terrain = guild.getTerrain();
                    Vector home = terrain.getHome();
                    player.teleport(new Location(terrain.getWorld().get(), home.getX(), home.getY(), home.getZ()));
                    String tpPoint = this.messageManager.getMessage(user.getLocale(), "home");
                    user.sendMessage("teleported", tpPoint);
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
