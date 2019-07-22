package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

public class LeaveCommand extends Command {

    public static final String NAME = "leave";
    public static final String DESCRIPTION = "leaving from guild";
    private final UserManager userManager;

    public LeaveCommand(UserManager userManager) {
        super(NAME, DESCRIPTION, "opusc");
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        this.userManager.getUser(sender).ifPresent(user -> {
            if (user.getGuild().isPresent()) {
                Guild guild = user.getGuild().get();
                if (!guild.getMembers().getOwnerId().equals(user.getUniqueId())) {
                    GuildRender render = guild.getRender();
                    user.sendMessage("leftGuild", render.getTag(), render.getName());
                    guild.getMembers().removePlayer(user.getUniqueId());
                    guild.getMembers().getAll().forEach(member -> {
                        this.userManager.getUser(member).ifPresent(user1 -> user1.sendMessage("memberLeftGuild", user.getName()));
                    });
                    user.setGuild(null);
                } else {
                    user.sendMessage("cannotLeaveOwnGuild");
                }
            } else {
                user.sendMessage("noGuild");
            }
        });
        return true;
    }
}
