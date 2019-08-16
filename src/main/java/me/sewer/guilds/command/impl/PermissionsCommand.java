package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.User;

public class PermissionsCommand extends Command {

    public static final String NAME = "perms";

    public PermissionsCommand() {
        super(NAME, "uprawnienia", "perm", "permissions", "permission");
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        guild.getPermissionWindow().open(user);
        return true;
    }
}
