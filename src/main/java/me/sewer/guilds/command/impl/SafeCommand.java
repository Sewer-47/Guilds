package me.sewer.guilds.command.impl;

import me.sewer.guilds.command.Command;
import me.sewer.guilds.guild.Guild;
import me.sewer.guilds.user.User;

public class SafeCommand extends Command {

    public static final String NAME = "safe";

    public SafeCommand() {
        super(NAME,"schowek", "sejf");
    }

    @Override
    public boolean onCommand(User user, String... args) {
        if (!user.getGuild().isPresent()) {
            user.sendMessage("noGuild");
            return true;
        }
        Guild guild = user.getGuild().get();
        guild.getSafe().open(user);
        return true;
    }
}
