package me.sewer.guilds.validity.guild;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildTerrain;
import me.sewer.guilds.guild.event.GuildDeleteEvent;
import me.sewer.guilds.validity.Validity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class GuildValidityTask implements Runnable {

    private final GuildManager guildManager;
    private final GuildsPlugin plugin;

    public GuildValidityTask(GuildsPlugin plugin) {
        this.guildManager = plugin.getGuildManager();
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.guildManager.getAll().forEach(guild -> {
            Validity validity = guild.getValidity();
            if (validity.hasExpired()) {
                Bukkit.getServer().getPluginManager().callEvent(new GuildDeleteEvent(guild, true));
                GuildTerrain terrain = guild.getTerrain();
                Vector vector = terrain.getHome();
                World world = terrain.getWorld().get();
                String worldName = (world == null) ? "" : world.getName();
                this.plugin.getUserManager().getOnline().forEach(user -> user.sendMessage("guildExpire", guild.getRender().getTag(), vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), worldName));
                this.plugin.getGuildManager().unregisterGuild(guild);
                guild.getMemebers().getMembers().forEach(user -> user.setGuild(null));
            }
        });
    }
}
