package me.sewer.guilds.command.commands;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.region.CuboidRegion;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class CreateCommand extends Command {

    public static final String NAME = "create";
    public static final String DESCRIPTION = "zakladanie gildii";
    private final GuildsPlugin plugin;

    public CreateCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION, "zaloz");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.plugin.getUserManager().getUser(sender).ifPresent(user ->  {
            if (args.length == 3) {

                if (!user.getGuild().isPresent()) {
                    Random random = new Random();
                    UUID uniqueId = new UUID(random.nextLong(), random.nextLong());
                    GuildRender render = new GuildRender(args[1], args[2]);
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

                    ConfigurationSection expireYaml = this.plugin.getConfig().getConfigurationSection("guildValidity");
                    LocalDateTime expire = LocalDateTime.now();
                    expire.plusMinutes(expireYaml.getInt("minutes"));
                    expire.plusHours(expireYaml.getInt("hours"));
                    expire.plusDays(expireYaml.getInt("days"));
                    expire.plusMonths(expireYaml.getInt("months"));
                    GuildValidity validity = new GuildValidity(expire);

                    Guild guild = new Guild(uniqueId, render, memebers, terrain, relations, crystal, validity);

                    GuildCreateEvent event = new GuildCreateEvent(guild);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {

                        crystal.create();
                        Bukkit.getPluginManager().registerEvents(crystal, this.plugin);

                        this.plugin.getGuildManager().registerGuild(guild);
                        this.plugin.getRegionManager().byWorldId(world.getUID()).add(region);
                        user.setGuild(guild);
                        user.sendMessage("guildCreate", render.getTag(), render.getName());
                        ChatUtil.sendAll("guildCreateBc", this.plugin.getUserManager(), sender.getName(), render.getTag(), render.getName());
                    }
                } else {
                    user.sendMessage("hasGuild");
                }
            } else {
                user.sendMessage("correctUsage", "/guild create <tag> <name>");
            }
        });
        return true;
    }
}
