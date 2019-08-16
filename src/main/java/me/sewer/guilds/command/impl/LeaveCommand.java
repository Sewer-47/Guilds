package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.guild.GuildRender;
import me.sewer.guilds.user.User;
import me.sewer.guilds.user.UserManager;

public class LeaveCommand extends Command {

    public static final String NAME = "leave";
    private final UserManager userManager;

    public LeaveCommand(UserManager userManager) {
        super(NAME, "opusc");
        this.userManager = userManager;
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        if (guild.getMembers().getOwnerId().equals(user.getUniqueId())) {
            user.sendMessage("cannotLeaveOwnGuild");
            return true;
        }
        GuildRender render = guild.getRender();
        user.sendMessage("leftGuild", render.getTag(), render.getName());
        guild.getMembers().removePlayer(user.getUniqueId());
        guild.getMembers().getAll().forEach(member -> {
            this.userManager.getUser(member).ifPresent(user1 -> user1.sendMessage("memberLeftGuild", user.getUsername()));
        });
        user.setGuild(null);
        return true;
    }
}
