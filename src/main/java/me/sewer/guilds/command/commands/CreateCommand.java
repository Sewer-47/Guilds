package me.sewer.guilds.command.commands;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.region.CuboidRegion;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class CreateCommand extends Command {

    public static final String NAME = "create";
    public static final String DESCRIPTION = "zakladanie gildii";
    private final GuildsPlugin plugin;

    public CreateCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        User user = this.plugin.getUserManager().getUser(sender).get();
        Random random = new Random();
        UUID uniqueId = new UUID(random.nextLong(), random.nextLong());
        GuildRender render = new GuildRender("tag", "nazwa");
        GuildMemebers memebers = new GuildMemebers(user);
        Location location = user.getBukkit().get().getLocation();
        World world = location.getWorld();

        Vector vectorMin = location.toVector();
        vectorMin.setX(vectorMin.getX() - 25);
        vectorMin.setY(0);
        vectorMin.setZ(vectorMin.getZ() - 25);

        Vector vectorMax = location.toVector();
        vectorMax.setX(vectorMax.getX() + 25);
        vectorMax.setY(world.getMaxHeight());
        vectorMax.setZ(vectorMax.getZ() + 25);

        Region region = new CuboidRegion(vectorMax, vectorMin, render.getTag());
        GuildTerrain terrain = new GuildTerrain(region, location);
        GuildRelations relations = new GuildRelations();

        GuildCrystal crystal = new GuildCrystal(location);
        crystal.create();
        Bukkit.getPluginManager().registerEvents(crystal, this.plugin);
        Guild guild = new Guild(uniqueId, render, memebers, terrain, relations, crystal);

        GuildCreateEvent event = new GuildCreateEvent(guild);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            this.plugin.getGuildManager().registerGuild(guild);
            this.plugin.getRegionManager().byWorldId(world.getUID()).add(region);
            sender.sendMessage("Zalozyles gildie");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("Gracz " + sender.getName() + " zalozyl gildie o tagu " + render.getTag());
            }
        }
        return true;
    }
}
