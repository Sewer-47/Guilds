package me.sewer.guilds.validity.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.Relation;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildDeleteEvent;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.validity.Validity;
import me.sewer.guilds.window.WindowManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GuildValidityTask implements Runnable {

    private final GuildManager guildManager;
    private final UserManager userManager;
    private final RegionManager regionManager;
    private final WindowManager windowManager;
    private final GuildsPlugin plugin;

    public GuildValidityTask(GuildsPlugin plugin) {
        this.guildManager = plugin.getGuildManager();
        this.userManager = plugin.getUserManager();
        this.regionManager = plugin.getRegionManager();
        this.windowManager = plugin.getWindowManager();
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.guildManager.getAll().forEach(guild -> {
            Validity validity = guild.getValidity();
            if (validity.hasExpired()) {
                GuildDeleteEvent event = new GuildDeleteEvent(guild, true);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    GuildTerrain terrain = guild.getTerrain();
                    World world = terrain.getWorld().get();
                    Vector vector = terrain.getHome();
                    String worldName = (world == null) ? "" : world.getName();

                    this.plugin.getUserManager().getOnline().forEach(user -> user.sendMessage("guildExpire", guild.getRender().getTag(), vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), worldName));

                    guild.getMembers().getAll().forEach(uuid -> {
                        this.userManager.getUser(uuid).ifPresent(member -> {
                            member.setGuild(null);
                            guild.getMembers().removePlayer(uuid);
                        });
                    });

                    Map<UUID, Relation> relations = guild.getRelations().getAll();
                    relations.keySet().forEach(relationGuildId -> {
                        this.guildManager.getGuild(relationGuildId).ifPresent(relationGuild -> {
                            relationGuild.getRelations().remove(guild.getUniqueId());
                        });
                    });

                    this.guildManager.unregisterGuild(guild);
                    this.regionManager.byWorldId(world.getUID()).remove(terrain.getRegion());
                    this.windowManager.unregisterWindow(guild.getSafe());
                }

            }
        });
    }
}
