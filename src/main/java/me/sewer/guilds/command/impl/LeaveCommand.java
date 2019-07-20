package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;
import org.bukkit.command.CommandSender;

import java.util.Collection;

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
                if (!guild.getMemebers().getOwner().equals(user)) {
                    GuildRender render = guild.getRender();
                    user.sendMessage("leftGuild", render.getTag(), render.getName());
                    Collection<User> members = guild.getMemebers().getAll();
                    members.remove(user);
                    members.forEach(member -> {
                        member.sendMessage("memberLeftGuild", user.getName());
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
