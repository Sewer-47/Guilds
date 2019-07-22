package me.sewer.guilds.command.impl.create;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.guild.member.GuildMember;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.member.GuildPermission;
import me.sewer.guilds.guild.member.PermissionsWindow;
import me.sewer.guilds.region.CuboidRegion;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.validity.Validity;
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
    public static final String DESCRIPTION = "guild creating";
    private final GuildsPlugin plugin;
    private final GuildManager guildManager;
    private final CreateOptions options;

    public CreateCommand(GuildsPlugin plugin, CreateOptions options) {
        super(NAME, DESCRIPTION, "zaloz");
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
        this.options = options;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.plugin.getUserManager().getUser(sender).ifPresent(user ->  {
            if (args.length == 3) {
                if (!user.getGuild().isPresent()) {

                    Location location = user.getBukkit().get().getLocation();
                    if (this.guildManager.byTag(args[1]).isPresent()) {
                        user.sendMessage("tagExists");
                        return;
                    }

                    if (this.guildManager.byName(args[2]).isPresent()) {
                        user.sendMessage("nameExists");
                        return;
                    }

                    Random random = new Random();
                    UUID uniqueId = new UUID(random.nextLong(), random.nextLong());
                    GuildRender render = new GuildRender(args[1], args[2]);
                    GuildMember guildMember = new GuildMember(user, this.plugin);
                    guildMember.addPermission(GuildPermission.ADD);
                    guildMember.addPermission(GuildPermission.KICK);
                    guildMember.addPermission(GuildPermission.OPEN_SAFE);
                    guildMember.addPermission(GuildPermission.MANAGE_SAFE);
                    guildMember.addPermission(GuildPermission.SETHOME);
                    GuildMembers members = new GuildMembers(guildMember);
                    World world = location.getWorld();

                    int regionSize = this.options.regionSize();

                    Vector vectorMin = location.toVector();
                    vectorMin.setX(vectorMin.getX() - regionSize);
                    vectorMin.setY(0);
                    vectorMin.setZ(vectorMin.getZ() - regionSize);

                    Vector vectorMax = location.toVector();
                    vectorMax.setX(vectorMax.getX() + regionSize);
                    vectorMax.setY(world.getMaxHeight());
                    vectorMax.setZ(vectorMax.getZ() + regionSize);

                    Region region = new CuboidRegion(vectorMax, vectorMin, render.getTag());
                    GuildTerrain terrain = new GuildTerrain(region, location);
                    GuildRelations relations = new GuildRelations();


                    ConfigurationSection configurationSection = this.plugin.getConfig().getConfigurationSection("guildValidity");
                    LocalDateTime expire = LocalDateTime.now()
                            .plusMinutes(configurationSection.getInt("minutes"))
                            .plusHours(configurationSection.getInt("hours"))
                            .plusDays(configurationSection.getInt("days"))
                            .plusMonths(configurationSection.getInt("months"));
                    Validity validity = new Validity(expire);

                    GuildHeart crystal = new GuildHeart(render, members, terrain, this.plugin);

                    GuildSafe safe = new GuildSafe(members, this.options.guildSafeSize(), this.options.guildSafeName()
                            .replace("{0}", render.getTag())
                            .replace("{1}", render.getName()), this.plugin);

                    PermissionsWindow permissions = new PermissionsWindow("", members, this.plugin.getMessageManager());

                    Guild guild = new Guild(uniqueId, render, members, terrain, relations, crystal, safe, permissions, validity);

                    GuildCreateEvent event = new GuildCreateEvent(guild, user);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        this.plugin.getWindowManager().registerWindow(safe);
                        this.plugin.getWindowManager().registerWindow(permissions);
                        crystal.create();
                        Bukkit.getPluginManager().registerEvents(crystal, this.plugin);
                        this.guildManager.registerGuild(guild);
                        this.plugin.getRegionManager().byWorldId(world.getUID()).add(region);
                        user.setGuild(guild);
                        user.sendMessage("guildCreate", render.getTag(), render.getName());
                        this.plugin.getUserManager().getOnline().forEach(user1 -> user1.sendMessage("guildCreateBc", sender.getName(), render.getTag(), render.getName()));

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
