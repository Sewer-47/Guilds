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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CreateCommand extends Command {

    public static final String NAME = "create";
    public static final String DESCRIPTION = "zakladanie gildii";
    private final GuildsPlugin plugin;
    private final GuildManager guildManager;
    private final int spawnDistance;
    private final int guildDistance;
    private final List<String> allowedWorlds;
    private final boolean guildCreatingEnabled;
    private final int tagMinLength;
    private final int tagMaxLength;
    private final int nameMinLength;
    private final int nameMaxLength;

    public CreateCommand(GuildsPlugin plugin, FileConfiguration configuration) {
        super(NAME, DESCRIPTION, "zaloz");
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
        this.spawnDistance = configuration.getInt("minDistanceToSpawn");
        this.guildDistance = configuration.getInt("minDistanceBetweenGuilds");
        this.allowedWorlds = configuration.getStringList("allowedWorlds");
        this.guildCreatingEnabled = configuration.getBoolean("guildCreatingEnabled");
        ConfigurationSection render = configuration.getConfigurationSection("guildRender");
        this.tagMinLength = render.getInt("tagMinLength");
        this.tagMaxLength = render.getInt("tagMaxLength");
        this.nameMinLength = render.getInt("nameMinLength");
        this.nameMaxLength = render.getInt("nameMaxLength");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.plugin.getUserManager().getUser(sender).ifPresent(user ->  {
            if (args.length == 3) {
                if (!user.getGuild().isPresent()) {
                    if (!this.guildCreatingEnabled) {
                        user.sendMessage("guildCreatingIsDisabled");
                        return;
                    }

                    if (args[1].length() < this.tagMinLength || args[1].length() > this.tagMaxLength) {
                        user.sendMessage("correctTagLength", this.tagMinLength, this.tagMaxLength);
                        return;
                    }

                    if (args[2].length() < this.nameMinLength || args[2].length() > this.nameMaxLength) {
                        user.sendMessage("correctNameLength", this.nameMinLength, this.nameMaxLength);
                        return;
                    }

                    Location location = user.getBukkit().get().getLocation();
                    if (this.guildManager.byTag(args[1]).isPresent()) {
                        user.sendMessage("tagExists");
                        return;
                    }

                    if (this.guildManager.byName(args[2]).isPresent()) {
                        user.sendMessage("nameExists");
                        return;
                    }

                    if (!this.allowedWorlds.contains(location.getWorld().getName())) {
                        user.sendMessage("worldCreationBlocked");
                        return;
                    }


                    if (location.distance(location.getWorld().getSpawnLocation()) <= spawnDistance) {
                        user.sendMessage("tooNearSpawn");
                        return;
                    }

                    for (Guild guild : this.guildManager.getAll()) { //Cant use lambda :(
                        Vector home = guild.getTerrain().getHome();
                        if (home.distance(location.toVector()) <= guildDistance) {
                            user.sendMessage("tooNearOtherGuild");
                            return;
                        }
                    }

                    Random random = new Random();
                    UUID uniqueId = new UUID(random.nextLong(), random.nextLong());
                    GuildRender render = new GuildRender(args[1], args[2]);
                    GuildMemebers memebers = new GuildMemebers(user);
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


                    ConfigurationSection configurationSection = this.plugin.getConfig().getConfigurationSection("guildValidity");
                    LocalDateTime expire = LocalDateTime.now()
                            .plusMinutes(configurationSection.getInt("minutes"))
                            .plusHours(configurationSection.getInt("hours"))
                            .plusDays(configurationSection.getInt("days"))
                            .plusMonths(configurationSection.getInt("months"));
                    GuildValidity validity = new GuildValidity(expire);

                    GuildCrystal crystal = new GuildCrystal(render, memebers, terrain, this.plugin);

                    Guild guild = new Guild(uniqueId, render, memebers, terrain, relations, crystal, validity);

                    GuildCreateEvent event = new GuildCreateEvent(guild);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {

                        crystal.create();
                        Bukkit.getPluginManager().registerEvents(crystal, this.plugin);
                        this.guildManager.registerGuild(guild);
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
