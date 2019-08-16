package me.sewer.guilds.command.impl.create;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildCreateEvent;
import me.sewer.guilds.guild.member.GuildMember;
import me.sewer.guilds.guild.member.GuildMembers;
import me.sewer.guilds.guild.permission.GuildPermission;
import me.sewer.guilds.guild.permission.PermissionsWindow;
import me.sewer.guilds.i18n.MessageManager;
import me.sewer.guilds.region.CuboidRegion;
import me.sewer.guilds.region.Region;
import me.sewer.guilds.user.User;
import me.sewer.guilds.validity.Validity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public class CreateCommand extends Command {

    public static final String NAME = "create";
    private final GuildsPlugin plugin;
    private final GuildManager guildManager;
    private final MessageManager messageManager;
    private final CreateOptions options;

    public CreateCommand(GuildsPlugin plugin, CreateOptions options) {
        super(NAME, "zaloz");
        this.plugin = plugin;
        this.guildManager = plugin.getGuildManager();
        this.messageManager = plugin.getMessageManager();
        this.options = options;
    }

    @Override
    public boolean onCommand(User user, String[] args) {
        if (args.length >= 2) {
            if (user.getGuild().isPresent()) {
                user.sendMessage("hasGuild");
                return true;
            }

            Location location = user.getBukkit().get().getLocation();
            if (this.guildManager.byTag(args[0]).isPresent()) {
                user.sendMessage("tagExists");
                return true;
            }

            if (this.guildManager.byName(args[1]).isPresent()) {
                user.sendMessage("nameExists");
                return true;
            }

            Random random = new Random();
            UUID uniqueId = new UUID(random.nextLong(), random.nextLong());
            GuildRender render = new GuildRender(args[0], args[1], Arrays.toString(Arrays.copyOfRange(args, 2, args.length))
                    .replace(",", "")
                    .replace("[", "")
                    .replace("]", ""));
            GuildMember guildMember = new GuildMember(user);
            guildMember.addPermission(GuildPermission.ADD);
            guildMember.addPermission(GuildPermission.KICK);
            guildMember.addPermission(GuildPermission.OPEN_SAFE);
            guildMember.addPermission(GuildPermission.MANAGE_SAFE);
            guildMember.addPermission(GuildPermission.SETHOME);
            guildMember.addPermission(GuildPermission.ALLY);
            guildMember.addPermission(GuildPermission.BREAK);
            guildMember.addPermission(GuildPermission.ACCEPT);
            guildMember.addPermission(GuildPermission.ENEMY);
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
                if (render.getDescription().length() > 0) {
                    user.sendMessage("guildCreate", render.getTag(), render.getName(), render.getDescription());
                    this.plugin.getUserManager().getOnline().forEach(user1 -> user1.sendMessage("guildCreateBc", user.getUsername(), render.getTag(), render.getName(), render.getDescription()));
                } else {
                    String lack = this.messageManager.getMessage(user.getLocale(), "lack");
                    user.sendMessage("guildCreate", render.getTag(), render.getName(), lack);
                    this.plugin.getUserManager().getOnline().forEach(user1 -> user1.sendMessage("guildCreateBc", user.getUsername(), render.getTag(), render.getName(), lack));
                }
            }
        } else {
            return false;
        }
        return true;
    }
}
