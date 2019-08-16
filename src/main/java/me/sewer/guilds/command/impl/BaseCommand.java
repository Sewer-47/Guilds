package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildTerrain;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.teleport.Teleport;
import me.sewer.guilds.teleport.TeleportManager;
import me.sewer.guilds.user.User;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class BaseCommand extends Command {

    public static final String NAME = "base";
    private final MessageManager messageManager;
    private final TeleportManager teleportManager;
    private final int cooldown;

    public BaseCommand(GuildsPlugin plugin) {
        super(NAME, "home", "dom", "baza");
        this.messageManager = plugin.getMessageManager();
        this.teleportManager = plugin.getTeleportManager();
        this.cooldown = plugin.getConfig().getInt("tpCooldown");
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        if (user.getBukkit().isPresent()) {
            GuildTerrain terrain = guild.getTerrain();
            Vector home = terrain.getHome();
            Location homeLocation = new Location(terrain.getWorld().get(), home.getX(), home.getY(), home.getZ());
            String tpPoint = this.messageManager.getMessage(user.getLocale(), "home");
            Teleport teleport = new Teleport(homeLocation, user, tpPoint, this.cooldown);
            this.teleportManager.addTeleport(teleport);
        }
        return true;
    }
}
