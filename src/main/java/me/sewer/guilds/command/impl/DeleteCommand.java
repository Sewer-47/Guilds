package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.*;
import me.sewer.guilds.guild.event.GuildDeleteEvent;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.window.WindowManager;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class DeleteCommand extends Command {

    public static final String NAME = "delete";
    private final UserManager userManager;
    private final GuildManager guildManager;
    private final RegionManager regionManager;
    private final WindowManager windowManager;

    public DeleteCommand(GuildsPlugin plugin) {
        super(NAME, "usun", "rozwiaz");
        this.userManager = plugin.getUserManager();
        this.guildManager = plugin.getGuildManager();
        this.regionManager = plugin.getRegionManager();
        this.windowManager = plugin.getWindowManager();
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        if (guild.getMembers().getOwnerId().equals(user.getUniqueId())) {
            GuildDeleteEvent event = new GuildDeleteEvent(guild, false);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                GuildRender render = guild.getRender();
                this.userManager.getOnline().forEach(user1 -> user1.sendMessage("guildDeleted", user.getUsername(), render.getTag(), render.getName()));
                guild.getMembers().getAll().forEach(uuid -> {
                    this.userManager.getUser(uuid).ifPresent(member -> {
                        member.setGuild(null);
                        guild.getMembers().removePlayer(uuid);
                    });
                });

                GuildRelations guildRelations = guild.getRelations();
                guildRelations.getAll().keySet().forEach(allyId -> {
                    this.guildManager.getGuild(allyId).ifPresent(ally -> ally.getRelations().remove(guild.getUniqueId()));
                });

                this.guildManager.unregisterGuild(guild);
                GuildTerrain guildTerrain = guild.getTerrain();
                World world = guildTerrain.getWorld().get(); //Must be checked
                this.regionManager.byWorldId(world.getUID()).remove(guildTerrain.getRegion());
                this.windowManager.unregisterWindow(guild.getSafe());
                guild.getHeart().kill();
            }
        } else {
            user.sendMessage("mustBeOwner");
        }
        return true;
    }
}
