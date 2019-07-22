package me.sewer.guilds.command.impl;

import me.sewer.guilds.GuildsPlugin;
import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildManager;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.guild.GuildTerrain;
import me.sewer.guilds.guild.event.GuildDeleteEvent;
import me.sewer.guilds.region.RegionManager;
import me.sewer.guilds.user.UserManager;
import me.sewer.guilds.window.WindowManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class DeleteCommand extends Command {

    public static final String NAME = "delete";
    public static final String DESCRIPTION = "deleting guild";
    private final UserManager userManager;
    private final GuildManager guildManager;
    private final RegionManager regionManager;
    private final WindowManager windowManager;

    public DeleteCommand(GuildsPlugin plugin) {
        super(NAME, DESCRIPTION, "usun", "rozwiaz");
        this.userManager = plugin.getUserManager();
        this.guildManager = plugin.getGuildManager();
        this.regionManager = plugin.getRegionManager();
        this.windowManager = plugin.getWindowManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                if (guild.getMembers().getOwnerId().equals(user.getUniqueId())) {
                    GuildDeleteEvent event = new GuildDeleteEvent(guild, false);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        GuildRender render = guild.getRender();
                        this.userManager.getOnline().forEach(user1 -> user1.sendMessage("guildDeleted", user.getName(), render.getTag(), render.getName()));
                        guild.getMembers().getAll().forEach(uuid -> {
                            this.userManager.getUser(uuid).ifPresent(member -> {
                                member.setGuild(null);
                                guild.getMembers().removePlayer(uuid);
                            });
                        });

                        this.guildManager.unregisterGuild(guild);
                        GuildTerrain guildTerrain = guild.getTerrain();
                        World world = guildTerrain.getWorld().get(); //Must be check
                        this.regionManager.byWorldId(world.getUID()).remove(guildTerrain.getRegion());
                        this.windowManager.unregisterWindow(guild.getSafe());
                    }
                } else {
                    user.sendMessage("mustBeOwner");
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
